package mc.unraveled.reforged.config;

import mc.unraveled.reforged.plugin.Traverse;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YamlManager {
    private final List<Yaml> configurations = new ArrayList<>();
    private final Traverse plugin;

    public YamlManager(Traverse plugin) {
        this.plugin = plugin;
    }

    public List<Yaml> getConfigurations() {
        return configurations;
    }

    public Traverse getPlugin() {
        return plugin;
    }

    public Builder bldr() {
        return new Builder(plugin);
    }

    public void insert(Yaml yaml) {
        configurations.add(yaml);
    }

    public void eject(Yaml yaml) {
        configurations.remove(yaml);
    }

    public void load(@NotNull Yaml yaml) {
        try {
            yaml.load(yaml.getYamlFile());
        } catch (IOException | InvalidConfigurationException e) {
            Bukkit.getLogger().severe("Failed to load " + yaml.getYamlFile().getName() + "!");
        }
    }

    public void save(@NotNull Yaml yaml) {
        try {
            yaml.save(yaml.getYamlFile());
        } catch (IOException e) {
            Bukkit.getLogger().severe("Failed to save " + yaml.getYamlFile().getName() + "!");
        }
    }

    public void loadAll() {
        configurations.forEach(y -> {
            try {
                y.load(y.getYamlFile());
            } catch (IOException | InvalidConfigurationException e) {
                getPlugin().getLogger().severe("Failed to load " + y.getYamlFile().getName() + "!");
                getPlugin().getLogger().severe(e.getMessage());
            }
        });
    }

    public void saveAll() {
        configurations.forEach(y -> {
            try {
                y.save(y.getYamlFile());
            } catch (IOException e) {
                getPlugin().getLogger().severe("Failed to save " + y.getYamlFile().getName() + "!");
                getPlugin().getLogger().severe(e.getMessage());
            }
        });
    }

    public static class Builder {
        private final Traverse plugin;

        private String fileName;
        private File dataFolder;
        private boolean copyDefaults;

        public Builder(Traverse plugin) {
            this.plugin = plugin;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            this.dataFolder = plugin.getDataFolder();
            this.copyDefaults = false;
            return this;
        }

        public Builder dataFolder(File dataFolder) {
            this.dataFolder = dataFolder;
            return this;
        }

        public Builder copyDefaults(boolean copyDefaults) {
            this.copyDefaults = copyDefaults;
            return this;
        }

        public Yaml build() {
            return new Yaml(plugin, fileName, dataFolder, copyDefaults) {
            };
        }
    }
}
