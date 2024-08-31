package wbe.randommythicbosses.config;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Config {

    private FileConfiguration config;

    public int defaultTime;
    public List<String> enabledWorlds;
    public List<String> bosses;
    public Material eggUpperMaterial;
    public Material eggLowerMaterial;
    public int maxSpawnRetries;

    public String compassName;
    public List<String> compassLore;

    public Config(FileConfiguration config) {
        this.config = config;

        defaultTime = config.getInt("Config.defaultTime");
        enabledWorlds = config.getStringList("Config.enabledWorlds");
        bosses = config.getStringList("Config.bosses");
        eggUpperMaterial = Material.valueOf(config.getString("Config.bossEgg.upperBlock"));
        eggLowerMaterial = Material.valueOf(config.getString("Config.bossEgg.lowerBlock"));
        maxSpawnRetries = config.getInt("Config.maxSpawnRetries");
        compassName = config.getString("Items.compass.name").replace("&", "§");
        compassLore = config.getStringList("Items.compass.lore");
    }
}
