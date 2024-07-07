package net.trickycreations.storyteamfight.utilities.strings;

import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import net.trickycreations.storyteamfight.TeamFight;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.time.Duration;

@UtilityClass
public class CC {
    public String replace(String message, Object... params) {
        if (params.length % 2 != 0)
            throw new IllegalArgumentException("Parameters should be in key-value pairs.");

        for (int i = 0; i < params.length; i += 2)
            message = message.replace(params[i].toString(), params[i + 1].toString());

        return message;
    }

    public Component component(String text, Object... params) {
        return Component.text(ChatColor.translateAlternateColorCodes('&', replace(text, params)));
    }

    public Component component(Player player, String message, Object... params) {
        return Component.text(translate(player, message, params));
    }

    public String translate(String message, Object... params) {
        return LegacyComponentSerializer.legacyAmpersand().serialize(component(message, params));
    }

    public String translate(Player player, String message, Object... params) {
        return PlaceholderAPI.setPlaceholders(player, translate(message, params));
    }

    public void send(Player player, String message, Object... params) {
        player.sendMessage(translate(player, message, params));
    }

    public void sendActionBar(Player player, String text, Object... params) {
        Audience target = TeamFight.getInstance().adventure().player(player);
        target.sendActionBar(component(player, text, params));
    }

    public void sendTitle(Player player, String title, String subTitle) {
        Audience target = TeamFight.getInstance().adventure().player(player);
        target.showTitle(Title.title(
                component(player, title),
                component(player, subTitle),
                Title.Times.times(
                        Duration.ofSeconds(1),
                        Duration.ofSeconds(2),
                        Duration.ofSeconds(1)
                )
        ));
    }
}