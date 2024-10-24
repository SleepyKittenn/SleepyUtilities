package live.amsleepy.sleepyutilities.commands;

import live.amsleepy.sleepyutilities.SleepyUtilities;
import live.amsleepy.sleepyutilities.antiredstoneclock.manager.RedstoneChunkManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor {

    private final SleepyUtilities plugin;
    private final ReloadCommand reloadCommand;
    private final StatusCommand statusCommand;
    private final ToggleCommand toggleCommand;
    private final RedstoneClockCommand redstoneClockCommand;

    public MainCommand(SleepyUtilities plugin, RedstoneChunkManager chunkManager) {
        this.plugin = plugin;
        this.reloadCommand = new ReloadCommand(plugin);
        this.statusCommand = new StatusCommand(plugin);
        this.toggleCommand = new ToggleCommand(plugin);
        this.redstoneClockCommand = new RedstoneClockCommand(chunkManager);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(plugin.formatMessage("Usage: /" + label + " <reload|status|toggle|antiredstoneclock>"));
            return true;
        }

        String subCommand = args[0].toLowerCase();
        switch (subCommand) {
            case "reload":
                return reloadCommand.onCommand(sender, command, label, args);

            case "status":
                return statusCommand.onCommand(sender, command, label, args);

            case "toggle":
                return toggleCommand.onCommand(sender, command, label, args);

            case "antiredstoneclock":
                if (args.length < 2) {
                    sender.sendMessage(plugin.formatMessage("Usage: /" + label + " antiredstoneclock <lock|unlock>"));
                    return true;
                }
                return redstoneClockCommand.onCommand(sender, command, label, new String[]{args[1]});

            default:
                sender.sendMessage(plugin.formatMessage("Usage: /" + label + " <reload|status|toggle|antiredstoneclock>"));
                break;
        }
        return true;
    }
}