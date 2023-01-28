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

import java.util.Date;

@CommandInfo(name = "ban",
        description = "Ban a player",
        usage = "/ban <player> <reason> [duration]")
public class BanCMD extends AbstractCommandBase {
    public BanCMD(@NotNull Traverse plugin) {
        super(plugin, "ban");
    }

    @Override
    public Component cmd(CommandSender sender, String[] args) {
        if (args.length < 2) {
            return Component.text("Usage: /ban <player> <duration> <reason>");
        }

        BanManager manager = getPlugin().getBanManager();
        OfflinePlayer target = (getPlugin().getServer().getPlayer(args[0]) != null) ? getPlugin().getServer().getPlayer(args[0]) : getPlugin().getServer().getOfflinePlayer(args[0]);
        String duration = args[1];
        String reason = StringUtils.join(ArrayUtils.subarray(args, 2, args.length - 1), " ");

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
}
