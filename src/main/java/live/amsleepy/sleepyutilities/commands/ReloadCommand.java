package live.amsleepy.sleepyutilities.commands;

import live.amsleepy.sleepyutilities.SleepyUtilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand implements CommandExecutor {

    private final SleepyUtilities plugin;

    public ReloadCommand(SleepyUtilities plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || !args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(plugin.formatMessage("Usage: /" + label + " reload"));
            return true;
        }

        if (sender instanceof Player && !sender.hasPermission("sleepyutilities.reload")) {
            sender.sendMessage(plugin.formatMessage("You do not have permission to use this command."));
            return true;
        }

        plugin.reloadPluginConfig();
        sender.sendMessage(plugin.formatMessage("Configuration reloaded."));
        return true;
    }
}