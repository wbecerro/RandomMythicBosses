package wbe.randommythicbosses;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class RandomMythicBosses extends JavaPlugin {
    private FileConfiguration config = getConfig();

    public RecipeManager recipeManager;

    private final PlayerListener playerListnener = new PlayerListener(this);

    Map<String, Location> bossEggs;

    public void onEnable() {
        this.bossEggs = new HashMap<>();
        saveDefaultConfig();
        loadManagers();
        getLogger().info("RandomMythicBoss enabled correctly");
        getServer().getPluginManager().registerEvents(this.playerListnener, (Plugin)this);
        int defaultTimeSpawn = this.config.getInt("defaultTime");
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)this, new Runnable() {
            public void run() {
                Random r = new Random();
                int rWorld = r.nextInt(RandomMythicBosses.this.config.getStringList("enabledWorlds").size());
                String world = RandomMythicBosses.this.config.getStringList("enabledWorlds").get(rWorld);
                if (!RandomMythicBosses.this.bossEggs.containsKey(world)) {
                    int xpn = r.nextInt(2);
                    int zpn = r.nextInt(2);
                    int x = 0;
                    int z = 0;
                    for (int i = 0; i < 2; i++) {
                        int coords = r.nextInt(RandomMythicBosses.this.config.getInt("radiusSpawn"));
                        if (x == 0) {
                            if (xpn == 0) {
                                x = coords;
                            } else {
                                x = coords * -1;
                            }
                        } else if (zpn == 0) {
                            z = coords;
                        } else {
                            z = coords * -1;
                        }
                    }
                    int y = 62;
                    while (Bukkit.getServer().getWorld(world).getBlockAt(x, y, z).getType() != Material.AIR)
                        y++;
                    Block block = Bukkit.getServer().getWorld(world).getBlockAt(x, y + 1, z);
                    RandomMythicBosses.this.randomBossGenerate(world, block);
                    for (Player p : Bukkit.getServer().getWorld(world).getPlayers())
                        p.sendMessage(RandomMythicBosses.this.config.getString("spawnedBoss").replace("&", "§"));
                                String location = "X: " + x + " Y: " + y + " Z: " + z;
                    Bukkit.broadcast(RandomMythicBosses.this.config.getString("spawnedBossCoords").replace("&", "§").replace("%world%", world).replace("%coords%", location), "mrb.admin");
                }
            }
        }, 20L, 20L * defaultTimeSpawn);
    }

    public void onDisable() {
        getLogger().info("RandomMythicBoss disabled correctly");
    }

    public void randomBossGenerate(String world, Block b) {
        if (!this.bossEggs.containsKey(world)) {
            b.getRelative(BlockFace.DOWN).setType(Material.END_ROD);
            b.setType(Material.END_PORTAL_FRAME);
            b.setMetadata("isBossSpawn", (MetadataValue)new FixedMetadataValue((Plugin)this, Boolean.valueOf(true)));
            this.bossEggs.put(world, b.getLocation());
        }
    }

    private void loadManagers() {
        this.recipeManager = new RecipeManager(this);
    }
}

