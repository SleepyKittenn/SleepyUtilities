package live.amsleepy.sleepyutilities.commands;

import live.amsleepy.sleepyutilities.antiredstoneclock.manager.RedstoneChunkManager;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RedstoneClockCommand implements CommandExecutor {
    private final RedstoneChunkManager chunkManager;

    public RedstoneClockCommand(RedstoneChunkManager chunkManager) {
        this.chunkManager = chunkManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        Chunk chunk = player.getLocation().getChunk();

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("lock")) {
                chunkManager.lockChunk(chunk);
                player.sendMessage("Redstone locked in this chunk.");
                return true;
            } else if (args[0].equalsIgnoreCase("unlock")) {
                if (chunkManager.unlockChunk(chunk)) {
                    player.sendMessage("Redstone unlocked in this chunk.");
                } else {
                    player.sendMessage("This chunk isn't locked.");
                }
                return true;
            }
        }

        player.sendMessage("Usage: /sleepyutilities antiredstoneclock <lock|unlock>");
        return false;
    }
}