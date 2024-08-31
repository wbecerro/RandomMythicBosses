package wbe.randommythicbosses.listeners;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import wbe.randommythicbosses.RandomMythicBosses;

public class PlayerJoinListeners implements Listener {

    @EventHandler
    public void discoverRecipesOnJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for(NamespacedKey key : RandomMythicBosses.recipeLoader.keys) {
            player.discoverRecipe(key);
        }
    }
}
