new Vue({
    el: '#app',
    data: {
        currentView: 'overview',
        connectionStatus: 'disconnected',
        world: {},
        players: {},
        devices: {},
        enemies: {},
        eventLog: [],
        mqttClient: null,
        commandText: '',
        chatLog: [],
        chatText: ''
    },
    computed: {
        sortedEventLog() {
            return this.eventLog.slice().sort((a, b) => b.timestamp - a.timestamp);
        }
    },
    methods: {
        connectMqtt() {
            const MQTT_BROKER = "localhost";
            const MQTT_PORT = 9001;
            const MQTT_PATH = "/mqtt";
            const MQTT_USER = "root";
            const MQTT_PASS = "coreflux";
            
            this.mqttClient = new Paho.Client(MQTT_BROKER, MQTT_PORT, MQTT_PATH, "mc_dashboard_" + Date.now());

            this.mqttClient.onConnectionLost = (responseObject) => {
                if (responseObject.errorCode !== 0) {
                    console.log("Connection lost: " + responseObject.errorMessage);
                    this.connectionStatus = 'disconnected';
                }
            };

            this.mqttClient.onMessageArrived = (message) => {
                this.handleMqttMessage(message.destinationName, message.payloadString);
            };

            this.mqttClient.connect({
                userName: MQTT_USER,
                password: MQTT_PASS,
                onSuccess: () => {
                    console.log("Connected to MQTT broker");
                    this.connectionStatus = 'connected';
                    this.mqttClient.subscribe("server/#");
                },
                onFailure: (message) => {
                    console.error("Connection failed: " + message.errorMessage);
                    this.connectionStatus = 'disconnected';
                }
            });
        },
        handleMqttMessage(topic, payload) {
            try {
                const data = JSON.parse(payload);
                this.updateEventLog(topic, payload, data.timestamp);
                const parts = topic.split('/');

                if (topic.startsWith('server/world/time')) {
                    this.$set(this.world, 'time', data);
                } else if (topic.startsWith('server/world/weather')) {
                    this.$set(this.world, 'weather', data);
                } else if (topic.startsWith('server/players/')) {
                    this.updatePlayer(parts[2], parts.slice(3).join('/'), data);
                } else if (topic.startsWith('server/devices/')) {
                    this.updateDevice(parts[2], parts[3], parts.slice(4).join('/'), data);
                } else if (topic.startsWith('server/enemies/')) {
                    this.updateEnemy(parts[2], parts[3], parts.slice(4).join('/'), data);
                } else if (topic.startsWith('server/chat/messages')) {
                    this.chatLog.push(data);
                    if (this.chatLog.length > 100) this.chatLog.shift();
                }
            } catch (e) {
                 this.updateEventLog(topic, payload, Date.now());
                 const parts = topic.split('/');
                 if (topic.includes('/status/')) {
                     const [,,playerName,,metric,sub] = parts;
                     if(this.players[playerName]) {
                        this.$set(this.players[playerName].status[metric], sub, payload);
                     }
                 }
            }
        },
        updatePlayer(name, path, data) {
            if (!this.players[name]) {
                this.$set(this.players, name, { events: {}, status: {}, location: {}, online: false });
            }
            if(path.startsWith('status')) {
                const [,metric,sub] = path.split('/');
                if(!this.players[name].status[metric]) {
                    this.$set(this.players[name].status, metric, {});
                }
                this.$set(this.players[name].status[metric], sub, data);

            } else {
                const eventType = path.split('/')[1];
                 if(eventType === 'join') {
                    this.players[name].online = true;
                    this.players[name].location = data.location;
                } else if(eventType === 'quit' || eventType === 'died') {
                    this.players[name].online = false;
                    this.players[name].location = data.location;
                } else if (data.location) {
                    this.players[name].location = data.location;
                }
                this.$set(this.players[name].events, eventType, data);
            }
        },
        updateDevice(type, id, path, data) {
             const fullId = `${type}-${id}`;
            if (!this.devices[fullId]) {
                this.$set(this.devices, fullId, { type: type, id: id, events: {}, status: 'idle' });
            }
            const eventType = path.split('/')[1];

            if (eventType === 'burn') {
                this.devices[fullId].status = 'burning';
            } else if (eventType === 'smelt') {
                this.devices[fullId].status = 'smelting';
            } else if (eventType === 'extract') {
                 this.devices[fullId].status = 'idle';
            }
            
            this.$set(this.devices[fullId].events, eventType, data);
        },
        updateEnemy(type, id, path, data) {
            const fullId = `${type}-${id}`;
            if (!this.enemies[fullId]) {
                this.$set(this.enemies, fullId, { type: type, id: id, events: {}, status: 'unknown', location: {} });
            }

            const eventType = path.split('/')[1];
            
            if (eventType === 'spawn') {
                this.enemies[fullId].status = 'alive';
                this.enemies[fullId].location = data.location;
            } else if (eventType === 'death') {
                this.enemies[fullId].status = 'dead';
                 this.enemies[fullId].location = data.location;
            } else if (eventType === 'damage') {
                this.enemies[fullId].location = data.location;
            }
            
            this.$set(this.enemies[fullId].events, eventType, data);
        },
        updateEventLog(topic, payload, timestamp) {
            this.eventLog.unshift({ topic, payload, timestamp });
            if (this.eventLog.length > 100) {
                this.eventLog.pop();
            }
        },
        sendCommand() {
            if (!this.commandText || !this.mqttClient) return;
            const topic = "server/commands/execute";
            const message = new Paho.Message(this.commandText);
            message.destinationName = topic;
            this.mqttClient.send(message);
            this.commandText = '';
        },
        killPlayer(playerName) {
            this.commandText = `kill ${playerName}`;
            this.sendCommand();
        },
        teleportPlayer(playerName, coords) {
            this.commandText = `tp ${playerName} ${coords}`;
            this.sendCommand();
        },
        giveItem(playerName, item, amount) {
            this.commandText = `give ${playerName} ${item} ${amount}`;
            this.sendCommand();
        },
        sendChatMessage() {
            if (!this.chatText || !this.mqttClient) return;
            const topic = "server/chat/send";
            const message = new Paho.Message(this.chatText);
            message.destinationName = topic;
            this.mqttClient.send(message);
            this.chatText = '';
        }
    },
    created() {
        this.connectMqtt();
    }
});
