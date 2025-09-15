package wbe.randommythicbosses.listeners;

import org.bukkit.plugin.PluginManager;
import wbe.randommythicbosses.RandomMythicBosses;

public class EventListeners {

    RandomMythicBosses plugin = RandomMythicBosses.getInstance();

    public void initializeListeners() {
        PluginManager pluginManager = plugin.getServer().getPluginManager();

        pluginManager.registerEvents(new PlayerInteractListeners(), plugin);
    }
}
