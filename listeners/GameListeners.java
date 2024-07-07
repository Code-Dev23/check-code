package net.trickycreations.storyteamfight.listeners;

import lombok.RequiredArgsConstructor;
import net.trickycreations.storyteamfight.TeamFight;
import net.trickycreations.storyteamfight.arena.Arena;
import net.trickycreations.storyteamfight.arena.state.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

@RequiredArgsConstructor
public class GameListeners implements Listener {
    private final TeamFight instance;

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player player) || !(event.getDamager() instanceof Player damager))
            return;

        Arena arena = instance.getArena();

        if (arena.getState() != GameState.LIVE)
            event.setCancelled(true);

        if (arena.isSpectator(player)) {
            event.setCancelled(true);
            return;
        }

        if (arena.getTeam(player).getType() == arena.getTeam(damager).getType())
            event.setCancelled(true);

        if (player.getHealth() - event.getFinalDamage() <= 0)
            handleDie(player, damager);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player))
            return;

        Arena arena = instance.getArena();

        if (arena.getState() != GameState.LIVE)
            event.setCancelled(true);

        if (arena.isSpectator(player)) {
            event.setCancelled(true);
            return;
        }

        if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            player.teleport(arena.getSpawn());
            return;
        }

        if (player.getHealth() - event.getFinalDamage() <= 0)
            handleDie(player, null);
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent event) {
        if (instance.getArena().getState() != GameState.LIVE) {
            event.setCancelled(true);
            event.setFoodLevel(20);
        }
    }

    public void handleDie(Player player, Player killer) {
        Arena arena = instance.getArena();

        player.spigot().respawn();
        instance.getArena().setSpectator(player);

        if (killer != null)
            arena.broadcast("&e{player} &fe' stato ucciso da &c{killer}&f!");
        else
            arena.broadcast("&e{player} &fe' morto!");

        arena.checkWinCondition();
    }
}