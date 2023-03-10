package mc.unraveled.reforged.command;

import mc.unraveled.reforged.api.annotations.CommandInfo;
import mc.unraveled.reforged.banning.BanManager;
import mc.unraveled.reforged.banning.SimpleBan;
import mc.unraveled.reforged.command.base.AbstractCommandBase;
import mc.unraveled.reforged.plugin.Traverse;
import mc.unraveled.reforged.util.Utilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CommandInfo(name = "ban",
        description = "Ban a player. Use -n as the second parameter for the default ban reason.",
        usage = "/ban <player> <duration> <reason>",
        aliases = {"b", "tempban", "tb"})
public class BanCMD extends AbstractCommandBase {
    public BanCMD(@NotNull Traverse plugin) {
        super(plugin, "ban");
    }

    @Override
    public Component cmd(CommandSender sender, String[] args) {
        if (args.length < 2) {
            return Component.text("Usage: /ban <player> <duration> <reason | -n>");
        }

        BanManager manager = getPlugin().getBanManager();
        OfflinePlayer target = (getPlugin().getServer().getPlayer(args[0]) != null) ? getPlugin().getServer().getPlayer(args[0]) : getPlugin().getServer().getOfflinePlayer(args[0]);
        String duration = args[1];
        String reason;

        if (args[2].equalsIgnoreCase("-n")) {
            reason = MessageDefaults.BANNED.toString();
        } else {
            reason = StringUtils.join(ArrayUtils.subarray(args, 2, args.length - 1), " ");
        }

        Date expiry = Utilities.parseDate(duration);
        String expiryString = Utilities.parseDateToString(expiry);

        if (target == null) {
            return MessageDefaults.MSG_NOT_FOUND;
        }

        if (reason.isEmpty() || duration.isEmpty()) {
            return Component.text("Usage: /ban <player> <duration> <reason>");
        }

        manager.unbake();

        SimpleBan ban = new SimpleBan(target, sender, reason, Utilities.parseDate(duration), true);

        if (target.isOnline()) {
            ((Player) target).kick(Component.text("You have been banned for " + reason + " until " + expiryString));
        }

        manager.insert(ban);
        manager.bake();
        manager.save();
        return Component.text("Successfully banned user " + target.getName() + " for " + reason + " until " + expiryString).color(NamedTextColor.YELLOW);
    }

    @Override
    public List<String> tab(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            return Utilities.playerCompletions(args[0]);
        }

        if (args.length == 2) {
            return Utilities.stringCompletions(args[1], "duration", "1d", "1h", "5m");
        }

        if (args.length == 3) {
            return Utilities.stringCompletions(args[2], "reason", "-n");
        }

        return completions;
    }
}
