package net.trickycreations.storyteamfight.team;

import com.google.common.collect.Sets;
import lombok.Getter;
import net.trickycreations.storyteamfight.team.type.TeamType;
import org.bukkit.entity.Player;

import java.util.Set;

@Getter
public class TeamData {
    private final TeamType type;
    private final Set<Player> players;

    public TeamData(TeamType type) {
        this.type = type;
        this.players = Sets.newConcurrentHashSet();
    }
}