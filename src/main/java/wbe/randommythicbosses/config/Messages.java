package wbe.randommythicbosses.config;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Messages {

    private FileConfiguration config;

    public String spawnedBoss;
    public String spawnedBossCoords;
    public String bossNotFound;
    public String bossSpawned;
    public String onlyMainHand;
    public String bossFound;
    public String noBoss;
    public String noPermission;
    public String timeMessage;
    public String locationMessage;
    public String limitMessage;
    public String bossForced;
    public String bossDespawned;
    public String reload;
    public List<String> help = new ArrayList<>();

    public Messages(FileConfiguration config) {
        this.config = config;

        spawnedBoss = config.getString("Messages.spawnedBoss").replace("&", "§");
        spawnedBossCoords = config.getString("Messages.spawnedBossCoords").replace("&", "§");
        bossNotFound = config.getString("Messages.bossNotFound").replace("&", "§");
        bossSpawned = config.getString("Messages.bossSpawned").replace("&", "§");
        onlyMainHand = config.getString("Messages.onlyMainHand").replace("&", "§");
        bossFound = config.getString("Messages.bossFound").replace("&", "§");
        noBoss = config.getString("Messages.noBoss").replace("&", "§");
        noPermission = config.getString("Messages.noPermission").replace("&", "§");
        timeMessage = config.getString("Messages.timeMessage").replace("&", "§");
        locationMessage = config.getString("Messages.locationMessage").replace("&", "§");
        limitMessage = config.getString("Messages.limitMessage").replace("&", "§");
        bossForced = config.getString("Messages.bossForced").replace("&", "§");
        bossDespawned = config.getString("Messages.bossDespawned").replace("&", "§");
        reload = config.getString("Messages.reload").replace("&", "§");
        help = config.getStringList("Messages.help");
    }
}
