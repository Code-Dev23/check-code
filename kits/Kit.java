package net.trickycreations.storyteamfight.kits;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class Kit {
    private List<ItemStack> items;
    private Map<String, ItemStack> armor;

    public void give(Player player) {
        player.getInventory().clear();
        items.forEach(item -> player.getInventory().addItem(item));
        armor.keySet().forEach(key -> {
            switch (key) {
                case "helmet" -> player.getInventory().setHelmet(armor.get("helmet"));
                case "chestplate" -> player.getInventory().setChestplate(armor.get("chestplate"));
                case "leggins" -> player.getInventory().setLeggings(armor.get("leggins"));
                case "boots" -> player.getInventory().setBoots(armor.get("boots"));
            }
        });
    }
}
