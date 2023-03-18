package mc.unraveled.reforged.command;

import mc.unraveled.reforged.api.annotations.CommandInfo;
import mc.unraveled.reforged.command.base.AbstractCommandBase;
import mc.unraveled.reforged.plugin.Traverse;
import mc.unraveled.reforged.util.Utilities;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@CommandInfo(name = "kick",
        description = "Kick a player.",
        usage = "/kick <player> <reason>",
        aliases = {"k"})
public class KickCMD extends AbstractCommandBase {

    public KickCMD(@NotNull Traverse plugin) {
        super(plugin, "kick", true);
    }

    @Override
    public Component cmd(CommandSender sender, String[] args) {
        if (args.length < 2) return Component.text("Usage: /kick <player> <reason>");

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) return MessageDefaults.MSG_NOT_FOUND;

        String reason = String.join(" ", args).substring(args[0].length() + 1);
        target.kick(Component.text(reason));
        broadcast(target.getName() + " has been kicked for " + reason + ".");
        return Component.text("You have kicked " + target.getName() + " for " + reason + ".");
    }

    @Override
    public List<String> tab(CommandSender sender, String[] args) {
        if (args.length == 1) return Utilities.playerCompletions(args[0]);

        return super.tab(sender, args);
    }
}
