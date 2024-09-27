package wbe.randommythicbosses.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import wbe.randommythicbosses.RandomMythicBosses;
import wbe.randommythicbosses.util.Utilities;

public class PapiExtension extends PlaceholderExpansion {

    private RandomMythicBosses plugin = RandomMythicBosses.getInstance();

    private Utilities utilities = new Utilities();

    @Override
    public String getIdentifier() {
        return "RandomMythicBosses";
    }

    @Override
    public String getAuthor() {
        return "wbe";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("cooldown")) {
            return utilities.getTime();
        }

        return null;
    }
}
