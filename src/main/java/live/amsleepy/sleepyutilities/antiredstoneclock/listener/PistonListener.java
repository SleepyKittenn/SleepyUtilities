package live.amsleepy.sleepyutilities.antiredstoneclock.listener;

import live.amsleepy.sleepyutilities.antiredstoneclock.service.RedstoneClockService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

public final class PistonListener implements Listener {
    private final RedstoneClockService redstoneClockService;

    public PistonListener(RedstoneClockService redstoneClockService) {
        this.redstoneClockService = redstoneClockService;
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        redstoneClockService.checkAndUpdateClockState(event.getBlock());
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        redstoneClockService.checkAndUpdateClockState(event.getBlock());
    }
}