package live.amsleepy.sleepyutilities.antiredstoneclock.listener;

import live.amsleepy.sleepyutilities.antiredstoneclock.service.RedstoneClockService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerListener implements Listener {
    private final RedstoneClockService redstoneClockService;

    public PlayerListener(RedstoneClockService redstoneClockService) {
        this.redstoneClockService = redstoneClockService;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Handle player join event
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        redstoneClockService.removeClockByLocation(event.getBlock().getLocation());
    }
}