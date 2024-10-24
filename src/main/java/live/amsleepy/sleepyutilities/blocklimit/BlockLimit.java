package live.amsleepy.sleepyutilities.blocklimit;

import live.amsleepy.sleepyutilities.SleepyUtilities;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class BlockLimit implements Listener {

    private final SleepyUtilities plugin;
    private final BossBarManager bossBarManager;
    private final BlockCounter blockCounter;

    public BlockLimit(SleepyUtilities plugin) {
        this.plugin = plugin;
        this.bossBarManager = new BossBarManager(plugin);
        this.blockCounter = new BlockCounter();
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!plugin.getConfig().getBoolean("BlockLimit.Enable")) {
            return;
        }

        if (event.getPlayer().hasPermission("sleepyutilities.blocklimit.bypass")) {
            return;
        }

        Block block = event.getBlock();
        Material material = block.getType();
        Chunk chunk = block.getChunk();

        Map<String, Object> limits = plugin.getConfig().getConfigurationSection("BlockLimit.Limits").getValues(false);
        if (limits.containsKey(material.name())) {
            int maxBlocks = (int) limits.get(material.name());
            int blockCount = blockCounter.getBlockCount(chunk, material);

            if (blockCount >= maxBlocks) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(plugin.formatMessage("Too many " + material.name().toLowerCase().replace('_', ' ') + " blocks in this chunk!"));
            } else {
                blockCounter.incrementBlockCount(chunk, material);
                updateBossBars(chunk, material, maxBlocks, blockCount + 1);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!plugin.getConfig().getBoolean("BlockLimit.Enable")) {
            return;
        }

        Block block = event.getBlock();
        Material material = block.getType();
        Chunk chunk = block.getChunk();

        Map<String, Object> limits = plugin.getConfig().getConfigurationSection("BlockLimit.Limits").getValues(false);
        if (limits.containsKey(material.name())) {
            int maxBlocks = (int) limits.get(material.name());
            blockCounter.decrementBlockCount(chunk, material);
            int blockCount = blockCounter.getBlockCount(chunk, material);

            updateBossBars(chunk, material, maxBlocks, blockCount);
        }
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack newItem = player.getInventory().getItem(event.getNewSlot());

        if (newItem != null) {
            Material material = newItem.getType();
            Map<String, Object> limits = plugin.getConfig().getConfigurationSection("BlockLimit.Limits").getValues(false);
            if (limits.containsKey(material.name())) {
                int maxBlocks = (int) limits.get(material.name());
                Chunk chunk = player.getLocation().getChunk();
                int blockCount = blockCounter.getBlockCount(chunk, material);
                double progress = (double) blockCount / maxBlocks;
                bossBarManager.createBossBar(player, material.name(), blockCount, maxBlocks, progress);
            } else {
                bossBarManager.removeBossBar(player);
            }
        } else {
            bossBarManager.removeBossBar(player);
        }
    }

    private void updateBossBars(Chunk chunk, Material material, int maxBlocks, int blockCount) {
        double progress = (double) blockCount / maxBlocks;

        for (Player player : chunk.getWorld().getPlayers()) {
            if (player.getLocation().getChunk().equals(chunk)) {
                bossBarManager.updateBossBar(player, material.name(), blockCount, maxBlocks, progress);
            }
        }
    }

    public void decrementBlockCount(Material material, Location location) {
        Chunk chunk = location.getChunk();
        blockCounter.decrementBlockCount(chunk, material);
        int blockCount = blockCounter.getBlockCount(chunk, material);
        int maxBlocks = plugin.getConfig().getInt("BlockLimit.Limits." + material.name(), Integer.MAX_VALUE);
        updateBossBars(chunk, material, maxBlocks, blockCount);
    }
}