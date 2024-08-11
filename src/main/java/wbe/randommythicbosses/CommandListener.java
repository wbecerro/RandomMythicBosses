package wbe.randommythicbosses;

import io.lumine.mythic.bukkit.utils.lib.jooq.impl.QOM;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.time.Instant;

public class CommandListener implements CommandExecutor {

    private RandomMythicBosses plugin;

    private FileConfiguration config;

    public CommandListener(RandomMythicBosses plugin) {
        this.plugin = plugin;
        this.config = this.plugin.getConfig();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("RandomMythicBosses")) {
            Player p = null;
            if(sender instanceof Player) {
                p = (Player) sender;
            }

            if(args.length == 0 || args[0].equalsIgnoreCase("help")) {
                if(!sender.hasPermission("randommythicbosses.command.help")) {
                    sender.sendMessage(config.getString("noPermission").replace("&", "§"));
                    return false;
                }
                for(String x : config.getStringList("help")) {
                    sender.sendMessage(x.replace("&", "§"));
                }
            } else if(args[0].equalsIgnoreCase("time")) {
                if(!sender.hasPermission("randommythicbosses.command.time")) {
                    sender.sendMessage(config.getString("noPermission").replace("&", "§"));
                    return false;
                }
                sender.sendMessage(config.getString("timeMessage").replace("%time%", getSpawnTime()).replace("&", "§"));
            } else if(args[0].equalsIgnoreCase("location")) {
                if(!sender.hasPermission("randommythicbosses.command.location")) {
                    sender.sendMessage(config.getString("noPermission").replace("&", "§"));
                    return false;
                }
                for(String s : plugin.bossEggs.keySet()) {
                    sender.sendMessage(config.getString("locationMessage").replace("%x%", String.valueOf(plugin.getX(s)))
                            .replace("%y%", String.valueOf(plugin.getY(s))).replace("%z%", String.valueOf(plugin.getZ(s)))
                            .replace("%world%", s).replace("&", "§"));
                }
            } else if(args[0].equalsIgnoreCase("limit")) {
                if(!(sender instanceof Player)) {
                    sender.sendMessage(config.getString("playerOnly").replace("&", "§"));
                    return false;
                }
                if(!sender.hasPermission("randommythicbosses.command.limit")) {
                    sender.sendMessage(config.getString("noPermission").replace("&", "§"));
                    return false;
                }
                Double mensaje = p.getWorld().getWorldBorder().getSize() / 2;
                p.sendMessage(config.getString("limitMessage").replace("&", "§").replace("%limit%", String.valueOf(mensaje)));
            } else if(args[0].equalsIgnoreCase("force")) {
                if(!sender.hasPermission("randommythicbosses.command.force")) {
                    sender.sendMessage(config.getString("noPermission").replace("&", "§"));
                    return false;
                }
                sender.sendMessage(config.getString("bossForced").replace("&", "§"));
                plugin.spawnBoss(true);
            } else if(args[0].equalsIgnoreCase("despawn")) {
                if(!sender.hasPermission("randommythicbosses.command.force")) {
                    sender.sendMessage(config.getString("noPermission").replace("&", "§"));
                    return false;
                }
                sender.sendMessage(config.getString("bossDespawned").replace("&", "§"));
                plugin.despawnBoss();
            }
        }
        return true;
    }

    public String getSpawnTime() {
        long past = plugin.getTime();
        long present = Instant.now().getEpochSecond();
        long time = past - present;

        long minutes = time / 60;
        if(minutes < 0) {
            return (int) time + "s";
        } else {
            double seconds = time - Math.floor(minutes) * 60;
            return (int) Math.floor(minutes) + "m " + (int) seconds + "s";
        }
    }
}
