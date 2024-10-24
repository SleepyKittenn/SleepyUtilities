package live.amsleepy.sleepyutilities.antiredstoneclock.model;

import org.bukkit.Location;
import java.util.LinkedList;
import java.util.Queue;

public class RedstoneClock {
    private final Location location;
    private int triggerCount;
    private final Queue<Long> triggerTimes;

    public RedstoneClock(Location location) {
        this.location = location;
        this.triggerCount = 0;
        this.triggerTimes = new LinkedList<>();
    }

    public Location getLocation() {
        return location;
    }

    public int getTriggerCount() {
        return triggerCount;
    }

    public void incrementTriggerCount() {
        triggerCount++;
    }

    public Queue<Long> getTriggerTimes() {
        return triggerTimes;
    }
}