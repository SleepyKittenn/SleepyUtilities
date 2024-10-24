package live.amsleepy.sleepyutilities.blocklimit;

import org.bukkit.boss.BossBar;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class BossBarManager {

    private final Plugin plugin;
    private final Map<Player, BossBar> playerBossBars = new HashMap<>();

    public BossBarManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void createBossBar(Player player, String blockName, int blockCount, int maxBlocks, double progress) {
        BossBar bossBar = playerBossBars.get(player);
        if (bossBar == null) {
            bossBar = plugin.getServer().createBossBar("", BarColor.GREEN, BarStyle.SOLID);
            bossBar.addPlayer(player);
            playerBossBars.put(player, bossBar);
        }
        updateBossBar(player, blockName, blockCount, maxBlocks, progress);
    }

    public void updateBossBar(Player player, String blockName, int blockCount, int maxBlocks, double progress) {
        BossBar bossBar = playerBossBars.get(player);
        if (bossBar != null) {
            // Ensure blockCount does not exceed maxBlocks
            int displayedBlockCount = Math.min(blockCount, maxBlocks);
            // Clamp progress between 0.0 and 1.0
            double displayedProgress = Math.max(0.0, Math.min(progress, 1.0));

            bossBar.setTitle("Blocks placed: " + displayedBlockCount + " / " + maxBlocks + " (" + blockName + ")");
            bossBar.setProgress(displayedProgress);
            bossBar.setColor(getBarColor(displayedProgress));
        }
    }

    public void removeBossBar(Player player) {
        BossBar bossBar = playerBossBars.remove(player);
        if (bossBar != null) {
            bossBar.removeAll();
        }
    }

    private BarColor getBarColor(double progress) {
        if (progress < 0.5) {
            return BarColor.GREEN;
        } else if (progress < 0.75) {
            return BarColor.YELLOW;
        } else {
            return BarColor.RED;
        }
    }
}