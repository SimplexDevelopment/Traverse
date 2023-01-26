package mc.unraveled.reforged.api;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

public interface ICommandBase extends TabExecutor {
    Component run(CommandSender sender, String[] args);
}
