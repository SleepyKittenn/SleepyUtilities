package live.amsleepy.sleepyutilities;

import live.amsleepy.sleepyutilities.blocklimit.BlockLimit;
import live.amsleepy.sleepyutilities.commandblocker.CommandBlocker;
import live.amsleepy.sleepyutilities.commands.CommandTabCompleter;
import live.amsleepy.sleepyutilities.commands.MainCommand;
import live.amsleepy.sleepyutilities.entitylimit.EntityLimit;
import live.amsleepy.sleepyutilities.antiredstoneclock.listener.PlayerListener;
import live.amsleepy.sleepyutilities.antiredstoneclock.listener.PistonListener;
import live.amsleepy.sleepyutilities.antiredstoneclock.listener.RedstoneListener;
import live.amsleepy.sleepyutilities.antiredstoneclock.listener.ComparatorListener;
import live.amsleepy.sleepyutilities.antiredstoneclock.listener.ObserverListener;
import live.amsleepy.sleepyutilities.antiredstoneclock.listener.RepeaterListener;
import live.amsleepy.sleepyutilities.antiredstoneclock.service.RedstoneClockService;
import live.amsleepy.sleepyutilities.antiredstoneclock.manager.RedstoneChunkManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;

public final class SleepyUtilities extends JavaPlugin {

    private static final String PREFIX = ChatColor.LIGHT_PURPLE + "[SleepyUtilities] " + ChatColor.RESET;
    private static final String COMMAND_BLOCKER_ENABLE = "CommandBlocker.Enable";
    private static final String BLOCK_LIMIT_ENABLE = "BlockLimit.Enable";
    private static final String ENTITY_LIMIT_ENABLE = "EntityLimit.Enable";
    private static final String ANTI_REDSTONE_CLOCK_ENABLE = "AntiRedstoneClock.Enable";

    private Listener commandBlockerListener;
    private Listener blockLimitListener;
    private Listener entityLimitListener;
    private RedstoneClockService redstoneClockService;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        if (getConfig().getBoolean(COMMAND_BLOCKER_ENABLE)) {
            commandBlockerListener = new CommandBlocker(this);
            getServer().getPluginManager().registerEvents(commandBlockerListener, this);
        }

        if (getConfig().getBoolean(BLOCK_LIMIT_ENABLE)) {
            blockLimitListener = new BlockLimit(this);
            getServer().getPluginManager().registerEvents(blockLimitListener, this);
        }

        if (getConfig().getBoolean(ENTITY_LIMIT_ENABLE)) {
            entityLimitListener = new EntityLimit(this);
            getServer().getPluginManager().registerEvents(entityLimitListener, this);
        }

        RedstoneChunkManager chunkManager = null;
        if (getConfig().getBoolean(ANTI_REDSTONE_CLOCK_ENABLE)) {
            chunkManager = new RedstoneChunkManager(this);
            redstoneClockService = new RedstoneClockService((BlockLimit) blockLimitListener, getConfig(), this);
            getServer().getPluginManager().registerEvents(redstoneClockService, this);
            getServer().getPluginManager().registerEvents(new PlayerListener(redstoneClockService), this);
            getServer().getPluginManager().registerEvents(new PistonListener(redstoneClockService), this);
            getServer().getPluginManager().registerEvents(new RedstoneListener(redstoneClockService), this);
            getServer().getPluginManager().registerEvents(new ComparatorListener(redstoneClockService), this);
            getServer().getPluginManager().registerEvents(new ObserverListener(redstoneClockService), this);
            getServer().getPluginManager().registerEvents(new RepeaterListener(redstoneClockService), this);
        }

        PluginCommand command = getCommand("sleepyutilities");
        if (command != null) {
            command.setExecutor(new MainCommand(this, chunkManager));
            command.setTabCompleter(new CommandTabCompleter());
        }
    }

    @Override
    public void onDisable() {
        // Unregister all listeners
        HandlerList.unregisterAll(this);
    }

    public void reloadPluginConfig() {
        reloadConfig();
    }

    public String formatMessage(String message) {
        return PREFIX + message;
    }
}