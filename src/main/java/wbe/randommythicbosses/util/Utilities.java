package wbe.randommythicbosses.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.metadata.FixedMetadataValue;
import wbe.randommythicbosses.RandomMythicBosses;

import java.time.Instant;
import java.util.Random;

public class Utilities {

    public void removeAllEggs() {
        for(String world : RandomMythicBosses.bossEggs.keySet()) {
            removeEgg(world);
        }
    }

    public boolean removeEgg(String world) {
        Location location = RandomMythicBosses.bossEggs.get(world);
        if(location == null) {
            return false;
        }
        Block block = Bukkit.getServer().getWorld(world).getBlockAt(location);
        block.getRelative(BlockFace.DOWN).setType(Material.AIR);
        block.setType(Material.AIR);
        RandomMythicBosses.bossEggs.remove(Bukkit.getServer().getWorld(world).getName());
        return true;
    }

    public Location getLocation(String world) {
        return RandomMythicBosses.bossEggs.get(world);
    }

    public void spawnBossEgg(boolean force) {
        Random random = new Random();
        int worldsSize = RandomMythicBosses.config.enabledWorlds.size();
        String worldName = RandomMythicBosses.config.enabledWorlds.get(random.nextInt(worldsSize));

        if(force) {
            removeEgg(worldName);
        }

        if(RandomMythicBosses.bossEggs.keySet().contains(worldName)) {
            return;
        }

        World world = Bukkit.getServer().getWorld(worldName);
        Location location = getValidCoordinates(world);
        if(location == null) {
            return;
        }

        int x = (int) location.getX();
        int y = (int) location.getY();
        int z = (int) location.getZ();
        Block lowerBlock = world.getBlockAt(x, y, z);
        lowerBlock.setType(RandomMythicBosses.config.eggUpperMaterial);
        lowerBlock.getRelative(BlockFace.DOWN).setType(RandomMythicBosses.config.eggLowerMaterial);
        lowerBlock.setMetadata("RandomMythicBossesBossEgg",
                new FixedMetadataValue(RandomMythicBosses.getInstance(), true));
        RandomMythicBosses.bossEggs.put(worldName, location);
        world.getPlayers().forEach(player -> player.sendMessage(RandomMythicBosses.messages.spawnedBoss));
        String locationMessage = "X: " + x + " Y: " + y + " Z: " + z;
        Bukkit.broadcast(RandomMythicBosses.messages.spawnedBossCoords.replace("%world%", worldName)
                .replace("%coords%", locationMessage), "randommythicbosses.admin");
    }

    public Location getValidCoordinates(World world) {
        Random random = new Random();
        int diameter = (int) world.getWorldBorder().getSize();
        int radius = diameter / 2;

        for(int i=0;i<RandomMythicBosses.config.maxSpawnRetries;i++) {
            int x = random.nextInt(diameter) - radius;
            int z = random.nextInt(diameter) - radius;
            int y = world.getHighestBlockYAt(x, z) + 2;

            if(y < world.getMaxHeight()) {
                return new Location(world, x, y, z);
            }
        }

        return null;
    }

    public String getRandomBoss() {
        Random random = new Random();
        int size = RandomMythicBosses.config.bosses.size();
        return RandomMythicBosses.config.bosses.get(random.nextInt(size));
    }

    public String getTime() {
        long present = Instant.now().getEpochSecond();
        long time = RandomMythicBosses.nextBoss - present;
        int hours = (int) (time / 3600);
        int minutes = (int) ((time - 3600 * hours) / 60);
        int seconds = (int) (time - hours * 3600 - minutes * 60);
        String timeLine = "";
        if(hours > 0) {
            timeLine += hours + "h ";
        }
        if(minutes > 0) {
            timeLine += minutes + "m ";
        }
        if(seconds > 0) {
            timeLine += seconds + "s";
        }
        return RandomMythicBosses.messages.timeMessage.replace("%time%", timeLine);
    }
}
