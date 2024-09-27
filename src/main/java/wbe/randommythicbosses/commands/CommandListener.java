package wbe.randommythicbosses.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wbe.randommythicbosses.RandomMythicBosses;
import wbe.randommythicbosses.util.Utilities;

public class CommandListener implements CommandExecutor {

    private RandomMythicBosses plugin = RandomMythicBosses.getInstance();

    private Utilities utilities = new Utilities();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("RandomMythicBosses")) {
            Player player = null;
            if(sender instanceof Player) {
                player = (Player) sender;
            }

            if(args.length == 0 || args[0].equalsIgnoreCase("help")) {
                if(!sender.hasPermission("randommythicbosses.command.help")) {
                    sender.sendMessage(RandomMythicBosses.messages.noPermission);
                    return false;
                }
                for(String x : RandomMythicBosses.messages.help) {
                    sender.sendMessage(x.replace("&", "§"));
                }
            } else if(args[0].equalsIgnoreCase("time")) {
                if(!sender.hasPermission("randommythicbosses.command.time")) {
                    sender.sendMessage(RandomMythicBosses.messages.noPermission);
                    return false;
                }
                sender.sendMessage(RandomMythicBosses.messages.timeMessage.replace("%time%", utilities.getTime()));
            } else if(args[0].equalsIgnoreCase("location")) {
                if(!sender.hasPermission("randommythicbosses.command.location")) {
                    sender.sendMessage(RandomMythicBosses.messages.noPermission);
                    return false;
                }
                for(String s : RandomMythicBosses.bossEggs.keySet()) {
                    Location location = utilities.getLocation(player.getWorld().getName());
                    sender.sendMessage(RandomMythicBosses.messages.locationMessage
                            .replace("%x%", String.valueOf(location.getX()))
                            .replace("%y%", String.valueOf(location.getY()))
                            .replace("%z%", String.valueOf(location.getZ()))
                            .replace("%world%", s));
                }
            } else if(args[0].equalsIgnoreCase("limit")) {
                if(!sender.hasPermission("randommythicbosses.command.limit")) {
                    sender.sendMessage(RandomMythicBosses.messages.noPermission);
                    return false;
                }
                Double mensaje = player.getWorld().getWorldBorder().getSize() / 2;
                player.sendMessage(RandomMythicBosses.messages.limitMessage.replace("%limit%", String.valueOf(mensaje)));
            } else if(args[0].equalsIgnoreCase("force")) {
                if(!sender.hasPermission("randommythicbosses.command.force")) {
                    sender.sendMessage(RandomMythicBosses.messages.noPermission);
                    return false;
                }
                sender.sendMessage(RandomMythicBosses.messages.bossForced);
                utilities.spawnBossEgg(true);
            } else if(args[0].equalsIgnoreCase("despawn")) {
                if(!sender.hasPermission("randommythicbosses.command.despawn")) {
                    sender.sendMessage(RandomMythicBosses.messages.noPermission);
                    return false;
                }
                sender.sendMessage(RandomMythicBosses.messages.bossDespawned);
                utilities.removeAllEggs();
            } else if(args[0].equalsIgnoreCase("reload")) {
                if(!sender.hasPermission("randommythicbosses.command.reload")) {
                    sender.sendMessage(RandomMythicBosses.messages.noPermission);
                    return false;
                }
                sender.sendMessage(RandomMythicBosses.messages.reload);
                plugin.reloadConfiguration();
            }
        }
        return true;
    }
}