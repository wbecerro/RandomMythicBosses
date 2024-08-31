package wbe.randommythicbosses.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import wbe.randommythicbosses.RandomMythicBosses;

import java.util.ArrayList;

public class Compass extends ItemStack {

    public Compass() {
        super(Material.COMPASS);

        ItemMeta meta;
        if(hasItemMeta()) {
            meta = getItemMeta();
        } else {
            meta = Bukkit.getItemFactory().getItemMeta(Material.COMPASS);
        }

        meta.setDisplayName(RandomMythicBosses.config.compassName);

        ArrayList<String> lore = new ArrayList<>();
        for(String line : RandomMythicBosses.config.compassLore) {
            lore.add(line.replace("&", "§"));
        }

        meta.setLore(lore);

        meta.addEnchant(Enchantment.MENDING, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        NamespacedKey compassKey = new NamespacedKey(RandomMythicBosses.getInstance(), "BossCompass");
        meta.getPersistentDataContainer().set(compassKey, PersistentDataType.BOOLEAN, true);
        setItemMeta(meta);
    }
}
