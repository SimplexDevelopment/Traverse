package mc.unraveled.reforged.api;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public interface ICommandBase extends TabExecutor {
    /**
     * This represents the actual command execution.
     * It returns a component, which is sent back to the sender.
     * You can send the sender nothing by returning a {@link Component#empty()}.
     *
     * @param sender The sender of the command.
     * @param args   The arguments of the command, for subcommand parsing.
     * @return A component to be sent back to the sender.
     * This allows for us to do things like return a direct error message,
     * or signify the command completed successfully.
     */
    Component cmd(CommandSender sender, String[] args);

    /**
     * This represents the tab completion of the command.
     * It returns a list of strings, which are sent back to the sender.
     * This by default returns an empty list, which is used by {@link #onTabComplete(CommandSender, Command, String, String[])}
     * to signify that there are no tab completions. If you want to implement your own completions,
     * you must override this method.
     *
     * @param sender The sender of the command.
     * @param args   The arguments of the command, for subcommand parsing.
     * @return A list of strings to be sent back to the sender.
     */
    default List<String> tab(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
