package net.trickycreations.storyteamfight.utilities.cooldown;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Cooldown {
    private final Map<UUID, Long> cooldownMap = new HashMap<>();

    public void add(UUID uuid, int seconds) {
        cooldownMap.put(uuid, Instant.now().getEpochSecond() + seconds);
    }

    public void remove(UUID uuid) {
        cooldownMap.remove(uuid);
    }

    public boolean isIn(UUID uuid) {
        long now = Instant.now().getEpochSecond();
        boolean isIn = cooldownMap.getOrDefault(uuid, 0L) > now;
        if (!isIn)
            remove(uuid);
        return isIn;
    }

    public int getTime(UUID uuid) {
        long now = Instant.now().getEpochSecond();
        return (int) Math.max(0, cooldownMap.getOrDefault(uuid, 0L) - now);
    }
}