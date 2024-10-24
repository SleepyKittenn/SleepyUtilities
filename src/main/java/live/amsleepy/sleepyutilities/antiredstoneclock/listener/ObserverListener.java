package live.amsleepy.sleepyutilities.antiredstoneclock.listener;

import live.amsleepy.sleepyutilities.antiredstoneclock.service.RedstoneClockService;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

public final class ObserverListener implements Listener {
    private final Material material;
    private final RedstoneClockService redstoneClockService;

    public ObserverListener(RedstoneClockService redstoneClockService) {
        this.material = Material.OBSERVER;
        this.redstoneClockService = redstoneClockService;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onRedstoneObserverClock(BlockRedstoneEvent blockRedstoneEvent) {
        var block = blockRedstoneEvent.getBlock();
        var type = block.getType();
        if (type != material) return;

        if (blockRedstoneEvent.getOldCurrent() != 0) return;

        redstoneClockService.checkAndUpdateClockState(block);
    }
}