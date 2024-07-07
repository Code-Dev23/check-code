package net.trickycreations.storyteamfight.tasks;

import lombok.AllArgsConstructor;
import net.trickycreations.storyteamfight.arena.Arena;
import org.bukkit.scheduler.BukkitRunnable;

@AllArgsConstructor
public class GameStartingTask extends BukkitRunnable {
    private final Arena arena;
    private int i;

    @Override
    public void run() {
        if (i <= 0) {
            arena.start();
            this.cancel();
            return;
        }

        if (i % 10 == 0 || i <= 5) {
            arena.sendTitle("&6" + i + " secondi", "&fL'evento sta per iniziare!");
            arena.broadcast("&eIl gioco inizierà tra &l{time} &esecondi", "{time}", String.valueOf(i));
        }

        i--;
    }
}
