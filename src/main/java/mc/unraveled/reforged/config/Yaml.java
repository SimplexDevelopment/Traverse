package mc.unraveled.reforged.config;

import mc.unraveled.reforged.api.Baker;
import mc.unraveled.reforged.plugin.Traverse;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public abstract class Yaml extends YamlConfiguration implements Baker {
    private final String fileName;
    private final File dataFolder;
    private final Traverse plugin;
    private final File yamlFile;
    private boolean baked = false;

    Yaml(@NotNull Traverse plugin, String fileName, File dataFolder, boolean copyDefaults) {
        this.fileName = fileName;
        this.plugin = plugin;
        this.dataFolder = dataFolder;
        this.yamlFile = new File(dataFolder, fileName);

        if (copyDefaults) {
            plugin.saveResource(fileName, true);
        }

        bake();
    }

    Yaml(@NotNull Traverse plugin, String fileName, boolean copyDefaults) {
        this(plugin, fileName, plugin.getDataFolder(), copyDefaults);
    }

    Yaml(@NotNull Traverse plugin, String fileName) {
        this(plugin, fileName, false);
    }

    public String getFileName() {
        return fileName;
    }

    public File getDataFolder() {
        return dataFolder;
    }

    public Traverse getPlugin() {
        return plugin;
    }

    public File getYamlFile() {
        return yamlFile;
    }

    public boolean isBaked() {
        return baked;
    }

    /**
     * Makes the file read only after saving to disk.
     */
    @Override
    public void bake() {
        if (baked) return;

        try {
            super.save(yamlFile);
        } catch (IOException e) {
            Bukkit.getLogger().severe(e.getMessage());
        }

        if (yamlFile.setWritable(false)) {
            getPlugin().getLogger().info("Baked " + getFileName());
            baked = true;
        } else {
            getPlugin().getLogger().warning("Failed to bake " + getFileName());
        }
    }

    /**
     * Allows read and write access to the file.
     */
    @Override
    public void unbake() {
        if (!baked) return;

        if (yamlFile.setWritable(true)) {
            getPlugin().getLogger().info("Unbaked " + getFileName());
            baked = false;
        } else {
            getPlugin().getLogger().warning("Failed to unbake " + getFileName());
        }
    }

    public void saveToFile() {
        unbake();
        try {
            super.save(yamlFile);
        } catch (IOException e) {
            Bukkit.getLogger().severe(e.getMessage());
        }
        bake();
    }

    public void loadFromFile() {
        unbake();
        try {
            super.load(yamlFile);
        } catch (IOException | InvalidConfigurationException e) {
            Bukkit.getLogger().severe(e.getMessage());
        }
        bake();
    }
}
