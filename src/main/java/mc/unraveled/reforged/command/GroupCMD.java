package mc.unraveled.reforged.command;

import mc.unraveled.reforged.api.annotations.CommandInfo;
import mc.unraveled.reforged.command.base.AbstractCommandBase;
import mc.unraveled.reforged.permission.Rank;
import mc.unraveled.reforged.permission.RankManager;
import mc.unraveled.reforged.plugin.Traverse;
import mc.unraveled.reforged.util.Context;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

@CommandInfo(name = "group",
        description = "Manages groups for all players.",
        usage = "/group <add | del> <player> <group>",
        aliases = {"g"})
public class GroupCMD extends AbstractCommandBase {
    private final Rank[] ranks = Rank.values();

    public GroupCMD(@NotNull Traverse plugin) {
        super(plugin, "group");
    }

    @Override
    public Component cmd(CommandSender sender, String[] args) {

        if (args.length < 4) {
            return Component.text("Usage: /group <group> <add | del> <player <player> | permission <permission>>");
        }

        String group = args[0];
        String action = args[1];
        String target = args[2];
        String value = args[3];

        AtomicBoolean invalid = new AtomicBoolean(false);
        Context<Rank> rankContext = new Context<>(Rank.NON_OP);

        Arrays.stream(ranks)
                .filter(rank -> rank.getAttachment().getName().equalsIgnoreCase(group))
                .findFirst()
                .ifPresentOrElse(rankContext::setContext, () -> invalid.set(true));

        if (invalid.get()) return Component.text("Invalid group!").color(NamedTextColor.RED);

        boolean actionType; // false is delete, true is add

        switch (action) {
            case "add":
                actionType = true;
                break;
            case "del":
                actionType = false;
                break;
            default:
                return Component.text("Invalid action!").color(NamedTextColor.RED);
        }

        RankManager manager = getPlugin().getRankManager();
        OfflinePlayer player;

        manager.unbake();

        switch (target) {
            case "player":
                if (getPlugin().getServer().getPlayer(value) != null) {
                    player = getPlugin().getServer().getPlayer(value);
                } else {
                    return Component.text("Invalid player!").color(NamedTextColor.RED);
                }

                if (player == null) {
                    throw new IllegalStateException("Player cannot be null!");
                }

                if (actionType) {
                    manager.insertPlayer(rankContext.getContext(), player);
                    manager.bake();
                    manager.save();
                    return Component.text("Added player " + player.getName() + " to group " + group + "!").color(NamedTextColor.GREEN);
                } else {
                    manager.ejectPlayer(rankContext.getContext(), player);
                    manager.bake();
                    manager.save();
                    return Component.text("Removed player " + player.getName() + " from group " + group + "!").color(NamedTextColor.GREEN);
                }
            case "permission":
                if (actionType) {
                    manager.insertPermission(rankContext.getContext(), value);
                    manager.bake();
                    manager.save();
                    return Component.text("Added permission " + value + " to group " + group + "!").color(NamedTextColor.GREEN);
                } else {
                    manager.ejectPermission(rankContext.getContext(), value);
                    manager.bake();
                    manager.save();
                    return Component.text("Removed permission " + value + " from group " + group + "!").color(NamedTextColor.GREEN);
                }
            default:
                manager.bake();
                return Component.text("Invalid target!").color(NamedTextColor.RED);
        }
    }
}
