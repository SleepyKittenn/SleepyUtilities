package live.amsleepy.sleepyutilities.commands;

import live.amsleepy.sleepyutilities.SleepyUtilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleCommand implements CommandExecutor {

    private final SleepyUtilities plugin;

    public ToggleCommand(SleepyUtilities plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && !sender.hasPermission("sleepyutilities.toggle")) {
            sender.sendMessage(plugin.formatMessage("You do not have permission to use this command."));
            return true;
        }

        if (args.length != 3 || !args[2].equalsIgnoreCase("enable") && !args[2].equalsIgnoreCase("disable")) {
            sender.sendMessage(plugin.formatMessage("Usage: /sleepyutilities toggle <service> <enable/disable>"));
            return true;
        }

        String service = args[1].toLowerCase();
        boolean enable = args[2].equalsIgnoreCase("enable");

        switch (service) {
            case "commandblocker":
                plugin.getConfig().set("CommandBlocker.Enable", enable);
                break;
            case "blocklimit":
                plugin.getConfig().set("BlockLimit.Enable", enable);
                break;
            case "entitylimit":
                plugin.getConfig().set("EntityLimit.Enable", enable);
                break;
            case "antiredstoneclock":
                plugin.getConfig().set("AntiRedstoneClock.Enable", enable);
                break;
            default:
                sender.sendMessage(plugin.formatMessage("Invalid service. Use CommandBlocker, BlockLimit, EntityLimit, or AntiRedstoneClock."));
                return true;
        }

        plugin.saveConfig();
        plugin.reloadConfig();
        sender.sendMessage(plugin.formatMessage(service + " has been " + (enable ? "enabled" : "disabled") + "."));
        return true;
    }
}