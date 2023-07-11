package wbe.randommythicbosses;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class RecipeManager {
    private final RandomMythicBosses plugin;

    private ItemStack compass;

    private FileConfiguration config;

    public RecipeManager(RandomMythicBosses plugin) {
        this.plugin = plugin;
        this.config = this.plugin.getConfig();
        this.compass = new ItemStack(Material.COMPASS);
        ItemMeta meta = this.compass.getItemMeta();
        meta.setDisplayName(this.config.getString("compass.name").replace("&", "§"));
                List<String> lore = new ArrayList<>();
        for (String x : this.config.getStringList("compass.lore"))
            lore.add(x.replace("&", "§"));
                    meta.setLore(lore);
        meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
        meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
        this.compass.setItemMeta(meta);
        this.plugin.getServer().resetRecipes();
        NamespacedKey key = new NamespacedKey((Plugin) this.plugin, "bukkit");
        ShapedRecipe compassRecipe = new ShapedRecipe(key, this.compass);
        compassRecipe.shape(new String[] { " D ", "GCI", " E " });
        compassRecipe.setIngredient('C', Material.COMPASS);
        compassRecipe.setIngredient('D', Material.DIAMOND);
        compassRecipe.setIngredient('E', Material.EMERALD);
        compassRecipe.setIngredient('G', Material.GOLD_INGOT);
        compassRecipe.setIngredient('I', Material.IRON_INGOT);
        this.plugin.getServer().addRecipe((Recipe)compassRecipe);
    }
}
