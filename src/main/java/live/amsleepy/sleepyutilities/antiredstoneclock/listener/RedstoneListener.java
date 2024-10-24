// RedstoneListener.java
package live.amsleepy.sleepyutilities.antiredstoneclock.listener;

import live.amsleepy.sleepyutilities.antiredstoneclock.service.RedstoneClockService;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

public final class RedstoneListener implements Listener {
    private final RedstoneClockService redstoneClockService;

    public RedstoneListener(RedstoneClockService redstoneClockService) {
        this.redstoneClockService = redstoneClockService;
    }

    @EventHandler
    public void onRedstoneEvent(BlockRedstoneEvent event) {
        if (event.getBlock().getType() == Material.REDSTONE_WIRE) {
            redstoneClockService.checkAndUpdateClockState(event.getBlock());
        }
    }
}