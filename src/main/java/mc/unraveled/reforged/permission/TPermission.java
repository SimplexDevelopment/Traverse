package mc.unraveled.reforged.permission;

import org.bukkit.command.CommandSender;

public abstract class TPermission {
    private final String permission;
    private final String permissionMessage;
    private final boolean allowConsole;

    /**
     * @param permission        The permission the user should have to run the command
     * @param permissionMessage The message to send when the user does not have the permission to run the command.
     * @param allowConsole      Whether to allow the command to be run anywhere, or only in game.
     */
    public TPermission(String permission, String permissionMessage, boolean allowConsole) {
        this.permission = "traverse." + permission;
        this.permissionMessage = permissionMessage;
        this.allowConsole = allowConsole;
    }

    /**
     * Gets the permission for the command it represents.
     *
     * @return The permission required to run the command.
     */
    public String getPermission() {
        return permission;
    }

    /**
     * Gets the message to display when a user doesn't have permission to run the command.
     *
     * @return The message to send the user when they do not have the required permission.
     */
    public String getPermissionMessage() {
        return permissionMessage;
    }

    /**
     * Checks if the source of the command has the permission required to run it.
     *
     * @param sender The command source
     * @return Whether the sender has the permission or not.
     */
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(getPermission());
    }

    /**
     * @return Whether to allow the command to be run from anywhere, or only players.
     */
    public boolean allowConsole() {
        return allowConsole;
    }
}
