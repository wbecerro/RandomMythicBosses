package wbe.randommythicbosses.listeners;

import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.MobExecutor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import wbe.randommythicbosses.RandomMythicBosses;
import wbe.randommythicbosses.util.Utilities;

import java.util.Collection;

public class PlayerInteractListeners implements Listener {

    private RandomMythicBosses plugin = RandomMythicBosses.getInstance();

    private Utilities utilities = new Utilities();

    @EventHandler(priority = EventPriority.NORMAL)
    public void useCompassOnInteract(PlayerInteractEvent event) {
        if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        ItemStack item = event.getItem();
        if(item == null) {
            return;
        }

        if(!item.getType().equals(Material.COMPASS)) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if(meta == null) {
            return;
        }

        NamespacedKey compassKey = new NamespacedKey(plugin, "BossCompass");
        if(!meta.getPersistentDataContainer().has(compassKey)) {
            return;
        }

        Player player = event.getPlayer();
        String world = player.getWorld().getName();
        if(RandomMythicBosses.bossEggs.containsKey(world)) {
            player.setCompassTarget(RandomMythicBosses.bossEggs.get(world));
            player.sendMessage(RandomMythicBosses.messages.bossFound);
        } else {
            player.setCompassTarget(player.getWorld().getSpawnLocation());
            player.sendMessage(RandomMythicBosses.messages.noBoss);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void awakenBossOnInteract(PlayerInteractEvent event) {
        if(!event.getAction().equals(Action.LEFT_CLICK_BLOCK) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        Block block = event.getClickedBlock();
        if(!block.hasMetadata("RandomMythicBossesBossEgg")) {
           return;
        }

        if(!utilities.removeEgg(block.getWorld().getName())) {
            return;
        }

        MobExecutor mobExecutor = MythicBukkit.inst().getMobManager();
        Collection<String> allMobs = mobExecutor.getMobNames();
        String boss = utilities.getRandomBoss();
        if(!allMobs.contains(boss)) {
            Bukkit.broadcast(RandomMythicBosses.messages.bossNotFound.replace("%boss%", boss),
                    "randommythicbosses.admin");
            return;
        }

        MythicMob bossMob = mobExecutor.getMythicMob(boss).get();
        mobExecutor.spawnMob(boss, block.getLocation());
        block.getWorld().getPlayers().forEach(player -> player.sendMessage(RandomMythicBosses.messages.bossSpawned
                .replace("%player%", event.getPlayer().getName())
                .replace("%boss%", bossMob.getDisplayName().get())));
    }
}
