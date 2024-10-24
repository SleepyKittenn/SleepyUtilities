package live.amsleepy.sleepyutilities.commandblocker;

import live.amsleepy.sleepyutilities.SleepyUtilities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandBlocker implements Listener {

    private final SleepyUtilities plugin;

    public CommandBlocker(SleepyUtilities plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.getPlayer().hasPermission("sleepyutilities.nocommands.bypass")) {
            return;
        }

        String command = event.getMessage().split(" ")[0].toLowerCase();
        if (plugin.getConfig().getStringList("blocked-commands").contains(command)) {
            event.getPlayer().sendMessage(plugin.formatMessage(plugin.getConfig().getString("block-message")));
            event.setCancelled(true);
        }
    }
}