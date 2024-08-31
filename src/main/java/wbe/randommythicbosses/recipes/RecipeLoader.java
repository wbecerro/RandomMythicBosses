package wbe.randommythicbosses.recipes;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import wbe.randommythicbosses.RandomMythicBosses;
import wbe.randommythicbosses.items.Compass;

public class RecipeLoader {

    private RandomMythicBosses plugin = RandomMythicBosses.getInstance();

    public List<NamespacedKey> keys = new ArrayList<>();

    public void loadRecipes() {
        loadCompassRecipe();
    }

    public void unloadRecipes() {
        for(NamespacedKey key : keys) {
            plugin.getServer().removeRecipe(key);
        }
    }

    private void loadCompassRecipe() {
        NamespacedKey key = new NamespacedKey(plugin, "BossCompass");
        ShapedRecipe compassRecipe = new ShapedRecipe(key, new Compass());
        compassRecipe.shape(new String[] { " D ", "GCI", " E " });
        compassRecipe.setIngredient('C', Material.COMPASS);
        compassRecipe.setIngredient('D', Material.DIAMOND);
        compassRecipe.setIngredient('E', Material.EMERALD);
        compassRecipe.setIngredient('G', Material.GOLD_INGOT);
        compassRecipe.setIngredient('I', Material.IRON_INGOT);
        plugin.getServer().addRecipe(compassRecipe);
        keys.add(key);
    }
}
