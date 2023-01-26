package mc.unraveled.reforged.command.base;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class DummyCommand extends Command implements PluginIdentifiableCommand {
    private final AbstractCommandBase base;
    private final Plugin plugin;

    /**
     * @param plugin       Your plugin instance.
     * @param base         Your command instance.
     * @param name         The name of your command
     * @param description  The description of your command
     * @param usageMessage The usage for your command
     * @param aliases      The aliases for your command.
     */
    DummyCommand(@NotNull Plugin plugin, @NotNull AbstractCommandBase base, @NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
        super(name, description, usageMessage, aliases);
        this.setName(name);
        this.setDescription(description);
        this.setUsage(usageMessage);
        this.setAliases(aliases);
        this.setPermission(base.getPermission());
        this.permissionMessage(Component.empty().content(base.getPermissionMessage()));
        this.base = base;
        this.plugin = plugin;
    }

    /**
     * The actual executor method.
     *
     * @param sender       The user who sent the command
     * @param commandLabel The name of the command
     * @param args         Any additional arguments the user may input
     * @return Successfully executed the command or not
     */
    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        base.onCommand(sender, this, commandLabel, args);
        return true;
    }

    /**
     * @return Gets your plugin (Generic)
     */
    @Override
    public @NotNull Plugin getPlugin() {
        return plugin;
    }
}
