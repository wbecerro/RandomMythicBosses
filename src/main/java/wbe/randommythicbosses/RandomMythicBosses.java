package wbe.randommythicbosses;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.*;
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

    private CommandListener commandListener = new CommandListener(this);

    public RecipeManager recipeManager;

    private final PlayerListener playerListener = new PlayerListener(this);

    Map<String, Location> bossEggs;

    private long time;

    public void onEnable() {
        this.bossEggs = new HashMap<>();
        saveDefaultConfig();
        loadManagers();
        getLogger().info("RandomMythicBosses enabled correctly");
        getCommand("RandomMythicBosses").setExecutor(this.commandListener);
        getServer().getPluginManager().registerEvents(this.playerListener, (Plugin)this);
        int defaultTimeSpawn = this.config.getInt("defaultTime");
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)this, new Runnable() {
            public void run() {
                spawnBoss(false);
            }
        }, 20L, 20L * defaultTimeSpawn);
    }

    public void onDisable() {
        getLogger().info("RandomMythicBosses disabled correctly");
        despawnBoss();
    }

    public void randomBossGenerate(String world, Block b) {
        if (!this.bossEggs.containsKey(world)) {
            b.getRelative(BlockFace.DOWN).setType(Material.END_ROD);
            b.setType(Material.END_PORTAL_FRAME);
            b.setMetadata("isBossSpawn", (MetadataValue)new FixedMetadataValue((Plugin)this, Boolean.valueOf(true)));
            this.bossEggs.put(world, b.getLocation());
        }
    }

    public void spawnBoss(boolean force) {
        Random r = new Random();
        int rWorld = r.nextInt(RandomMythicBosses.this.config.getStringList("enabledWorlds").size());
        String world = RandomMythicBosses.this.config.getStringList("enabledWorlds").get(rWorld);
        int radius = (int) Bukkit.getServer().getWorld(world).getWorldBorder().getSize() / 2;
        if (!RandomMythicBosses.this.bossEggs.containsKey(world)) {
            int xpn = r.nextInt(2);
            if(xpn == 0)
                xpn = -1;
            int zpn = r.nextInt(2);
            if(zpn == 0)
                zpn = -1;
            int x = r.nextInt(radius) * xpn;
            int z = r.nextInt(radius) * zpn;
            int y = 62;
            while (Bukkit.getServer().getWorld(world).getBlockAt(x, y, z).getType() != Material.AIR
                    && Bukkit.getServer().getWorld(world).getBlockAt(x, y+1, z).getType() != Material.AIR)
                y++;
            Block block = Bukkit.getServer().getWorld(world).getBlockAt(x, y + 1, z);
            randomBossGenerate(world, block);
            for (Player p : Bukkit.getServer().getWorld(world).getPlayers())
                p.sendMessage(RandomMythicBosses.this.config.getString("spawnedBoss").replace("&", "§"));
            String location = "X: " + x + " Y: " + y + " Z: " + z;
            Bukkit.broadcast(RandomMythicBosses.this.config.getString("spawnedBossCoords").replace("&", "§").replace("%world%", world).replace("%coords%", location), "mrb.admin");
        }
        if(!force) {
            time = Instant.now().getEpochSecond() + config.getInt("defaultTime");
        }
    }

    public void despawnBoss() {
        for(String key : bossEggs.keySet()) {
            Block block = Bukkit.getServer().getWorld(key).getBlockAt(bossEggs.get(key));
            block.getRelative(BlockFace.DOWN).setType(Material.AIR);
            block.setType(Material.AIR);
            this.bossEggs.remove(Bukkit.getServer().getWorld(key).getName());
        }
    }

    public long getTime() {
        return time;
    }

    public int getX(String world) {
        return (int) this.bossEggs.get(world).getX();
    }

    public int getY(String world) {
        return (int) this.bossEggs.get(world).getY();
    }

    public int getZ(String world) {
        return (int) this.bossEggs.get(world).getZ();
    }

    private void loadManagers() {
        this.recipeManager = new RecipeManager(this);
    }

    public NamespacedKey getKey() {
        return recipeManager.getKey();
    }
}

