package live.amsleepy.sleepyutilities.antiredstoneclock.manager;

import live.amsleepy.sleepyutilities.SleepyUtilities;
import org.bukkit.Chunk;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class RedstoneChunkManager {
    private final Set<String> lockedChunks = new HashSet<>();
    private final File file;

    public RedstoneChunkManager(SleepyUtilities plugin) {
        File folder = plugin.getDataFolder();
        if (!folder.exists()) {
            folder.mkdirs();
        }
        this.file = new File(folder, "locked_chunks.txt");
        loadLockedChunks();
    }

    public void lockChunk(Chunk chunk) {
        String chunkKey = getChunkKey(chunk);
        lockedChunks.add(chunkKey);
        saveLockedChunks();
    }

    public boolean unlockChunk(Chunk chunk) {
        String chunkKey = getChunkKey(chunk);
        loadLockedChunks(); // Reload the locked chunks from the file
        if (lockedChunks.contains(chunkKey)) {
            lockedChunks.remove(chunkKey);
            saveLockedChunks();
            System.out.println("Chunk unlocked: " + chunkKey);
            return true;
        } else {
            System.out.println("Chunk isn't locked: " + chunkKey);
            return false;
        }
    }

    public boolean isChunkLocked(Chunk chunk) {
        loadLockedChunks(); // Reload the locked chunks from the file
        return lockedChunks.contains(getChunkKey(chunk));
    }

    private String getChunkKey(Chunk chunk) {
        return chunk.getWorld().getName() + ":" + chunk.getX() + "," + chunk.getZ();
    }

    private void saveLockedChunks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String chunkKey : lockedChunks) {
                writer.write(chunkKey);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadLockedChunks() {
        lockedChunks.clear(); // Clear the current set before loading
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lockedChunks.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}