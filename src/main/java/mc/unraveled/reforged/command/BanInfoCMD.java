package mc.unraveled.reforged.command;

import mc.unraveled.reforged.api.annotations.CommandInfo;
import mc.unraveled.reforged.banning.AbstractBan;
import mc.unraveled.reforged.command.base.AbstractCommandBase;
import mc.unraveled.reforged.plugin.Traverse;
import mc.unraveled.reforged.util.ComponentBuilder;
import mc.unraveled.reforged.util.Utilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@CommandInfo(
        name = "baninfo",
        description = "Shows information about a ban",
        usage = "/baninfo <player>",
        aliases = {"bi", "bandata"}
)
public class BanInfoCMD extends AbstractCommandBase {
    public BanInfoCMD(@NotNull Traverse plugin) {
        super(plugin, "baninfo");
    }

    @Override
    public Component cmd(CommandSender sender, String[] args) {
        if (args.length != 1) {
            return Component.text("Usage: /baninfo <player>");
        }

        OfflinePlayer player = getPlugin().getServer().getOfflinePlayer(args[0]);
        AbstractBan ban = getPlugin().getBanManager().getBan(player);

        if (ban == null) {
            return Component.text("That player is not banned.");
        }

        NamedTextColor GENERAL = NamedTextColor.YELLOW;
        NamedTextColor INFO = NamedTextColor.GREEN;
        NamedTextColor DETAIL = NamedTextColor.RED;

        return ComponentBuilder.begin()
                .append("Ban info for ", GENERAL)
                .append(player.getName(), DETAIL)
                .append(":", GENERAL)
                .append("\nUUID: ", INFO)
                .append(ban.getUuid(), DETAIL)
                .append("\nIP: ", INFO)
                .append(ban.getIp(), DETAIL)
                .append("\nReason: ", INFO)
                .append(ban.getReason(), DETAIL)
                .append("\nBanned by: ", INFO)
                .append(ban.getSource(), DETAIL)
                .append("\nBanned on: ", INFO)
                .append(ban.getPropagated(), DETAIL)
                .append("\nExpires on: ", INFO)
                .append(ban.getExpiry(), DETAIL).build();
    }

    @Override
    public List<String> tab(CommandSender sender, String[] args) {
        return Utilities.playerCompletions(args[0]);
    }
}
