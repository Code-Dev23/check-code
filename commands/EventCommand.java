package net.trickycreations.storyteamfight.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import lombok.RequiredArgsConstructor;
import net.trickycreations.storyteamfight.TeamFight;
import net.trickycreations.storyteamfight.utilities.location.LocationUtil;
import net.trickycreations.storyteamfight.utilities.strings.CC;
import org.bukkit.entity.Player;

@CommandAlias("event")
@CommandPermission("storyteamfight.admin")
@RequiredArgsConstructor
public class EventCommand extends BaseCommand {
    private final TeamFight instance;

    @Default
    public void command(Player player) {
        CC.send(player, "&cUse: /event <setspawn|setteamspawn|setmax> <value>");
    }

    @Subcommand("setspawn")
    public void subSetSpawn(Player player) {
        instance.getConfig().set("arena.spawn", LocationUtil.serializeLocation((player.getLocation())));
        instance.saveConfig();
        instance.getArena().setSpawn(player.getLocation());

        CC.send(player, "&aSpawn set!");
    }

    @Subcommand("setmax")
    public void subSetMax(Player player, String[] args) {
        if (args.length == 0) {
            CC.send(player, "&cInserisci un numero per il massimo di giocatori.");
            return;
        }
        int value;
        try {
            value = Integer.parseInt(args[0]);
        } catch (NumberFormatException ex) {
            CC.send(player, "&cInserisci un numero valido.");
            return;
        }
        instance.getConfig().set("arena.max_players", value);
        instance.saveConfig();
        instance.getArena().setMaxPlayers(value);

        CC.send(player, "&aSpawn set!");
    }

    @Subcommand("setteamspawn first")
    public void onSubSetTeamSpawnFirst(Player player) {
        instance.getConfig().set("arena.teams_spawns.first", LocationUtil.serializeLocation(player.getLocation()));
        instance.saveConfig();
        instance.getArena().setFirstSpawn(player.getLocation());

        CC.send(player, "&aFirst team spawn set!");
    }

    @Subcommand("setteamspawn second")
    public void onSubSetTeamSpawnSecond(Player player) {
        instance.getConfig().set("arena.teams_spawns.second", LocationUtil.serializeLocation(player.getLocation()));
        instance.saveConfig();
        instance.getArena().setSecondSpawn(player.getLocation());

        CC.send(player, "&aFirst team spawn set!");
    }

    @Subcommand("start")
    public void onSubStart(Player player) {
        instance.getArena().countdown();

        CC.send(player, "&eHai avviato l'evento!");
        instance.getArena().broadcast("&3Evento avviato!");
    }

    @Subcommand("end")
    public void onSubEnd(Player player) {
        instance.getArena().end();
    }
}