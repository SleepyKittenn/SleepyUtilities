package live.amsleepy.sleepyutilities.blocklimit;

import org.bukkit.Chunk;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class BlockCounter {
    private final Map<String, Integer> blockCounts = new HashMap<>();

    public int getBlockCount(Chunk chunk, Material material) {
        return blockCounts.getOrDefault(getKey(chunk, material), 0);
    }

    public void incrementBlockCount(Chunk chunk, Material material) {
        String key = getKey(chunk, material);
        int newCount = blockCounts.getOrDefault(key, 0) + 1;
        blockCounts.put(key, newCount);
        // System.out.println("Incremented block count for " + key + " to " + newCount);
    }

    public void decrementBlockCount(Chunk chunk, Material material) {
        String key = getKey(chunk, material);
        int currentCount = blockCounts.getOrDefault(key, 0);
        if (currentCount > 0) {
            blockCounts.put(key, currentCount - 1);
            // System.out.println("Decremented block count for " + key + " to " + (currentCount - 1));
        }
    }

    private String getKey(Chunk chunk, Material material) {
        return chunk.getWorld().getName() + ":" + chunk.getX() + ":" + chunk.getZ() + ":" + material.name();
    }
}