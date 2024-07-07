package net.trickycreations.storyteamfight.arena;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import net.trickycreations.storyteamfight.TeamFight;
import net.trickycreations.storyteamfight.arena.state.GameState;
import net.trickycreations.storyteamfight.tasks.GameStartingTask;
import net.trickycreations.storyteamfight.team.TeamData;
import net.trickycreations.storyteamfight.team.type.TeamType;
import net.trickycreations.storyteamfight.utilities.strings.CC;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Set;

@Getter
public class Arena {

    private final TeamFight instance;
    private final Set<Player> players, spectators;
    private final TeamData redTeam;
    private final TeamData blueTeam;
    @Setter
    private GameState state;
    @Setter
    private Location firstSpawn, secondSpawn, spawn;
    @Setter
    private int maxPlayers;

    public Arena(TeamFight instance, Location spawn, Location firstSpawn, Location secondSpawn, int maxPlayers) {
        this.instance = instance;
        this.state = GameState.ACTIVE;

        this.players = Sets.newConcurrentHashSet();
        this.spectators = Sets.newConcurrentHashSet();

        redTeam = new TeamData(TeamType.RED);
        blueTeam = new TeamData(TeamType.BLUE);

        this.spawn = spawn;
        this.firstSpawn = firstSpawn;
        this.secondSpawn = secondSpawn;

        this.maxPlayers = maxPlayers;
    }

    public void addPlayer(Player player) {
        resetPlayer(player);

        players.add(player);

        if (redTeam.getPlayers().size() <= blueTeam.getPlayers().size()) {
            redTeam.getPlayers().add(player);
        } else {
            blueTeam.getPlayers().add(player);
        }

        if (spawn == null) return;
        player.teleport(spawn);
    }

    public void removePlayer(Player player) {
        removeFromTeam(player);
        players.remove(player);
        spectators.remove(player);
    }

    public void removeFromTeam(Player player) {
        redTeam.getPlayers().remove(player);
        blueTeam.getPlayers().remove(player);
    }

    public void countdown() {
        setState(GameState.COUNTDOWN);
        new GameStartingTask(this, 30).runTaskTimer(instance, 0L, 20L);
    }

    public void start() {
        if (spawn == null || firstSpawn == null || secondSpawn == null) return;
        setState(GameState.LIVE);

        redTeam.getPlayers().forEach(p -> {
            resetPlayer(p);
            p.setGameMode(GameMode.SURVIVAL);
            p.teleport(firstSpawn);
        });
        blueTeam.getPlayers().forEach(p -> {
            resetPlayer(p);
            p.setGameMode(GameMode.SURVIVAL);
            p.teleport(secondSpawn);
        });
    }

    public void end() {
        Bukkit.getScheduler().runTaskLater(instance, () -> {
            players.clear();
            spectators.clear();
            Bukkit.getOnlinePlayers().forEach(p -> p.kickPlayer(CC.translate(p, "&cEvento terminato!")));
        }, 5 * 20);
    }

    public void setSpectator(Player player) {
        removeFromTeam(player);

        players.forEach(p -> p.hidePlayer(player));
        spectators.forEach(player::showPlayer);

        spectators.add(player);
        resetPlayer(player);

        player.teleport(spawn);
        player.setAllowFlight(true);
    }

    public boolean isSpectator(Player player) {
        return spectators.contains(player);
    }

    public TeamData getTeam(Player player) {
        return (redTeam.getPlayers().contains(player)) ? redTeam : blueTeam;
    }

    public void resetPlayer(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        player.setAllowFlight(false);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setExp(0);
        player.setGameMode(GameMode.ADVENTURE);
    }

    public boolean canJoin() {
        return state != GameState.LIVE && players.size() <= maxPlayers;
    }

    public void sendTitle(String title, String subTitle) {
        players.forEach(p -> CC.sendTitle(p, title, subTitle));
        spectators.forEach(p -> CC.sendTitle(p, title, subTitle));
    }

    public void sendActionBar(String text, Object... params) {
        players.forEach(p -> CC.sendActionBar(p, text, params));
        spectators.forEach(p -> CC.sendActionBar(p, text, params));
    }

    public void broadcast(String text, Object... params) {
        players.forEach(p -> CC.send(p, text, params));
        spectators.forEach(p -> CC.send(p, text, params));
    }

    public void checkWinCondition() {
        if (state != GameState.LIVE)
            return;
        if (redTeam.getPlayers().isEmpty() || blueTeam.getPlayers().isEmpty()) {
            TeamType winner = redTeam.getPlayers().isEmpty() ? TeamType.BLUE : TeamType.RED;
            sendTitle("&bEvento terminato!", "&fHa vinto il team " + winner.getColor() + winner.getName() + "&f!");
            end();
        }
    }
}