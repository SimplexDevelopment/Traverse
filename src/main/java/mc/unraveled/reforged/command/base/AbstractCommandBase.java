package mc.unraveled.reforged.command.base;

import lombok.Getter;
import mc.unraveled.reforged.api.ICommandBase;
import mc.unraveled.reforged.permission.TPermission;
import mc.unraveled.reforged.plugin.Traverse;
import mc.unraveled.reforged.util.BasicColors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public abstract class AbstractCommandBase extends TPermission implements ICommandBase {
    @Getter
    private final Traverse plugin;

    /**
     * @param plugin            The plugin that owns this command
     * @param permission        The permission the user should have to run the command
     * @param permissionMessage The message to send when the user does not have the permission to run the command.
     * @param allowConsole      Whether to allow the command to be run anywhere, or only in game.
     */
    public AbstractCommandBase(@NotNull Traverse plugin, @NotNull String permission, String permissionMessage, boolean allowConsole) {
        super(permission, permissionMessage, allowConsole);
        this.plugin = plugin;
    }

    /**
     * @param plugin            The plugin that owns this command
     * @param permission        The permission the user should have to run the command
     * @param permissionMessage The message to send when the user does not have the permission to run the command.
     */
    public AbstractCommandBase(@NotNull Traverse plugin, @NotNull String permission, String permissionMessage) {
        this(plugin, permission, permissionMessage, true);
    }

    /**
     * @param plugin       The plugin that owns this command
     * @param permission   The permission the user should have to run the command
     * @param allowConsole Whether to allow the command to be run anywhere, or only in game.
     */
    public AbstractCommandBase(@NotNull Traverse plugin, @NotNull String permission, boolean allowConsole) {
        this(plugin, permission, "You do not have permission to use this command!", allowConsole);
    }

    /**
     * @param plugin     The plugin that owns this command
     * @param permission The permission the user should have to run the command
     */
    public AbstractCommandBase(@NotNull Traverse plugin, @NotNull String permission) {
        this(plugin, permission, "You do not have permission to use this command!", true);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String lbl, String[] args) {
        if (!hasPermission(sender)) {
            sender.sendMessage(MessageDefaults.MSG_NO_PERMS);
            return true;
        }

        if (!(sender instanceof Player) && !allowConsole()) {
            sender.sendMessage(MessageDefaults.MSG_NOT_CONSOLE);
            return true;
        }

        Component result = cmd(sender, args);

        if (!result.equals(Component.empty())) {
            sender.sendMessage(result);
        }

        return true;
    }

    @Override
    public @Nullable
    List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return tab(sender, args);
    }

    /**
     * Returns a text component for Kyori friendly messaging.
     *
     * @param text The text to convert to a component
     * @return A {@link TextComponent} containing the message provided in {@param text}
     */
    @NotNull
    public TextComponent msg(@NotNull String text) {
        return Component.empty().content(text);
    }

    /**
     * Returns a text component for Kyori friendly messaging.
     *
     * @param text  The text to convert to a Component
     * @param color The color you'd like the text. These colors are basic and the majority of which are provided by Minecraft's native color system.
     * @return A {@link TextComponent} containing the message provided in {@param text} with the provided {@param color}
     */
    @NotNull
    public TextComponent msg(@NotNull String text, @NotNull BasicColors color) {
        return Component.empty().content(text).color(color.getColor());
    }

    @Nullable
    public Player getPlayer(@NotNull String name) {
        return Bukkit.getServer().getPlayer(name);
    }

    @Nullable
    public Player getPlayer(@NotNull UUID uuid) {
        return Bukkit.getServer().getPlayer(uuid);
    }

    public List<? extends Player> getOnlinePlayers() {
        return Bukkit.getOnlinePlayers().stream().toList();
    }

    public void broadcast(String text) {
        Bukkit.getServer().broadcast(msg(text));
    }

    public void broadcast(String text, BasicColors color) {
        Bukkit.getServer().broadcast(msg(text, color));
    }

    public void enablePlugin(Plugin plugin) {
        Bukkit.getServer().getPluginManager().enablePlugin(plugin);
    }

    public void disablePlugin(Plugin plugin) {
        Bukkit.getServer().getPluginManager().disablePlugin(plugin);
    }

    protected static class MessageDefaults {
        public static final Component MSG_INVALID_AMOUNT = Component.text("Amount must be greater than 0.").color(NamedTextColor.RED);
        public static final Component MSG_NO_PERMS = Component.text("You do not have permission to use this command!").color(NamedTextColor.RED);
        public static final Component MSG_NOT_PLAYER = Component.text("This command can only be run through the console.").color(NamedTextColor.RED);
        public static final Component MSG_NOT_FOUND = Component.text("Player not found.").color(NamedTextColor.RED);
        public static final Component MSG_NOT_CONSOLE = Component.text("This command can only be run by a player.").color(NamedTextColor.RED);
        public static final Component MSG_NOT_ENOUGH_ARGS = Component.text("Not enough arguments.").color(NamedTextColor.RED);
        public static final Component MUTED = Component.text("You are muted!").color(NamedTextColor.RED);
        public static final Component BANNED = Component.text("You are banned!").color(NamedTextColor.RED);
    }
}
