package live.amsleepy.sleepyutilities.entitylimit;

import live.amsleepy.sleepyutilities.SleepyUtilities;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Map;

public class EntityLimit implements Listener {

    private final SleepyUtilities plugin;

    public EntityLimit(SleepyUtilities plugin) {
        this.plugin = plugin;
        startEntityCheckTask();
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!plugin.getConfig().getBoolean("EntityLimit.Enable")) {
            return;
        }

        Entity entity = event.getEntity();
        Chunk chunk = entity.getLocation().getChunk();
        EntityType entityType = entity.getType();

        Map<String, Object> limits = plugin.getConfig().getConfigurationSection("EntityLimit.Limits").getValues(false);
        if (limits.containsKey(entityType.name())) {
            int maxEntities = (int) limits.get(entityType.name());
            int entityCount = countEntitiesInChunk(chunk, entityType);

            if (entityCount >= maxEntities) {
                event.setCancelled(true);
                entity.getWorld().getPlayers().forEach(player -> player.sendMessage(plugin.formatMessage("Too many " + entityType.name().toLowerCase().replace('_', ' ') + " entities in this chunk!")));
            }
        }
    }

    @EventHandler
    public void onEntityTeleport(EntityTeleportEvent event) {
        handleEntityMovement(event.getEntity(), event.getTo().getChunk());
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        handleEntityMovement(event.getPlayer(), event.getTo().getChunk());
    }

    @EventHandler
    public void onVehicleMove(VehicleMoveEvent event) {
        handleEntityMovement(event.getVehicle(), event.getTo().getChunk());
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!plugin.getConfig().getBoolean("EntityLimit.Enable")) {
            return;
        }

        Entity entity = event.getEntity();
        if (entity.getType() == EntityType.SNOWBALL) {
            Chunk chunk = entity.getLocation().getChunk();
            EntityType entityType = entity.getType();

            Map<String, Object> limits = plugin.getConfig().getConfigurationSection("EntityLimit.Limits").getValues(false);
            if (limits.containsKey(entityType.name())) {
                int maxEntities = (int) limits.get(entityType.name());
                int entityCount = countEntitiesInChunk(chunk, entityType);

                if (entityCount >= maxEntities) {
                    event.setCancelled(true);
                    entity.getWorld().getPlayers().forEach(player -> player.sendMessage(plugin.formatMessage("Too many " + entityType.name().toLowerCase().replace('_', ' ') + " entities in this chunk!")));
                }
            }
        }
    }

    private void handleEntityMovement(Entity entity, Chunk toChunk) {
        if (!plugin.getConfig().getBoolean("EntityLimit.Enable")) {
            return;
        }

        EntityType entityType = entity.getType();
        Map<String, Object> limits = plugin.getConfig().getConfigurationSection("EntityLimit.Limits").getValues(false);
        if (limits.containsKey(entityType.name())) {
            int maxEntities = (int) limits.get(entityType.name());
            int entityCount = countEntitiesInChunk(toChunk, entityType);

            if (entityCount >= maxEntities) {
                entity.remove();
                toChunk.getWorld().getPlayers().forEach(player -> player.sendMessage(plugin.formatMessage("Entity limit exceeded for " + entityType.name().toLowerCase().replace('_', ' ') + " in this chunk!")));
            }
        }
    }

    private int countEntitiesInChunk(Chunk chunk, EntityType entityType) {
        int count = 0;
        for (Entity entity : chunk.getEntities()) {
            if (entity.getType() == entityType) {
                count++;
            }
        }
        return count;
    }

    private void startEntityCheckTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!plugin.getConfig().getBoolean("EntityLimit.Enable")) {
                    return;
                }

                for (Chunk chunk : Bukkit.getWorlds().stream()
                        .flatMap(world -> Arrays.stream(world.getLoadedChunks()))
                        .toArray(Chunk[]::new)) {
                    for (EntityType entityType : EntityType.values()) {
                        Map<String, Object> limits = plugin.getConfig().getConfigurationSection("EntityLimit.Limits").getValues(false);
                        if (limits.containsKey(entityType.name())) {
                            int maxEntities = (int) limits.get(entityType.name());
                            int entityCount = countEntitiesInChunk(chunk, entityType);

                            if (entityCount > maxEntities) {
                                int excess = entityCount - maxEntities;
                                for (Entity entity : chunk.getEntities()) {
                                    if (entity.getType() == entityType && excess > 0) {
                                        entity.remove();
                                        excess--;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L * 5); // Run every 5 seconds
    }
}