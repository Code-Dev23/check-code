package net.trickycreations.storyteamfight;

import co.aikar.commands.BukkitCommandManager;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.trickycreations.storyteamfight.arena.Arena;
import net.trickycreations.storyteamfight.commands.EventCommand;
import net.trickycreations.storyteamfight.listeners.ConnectionListeners;
import net.trickycreations.storyteamfight.listeners.GameListeners;
import net.trickycreations.storyteamfight.utilities.debug.Logs;
import net.trickycreations.storyteamfight.utilities.location.LocationUtil;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Getter
public final class TeamFight extends JavaPlugin {

    @Getter
    private static TeamFight instance;

    private BukkitAudiences adventure;
    private Arena arena;

    public BukkitAudiences adventure() {
        if (this.adventure == null)
            throw new IllegalStateException("Tried to access Adventure API when the plugin was disabled!");
        return this.adventure;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.adventure = BukkitAudiences.create(instance);

        try {

            Location spawn = LocationUtil.deserializeLocation(getConfig().getString("arena.spawn"));
            Location firstSpawn = LocationUtil.deserializeLocation(getConfig().getString("arena.teams_spawns.first"));
            Location secondSpawn = LocationUtil.deserializeLocation(getConfig().getString("arena.teams_spawns.second"));

            int maxPlayers = getConfig().getInt("arena.max_players");

            arena = new Arena(this, spawn, firstSpawn, secondSpawn, maxPlayers);
        } catch (Exception ex) {
            Logs.error("Location not found!");
            return;
        }

        loadCommandsAndListeners();
    }

    @Override
    public void onDisable() {

    }

    private void loadCommandsAndListeners() {
        BukkitCommandManager commandManager = new BukkitCommandManager(this);

        List.of(
                new EventCommand(this)
        ).forEach(commandManager::registerCommand);

        List.of(
                new ConnectionListeners(this),
                new GameListeners(this)
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }
}