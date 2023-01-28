package mc.unraveled.reforged.config;

import lombok.Getter;
import lombok.SneakyThrows;
import mc.unraveled.reforged.api.Baker;
import mc.unraveled.reforged.plugin.Traverse;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@Getter
abstract class Yaml extends YamlConfiguration implements Baker {
    private final String fileName;
    private final File dataFolder;
    private final Traverse plugin;
    private final File yamlFile;

    @Getter
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

    /**
     * Makes the file read only after saving to disk.
     */
    @SneakyThrows
    @Override
    public void bake() {
        if (baked) return;

        super.save(yamlFile);

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
}
