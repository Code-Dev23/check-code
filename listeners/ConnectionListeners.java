package net.trickycreations.storyteamfight.listeners;

import lombok.RequiredArgsConstructor;
import net.trickycreations.storyteamfight.TeamFight;
import net.trickycreations.storyteamfight.utilities.strings.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class ConnectionListeners implements Listener {
    private final TeamFight instance;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!instance.getArena().canJoin()) {
            player.kickPlayer(CC.translate("&cL'evento è già iniziato!"));
            return;
        }

        instance.getArena().addPlayer(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        instance.getArena().removePlayer(player);
        instance.getArena().checkWinCondition();
    }
}
