package wbe.randommythicbosses.util;

import org.bukkit.Bukkit;
import wbe.randommythicbosses.RandomMythicBosses;

import java.time.Instant;

public class Scheduler {

    private static Utilities utilities = new Utilities();

    public static void startSchedulers() {
        startBossSpawnerScheduler();
    }

    private static void startBossSpawnerScheduler() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(RandomMythicBosses.getInstance(), new Runnable() {
            @Override
            public void run() {
                utilities.spawnBossEgg(false);
                RandomMythicBosses.nextBoss = Instant.now().getEpochSecond() + RandomMythicBosses.config.defaultTime;
            }
        }, 20L, 20L * RandomMythicBosses.config.defaultTime);
    }
}
