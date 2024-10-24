package live.amsleepy.sleepyutilities.antiredstoneclock.service;

import live.amsleepy.sleepyutilities.SleepyUtilities;
import live.amsleepy.sleepyutilities.blocklimit.BlockLimit;
import live.amsleepy.sleepyutilities.antiredstoneclock.manager.RedstoneChunkManager;
import live.amsleepy.sleepyutilities.antiredstoneclock.model.RedstoneClock;
import live.amsleepy.sleepyutilities.notification.DiscordWebhookNotifier;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Queue;

public final class RedstoneClockService implements Listener {
    private final ConcurrentHashMap<Location, RedstoneClock> activeClocks = new ConcurrentHashMap<>();
    private final BlockLimit blockLimit;
    private final RedstoneChunkManager chunkManager;
    private final int ticksChecked;
    private final int timeLimitSeconds;
    private final DiscordWebhookNotifier discordNotifier;
    private final SleepyUtilities plugin;

    public RedstoneClockService(BlockLimit blockLimit, FileConfiguration config, SleepyUtilities plugin) {
        this.blockLimit = blockLimit;
        this.chunkManager = new RedstoneChunkManager(plugin);
        this.ticksChecked = config.getInt("AntiRedstoneClock.ticksChecked", 25);
        this.timeLimitSeconds = config.getInt("AntiRedstoneClock.timeLimitSeconds", 5);
        this.discordNotifier = new DiscordWebhookNotifier(config.getString("DiscordWebhook.Url"));
        this.plugin = plugin;
    }

    public void checkAndUpdateClockState(Block block) {
        if (!plugin.getConfig().getBoolean("AntiRedstoneClock.Enable", true)) {
            return;
        }

        Location location = block.getLocation();
        RedstoneClock clock = activeClocks.get(location);
        if (clock != null) {
            clock.incrementTriggerCount();
            if (isSuspicious(clock)) {
                lockChunk(block.getChunk());
                notifyPlayers(location);
                sendWebhookNotification(location);
            }
        } else {
            activeClocks.put(location, new RedstoneClock(location));
        }
    }

    private boolean isSuspicious(RedstoneClock clock) {
        long currentTime = System.currentTimeMillis() / 1000;
        Queue<Long> triggerTimes = clock.getTriggerTimes();
        triggerTimes.add(currentTime);

        while (!triggerTimes.isEmpty() && (currentTime - triggerTimes.peek() > timeLimitSeconds)) {
            triggerTimes.poll();
        }

        return triggerTimes.size() >= ticksChecked;
    }

    private void lockChunk(Chunk chunk) {
        chunkManager.lockChunk(chunk);
        Bukkit.getLogger().info("Locked redstone in chunk: " + chunk);
    }

    private void unlockChunk(Chunk chunk) {
        chunkManager.unlockChunk(chunk);
        Bukkit.getLogger().info("Unlocked redstone in chunk: " + chunk);
    }

    private void notifyPlayers(Location location) {
        String message = plugin.formatMessage("Redstone clock detected and locked at: " + location);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("sleepyutilities.antiredstoneclock.notify")) {
                player.sendMessage(message);
            }
        }
    }

    private void sendWebhookNotification(Location location) {
        String coordinates = "X: " + location.getBlockX() + " Y: " + location.getBlockY() + " Z: " + location.getBlockZ();
        String world = location.getWorld().getName();
        discordNotifier.sendEmbedNotification("Redstone Clock Detected", "A redstone clock has been detected and locked.", "Redstone", coordinates, world);
    }

    @EventHandler
    public void onBlockRedstoneChange(BlockRedstoneEvent event) {
        if (!plugin.getConfig().getBoolean("AntiRedstoneClock.Enable", true)) {
            return;
        }

        if (chunkManager.isChunkLocked(event.getBlock().getChunk())) {
            event.setNewCurrent(0);
        }
    }

    @EventHandler
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        if (!plugin.getConfig().getBoolean("AntiRedstoneClock.Enable", true)) {
            return;
        }

        if (chunkManager.isChunkLocked(event.getBlock().getChunk())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        if (!plugin.getConfig().getBoolean("AntiRedstoneClock.Enable", true)) {
            return;
        }

        if (chunkManager.isChunkLocked(event.getBlock().getChunk())) {
            event.setCancelled(true);
        }
    }

    public void removeClock(RedstoneClock clock) {
        activeClocks.remove(clock.getLocation());
    }

    public void removeClockByLocation(Location location) {
        activeClocks.remove(location);
    }
}