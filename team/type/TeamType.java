package net.trickycreations.storyteamfight.team.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TeamType {
    RED("&c", "Red"),
    BLUE("&9", "Blue"),

    ;

    private final String color;
    private final String name;
}