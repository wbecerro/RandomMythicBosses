package wbe.randommythicbosses;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import wbe.randommythicbosses.commands.CommandListener;
import wbe.randommythicbosses.commands.TabListener;
import wbe.randommythicbosses.config.Config;
import wbe.randommythicbosses.config.Messages;
import wbe.randommythicbosses.listeners.EventListeners;
import wbe.randommythicbosses.papi.PapiExtension;
import wbe.randommythicbosses.util.Scheduler;
import wbe.randommythicbosses.util.Utilities;

public class RandomMythicBosses extends JavaPlugin {

    private FileConfiguration configuration;

    private CommandListener commandListener;

    private TabListener tabListener;

    private EventListeners eventListeners;

    private PapiExtension papiExtension;


    public static Config config;

    public static Messages messages;

    public static Map<String, Location> bossEggs = new HashMap<>();

    public static long nextBoss;

    private Utilities utilities = new Utilities();

    public void onEnable() {
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            papiExtension = new PapiExtension();
            papiExtension.register();
        }
        saveDefaultConfig();
        getLogger().info("RandomMythicBosses enabled correctly");
        reloadConfiguration();

        commandListener = new CommandListener();
        getCommand("RandomMythicBosses").setExecutor(this.commandListener);
        tabListener = new TabListener();
        getCommand("RandomMythicBosses").setTabCompleter(this.tabListener);
        eventListeners = new EventListeners();
        eventListeners.initializeListeners();
        Scheduler.startSchedulers();
    }

    public void onDisable() {
        utilities.removeAllEggs();
        getServer().getScheduler().cancelTasks(this);
        getLogger().info("RandomMythicBosses disabled correctly");
    }

    public static RandomMythicBosses getInstance() {
        return getPlugin(RandomMythicBosses.class);
    }

    public void reloadConfiguration() {
        if(!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }
        reloadConfig();
        configuration = getConfig();
        messages = new Messages(configuration);
        config = new Config(configuration);
    }
}

