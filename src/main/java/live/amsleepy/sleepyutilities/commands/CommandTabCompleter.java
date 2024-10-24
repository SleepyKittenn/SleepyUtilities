package live.amsleepy.sleepyutilities.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandTabCompleter implements TabCompleter {

    private static final List<String> COMMANDS = Arrays.asList("reload", "status", "toggle", "antiredstoneclock");
    private static final List<String> SERVICES = Arrays.asList("CommandBlocker", "BlockLimit", "EntityLimit", "AntiRedstoneClock");
    private static final List<String> TOGGLE_ACTIONS = Arrays.asList("enable", "disable");
    private static final List<String> ANTIRESTONECLOCK_SUBCOMMANDS = Arrays.asList("lock", "unlock");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            for (String cmd : COMMANDS) {
                if (cmd.startsWith(args[0].toLowerCase())) {
                    completions.add(cmd);
                }
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("toggle")) {
                for (String service : SERVICES) {
                    if (service.toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(service);
                    }
                }
            } else if (args[0].equalsIgnoreCase("antiredstoneclock")) {
                for (String subCmd : ANTIRESTONECLOCK_SUBCOMMANDS) {
                    if (subCmd.startsWith(args[1].toLowerCase())) {
                        completions.add(subCmd);
                    }
                }
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("toggle")) {
            for (String action : TOGGLE_ACTIONS) {
                if (action.startsWith(args[2].toLowerCase())) {
                    completions.add(action);
                }
            }
        }
        return completions;
    }
}