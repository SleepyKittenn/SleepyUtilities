package live.amsleepy.sleepyutilities.commands;

import live.amsleepy.sleepyutilities.SleepyUtilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatusCommand implements CommandExecutor {

    private final SleepyUtilities plugin;

    public StatusCommand(SleepyUtilities plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && !sender.hasPermission("sleepyutilities.status")) {
            sender.sendMessage(plugin.formatMessage("You do not have permission to use this command."));
            return true;
        }

        sender.sendMessage(plugin.formatMessage("Feature Status:"));
        sender.sendMessage("CommandBlocker: " + (plugin.getConfig().getBoolean("CommandBlocker.Enable") ? "Enabled" : "Disabled"));
        sender.sendMessage("BlockLimit: " + (plugin.getConfig().getBoolean("BlockLimit.Enable") ? "Enabled" : "Disabled"));
        sender.sendMessage("EntityLimit: " + (plugin.getConfig().getBoolean("EntityLimit.Enable") ? "Enabled" : "Disabled"));
        sender.sendMessage("AntiRedstoneClock: " + (plugin.getConfig().getBoolean("AntiRedstoneClock.Enable") ? "Enabled" : "Disabled"));

        return true;
    }
}