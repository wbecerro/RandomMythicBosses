package wbe.randommythicbosses;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.MobExecutor;
import java.util.Collection;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class PlayerListener implements Listener {
    private RandomMythicBosses plugin;

    private FileConfiguration config;

    private boolean compassB;

    public PlayerListener(RandomMythicBosses plugin) {
        this.plugin = plugin;
        this.config = this.plugin.getConfig();
        this.compassB = false;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Random r = new Random();
        if (e.hasBlock())
            if (e.getClickedBlock().getType() == Material.END_PORTAL_FRAME) {
                Location bossBlockLocation = e.getClickedBlock().getLocation();
                if (isBossSpawn(e.getClickedBlock())) {
                    MobExecutor mm = MythicBukkit.inst().getMobManager();
                    Collection<String> mobs = mm.getMobNames();
                    String boss = this.config.getStringList("bosses").get(r.nextInt(this.config.getStringList("bosses").size()));
                    if (mobs.contains(boss)) {
                        removeBossSpawn(e.getClickedBlock().getLocation(), p.getWorld().getName());
                        mm.spawnMob(boss, bossBlockLocation);
                        for (Player player : Bukkit.getServer().getWorld(p.getWorld().getName()).getPlayers())
                            player.sendMessage(this.config.getString("bossSpawned").replace("&", "§").replace("%player%", p.getName()));
                    } else {
                        p.sendMessage(this.config.getString("bossNotFound").replace("&", "§").replace("%boss%", boss));
                    }
                }
            } else if (p.getInventory().getItemInOffHand().getType() == Material.COMPASS) {
                ItemStack item = p.getInventory().getItemInOffHand();
                if (item.getItemMeta().getDisplayName().equals(this.config.getString("compass.name").replace("&", "§")) &&
                                item.getItemMeta().hasLore())
                        p.sendMessage(this.config.getString("onlyMainHand").replace("&", "§").replace("%compass_name%", this.config.getString("compass.name").replace("&", "§")));
            } else if (p.getInventory().getItemInMainHand().getType() == Material.COMPASS) {
                ItemStack item = p.getInventory().getItemInMainHand();
                if (item.getItemMeta().getDisplayName().equals(this.config.getString("compass.name").replace("&", "§")) &&
                        item.getItemMeta().hasLore() && (
                                e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK))
                if (this.plugin.bossEggs.containsKey(p.getWorld().getName())) {
                    p.setCompassTarget(this.plugin.bossEggs.get(p.getWorld().getName()));
                    p.sendMessage(this.config.getString("bossFound").replace("&", "§"));
                } else {
                    p.setCompassTarget(p.getWorld().getSpawnLocation());
                    p.sendMessage(this.config.getString("noBoss").replace("&", "§"));
                }
            }
    }

    public boolean isBossSpawn(Block b) {
        boolean bossSpawn = false;
        if (b.getType() == Material.END_PORTAL_FRAME)
            for (MetadataValue mdv : b.getMetadata("isBossSpawn")) {
                if (mdv.getOwningPlugin().equals(this.plugin) &&
                        mdv.asBoolean())
                    bossSpawn = true;
            }
        return bossSpawn;
    }

    public void removeBossSpawn(Location b, String world) {
        b.getBlock().setType(Material.AIR);
        b.getBlock().removeMetadata("isBossSpawn", (Plugin)this.plugin);
        b.getBlock().getRelative(BlockFace.DOWN).setType(Material.AIR);
        this.plugin.bossEggs.remove(world);
    }
}
