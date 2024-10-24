package live.amsleepy.sleepyutilities.antiredstoneclock.listener;

import live.amsleepy.sleepyutilities.antiredstoneclock.service.RedstoneClockService;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

public class RepeaterListener implements Listener {
    private final RedstoneClockService redstoneClockService;

    public RepeaterListener(RedstoneClockService redstoneClockService) {
        this.redstoneClockService = redstoneClockService;
    }

    @EventHandler
    public void onBlockRedstoneChange(BlockRedstoneEvent event) {
        if (event.getBlock().getType() == Material.REPEATER) {
            redstoneClockService.checkAndUpdateClockState(event.getBlock());
        }
    }
}