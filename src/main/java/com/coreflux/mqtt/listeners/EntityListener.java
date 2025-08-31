package com.coreflux.mqtt.listeners;

import com.coreflux.mqtt.ConfigManager;
import com.coreflux.mqtt.MinecraftMQTTStreamer;
import com.coreflux.mqtt.events.EnemyDamageEvent;
import com.coreflux.mqtt.events.EnemyDeathEvent;
import com.coreflux.mqtt.events.EnemySpawnEvent;
import com.coreflux.mqtt.events.PlayerDamageEvent;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.HashMap;
import java.util.Map;

public class EntityListener implements Listener {

    private final MinecraftMQTTStreamer plugin;
    private final ConfigManager config;

    public EntityListener(MinecraftMQTTStreamer plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigManager();
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getEntity() instanceof Player) {
            handlePlayerDamage((Player) event.getEntity(), event);
        } else if (event.getEntity() instanceof Monster) {
            handleEnemyDamage((Monster) event.getEntity(), event);
        }
    }

    private void handlePlayerDamage(Player player, EntityDamageEvent event) {
        if (!config.isPlayerDamageEnabled()) {
            return;
        }

        PlayerDamageEvent payload = new PlayerDamageEvent(
            event.getCause().toString(),
            event.getFinalDamage(),
            player.getLocation()
        );

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("playerName", player.getName());

        plugin.getMqttManager().publish(config.getPlayerDamageTopic(), placeholders, payload);
    }

    private void handleEnemyDamage(Monster monster, EntityDamageEvent event) {
        if (!config.isEnemyDamageEnabled()) {
            return;
        }

        String damagerName = "ENVIRONMENT";
        String damagerType = event.getCause().toString();

        if (event instanceof EntityDamageByEntityEvent) {
            Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
            if (damager instanceof Projectile && ((Projectile) damager).getShooter() instanceof Entity) {
                damager = (Entity) ((Projectile) damager).getShooter();
            }
            damagerName = damager.getName();
            damagerType = damager.getType().toString();
        }

        EnemyDamageEvent payload = new EnemyDamageEvent(
            damagerName,
            damagerType,
            event.getFinalDamage(),
            monster.getLocation()
        );

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("enemyType", monster.getType().toString().toLowerCase());
        placeholders.put("enemyId", monster.getUniqueId().toString());

        plugin.getMqttManager().publish(config.getEnemyDamageTopic(), placeholders, payload);
    }
    
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.isCancelled() || !(event.getEntity() instanceof Monster)) {
            return;
        }

        if (!config.isEnemySpawnEnabled()) {
            return;
        }

        Monster monster = (Monster) event.getEntity();
        EnemySpawnEvent payload = new EnemySpawnEvent(monster.getLocation());

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("enemyType", monster.getType().toString().toLowerCase());
        placeholders.put("enemyId", monster.getUniqueId().toString());

        plugin.getMqttManager().publish(config.getEnemySpawnTopic(), placeholders, payload);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Monster) {
            if (!config.isEnemyDeathEnabled()) {
                return;
            }

            Monster monster = (Monster) event.getEntity();
            Player killer = monster.getKiller();

            String killerName = "ENVIRONMENT";
            String killerType = "NONE";

            if (killer != null) {
                killerName = killer.getName();
                killerType = killer.getType().toString();
            }

            EnemyDeathEvent payload = new EnemyDeathEvent(
                killerName,
                killerType,
                monster.getLocation()
            );

            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("enemyType", monster.getType().toString().toLowerCase());
            placeholders.put("enemyId", monster.getUniqueId().toString());

            plugin.getMqttManager().publish(config.getEnemyDeathTopic(), placeholders, payload);
        }
    }
}
