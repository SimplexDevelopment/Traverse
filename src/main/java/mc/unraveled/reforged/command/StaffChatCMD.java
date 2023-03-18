package mc.unraveled.reforged.command;

import mc.unraveled.reforged.api.annotations.CommandInfo;
import mc.unraveled.reforged.command.base.AbstractCommandBase;
import mc.unraveled.reforged.plugin.Traverse;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@CommandInfo(name = "staffchat",
        description = "Toggle staff chat.",
        usage = "/staffchat [message]",
        aliases = {"sc", "ac", "o"})
public class StaffChatCMD extends AbstractCommandBase {
    public StaffChatCMD(@NotNull Traverse plugin) {
        super(plugin, "staff", true);
    }

    @Override
    public Component cmd(CommandSender sender, String[] args) {
        if (args.length == 0) {
            if (sender instanceof ConsoleCommandSender) {
                return Component.text("You must be in game to toggle staff chat.");
            }

            getPlugin().getStaffChat().toggleChat(((Player) sender).getUniqueId());

            boolean chatStat = getPlugin().getStaffChat().inChat(((Player) sender).getUniqueId());

            return Component.text("Staff chat toggled " + (chatStat ? "on" : "off") + ".");
        }

        String message = String.join(" ", args);
        getPlugin().getStaffChat().distribute(sender, message);
        return null;
    }

    @Override
    public List<String> tab(CommandSender sender, String[] args) {
        return super.tab(sender, args);
    }
}
