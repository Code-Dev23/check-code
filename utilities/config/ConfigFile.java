package net.trickycreations.storyteamfight.utilities.config;

import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Getter
public class ConfigFile extends YamlConfiguration {
    private final File file;

    public ConfigFile(JavaPlugin plugin, String name) {
        file = new File(plugin.getDataFolder(), name);

        if (!file.exists())
            plugin.saveResource(name, false);

        try {
            load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    public int getInt(String path) {
        return getInt(path, 0);
    }

    @NotNull
    public double getDouble(String path) {
        return getDouble(path, 0.0D);
    }

    @NotNull
    public boolean getBoolean(String path) {
        return getBoolean(path, false);
    }

    @NotNull
    public String getString(String path) {
        return getString(path, "");
    }

    @NotNull
    public List<String> getStringList(String path) {
        return super.getStringList(path);
    }
}