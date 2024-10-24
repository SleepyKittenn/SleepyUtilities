// ComparatorListener.java
package live.amsleepy.sleepyutilities.antiredstoneclock.listener;

import live.amsleepy.sleepyutilities.antiredstoneclock.service.RedstoneClockService;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

public final class ComparatorListener implements Listener {
    private final RedstoneClockService redstoneClockService;

    public ComparatorListener(RedstoneClockService redstoneClockService) {
        this.redstoneClockService = redstoneClockService;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onRedstoneComparatorClock(BlockRedstoneEvent blockRedstoneEvent) {
        var block = blockRedstoneEvent.getBlock();
        var type = block.getType();
        if (type != Material.COMPARATOR) return;

        if (blockRedstoneEvent.getOldCurrent() != 0) return;

        redstoneClockService.checkAndUpdateClockState(block);
    }
}