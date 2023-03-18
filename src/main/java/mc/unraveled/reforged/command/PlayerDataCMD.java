package mc.unraveled.reforged.command;

import mc.unraveled.reforged.api.annotations.CommandInfo;
import mc.unraveled.reforged.command.base.AbstractCommandBase;
import mc.unraveled.reforged.data.InfractionData;
import mc.unraveled.reforged.data.PlayerData;
import mc.unraveled.reforged.permission.Rank;
import mc.unraveled.reforged.plugin.Traverse;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(
        name = "playerdata",
        description = "Checks your player data. Also can be used to check and modify other players' data.",
        usage = "/<command> [<player> <info | reset <data> | set <data> <newValue>]",
        aliases = {"pd", "userdata", "ud"}
)
public class PlayerDataCMD extends AbstractCommandBase {
    public PlayerDataCMD(Traverse plugin) {
        super(plugin, "playerdata");
    }

    @Override
    public Component cmd(CommandSender sender, String[] args) {
        switch (args.length) {
            case 0 -> {
                if (sender instanceof Player player) {
                    PlayerData data = getPlugin().getDataManager().getPlayerData(player.getUniqueId());

                    if (data == null) {
                        return Component.text("Player data not found!").color(NamedTextColor.RED);
                    }

                    String newLine = "\n";

                    String sb = "Player Data:" + newLine +
                            "UUID: " + data.getUuid() + newLine +
                            "Username: " + data.getUserName() + newLine +
                            "Rank: " + data.getRank() + newLine +
                            "Play Time: " + data.getPlaytime() + newLine +
                            "Balance: " + data.getCoins() + newLine +
                            "Infractions: " + data.getInfractionData().getInfractions() + newLine +
                            "Last Login: " + data.getLastLogin() + newLine +
                            "Login Message: " + data.getLoginMessage();

                    return Component.text(sb);
                } else return Component.text("This command can only be used by players!");
            }
            case 2 -> {
                if (args[1].equalsIgnoreCase("info")) {
                    Player target = getPlugin().getServer().getPlayer(args[0]);
                    if (target == null) return MessageDefaults.MSG_NOT_FOUND;

                    PlayerData data = getPlugin().getDataManager().getPlayerData(target.getUniqueId());
                    if (data == null) {
                        return Component.text("Player data not found!").color(NamedTextColor.RED);
                    }

                    String newLine = "\n";

                    String sb = "Player Data:" + newLine +
                            "UUID: " + data.getUuid() + newLine +
                            "Username: " + data.getUserName() + newLine +
                            "Rank: " + data.getRank() + newLine +
                            "Play Time: " + data.getPlaytime() + newLine +
                            "Balance: " + data.getCoins() + newLine +
                            "Infractions: " + data.getInfractionData().getInfractions() + newLine +
                            "Last Login: " + data.getLastLogin() + newLine +
                            "Login Message: " + data.getLoginMessage();

                    return Component.text(sb);
                } else {
                    return usage();
                }
            }
            case 3 -> {
                if (args[1].equalsIgnoreCase("reset")) {
                    Player target = getPlugin().getServer().getPlayer(args[0]);
                    if (target == null) return MessageDefaults.MSG_NOT_FOUND;

                    switch (args[2]) {
                        case "rank":
                            PlayerData data = getPlugin().getDataManager().getPlayerData(target.getUniqueId());
                            if (data == null) return MessageDefaults.MSG_NOT_FOUND;

                            data.setRank(Rank.NON_OP);
                            getPlugin().getDataManager().updatePlayer(target.getUniqueId(), data);
                            return Component.text("Reset " + target.getName() + "'s rank to " + Rank.NON_OP);
                        case "playtime":
                            data = getPlugin().getDataManager().getPlayerData(target.getUniqueId());
                            if (data == null) return MessageDefaults.MSG_NOT_FOUND;

                            data.setPlaytime(0);
                            getPlugin().getDataManager().updatePlayer(target.getUniqueId(), data);
                            return Component.text("Reset " + target.getName() + "'s playtime to 0");
                        case "balance":
                            data = getPlugin().getDataManager().getPlayerData(target.getUniqueId());
                            if (data == null) return MessageDefaults.MSG_NOT_FOUND;

                            data.setCoins(0);
                            getPlugin().getDataManager().updatePlayer(target.getUniqueId(), data);
                            return Component.text("Reset " + target.getName() + "'s balance to 0");
                        case "infractions":
                            data = getPlugin().getDataManager().getPlayerData(target.getUniqueId());
                            if (data == null) return MessageDefaults.MSG_NOT_FOUND;

                            InfractionData inf = data.getInfractionData();
                            while (inf.getInfractions() > 0) {
                                inf.decrement();
                            }

                            inf.setFrozen(false);
                            inf.setJailed(false);
                            inf.setMuted(false);
                            inf.setLocked(false);

                            getPlugin().getDataManager().updatePlayer(target.getUniqueId(), data);
                            return Component.text("Reset " + target.getName() + "'s infractions to 0");
                        case "login":
                            data = getPlugin().getDataManager().getPlayerData(target.getUniqueId());
                            if (data == null) return MessageDefaults.MSG_NOT_FOUND;

                            data.setLoginMessage("");
                            getPlugin().getDataManager().updatePlayer(target.getUniqueId(), data);
                            return Component.text("Reset " + target.getName() + "'s login message to empty");
                        default:
                            return Component.text("Invalid data type!").color(NamedTextColor.RED);
                    }
                }
            }
            case 4 -> {
                Player target = getPlugin().getServer().getPlayer(args[0]);
                if (target == null) return MessageDefaults.MSG_NOT_FOUND;
                if (args[1].equalsIgnoreCase("set")) {
                    switch (args[2]) {
                        case "rank":
                            PlayerData data = getPlugin().getDataManager().getPlayerData(target.getUniqueId());
                            if (data == null) return MessageDefaults.MSG_NOT_FOUND;

                            Rank rank = Rank.valueOf(args[3].toUpperCase());
                            data.setRank(rank);
                            getPlugin().getDataManager().updatePlayer(target.getUniqueId(), data);
                            return Component.text("Set " + target.getName() + "'s rank to " + rank);
                        case "playtime":
                            data = getPlugin().getDataManager().getPlayerData(target.getUniqueId());
                            if (data == null) return MessageDefaults.MSG_NOT_FOUND;

                            int playtime = Integer.parseInt(args[3]);
                            data.setPlaytime(playtime);
                            getPlugin().getDataManager().updatePlayer(target.getUniqueId(), data);
                            return Component.text("Set " + target.getName() + "'s playtime to " + playtime);
                        case "balance":
                            data = getPlugin().getDataManager().getPlayerData(target.getUniqueId());
                            if (data == null) return MessageDefaults.MSG_NOT_FOUND;

                            int balance = Integer.parseInt(args[3]);
                            data.setCoins(balance);
                            getPlugin().getDataManager().updatePlayer(target.getUniqueId(), data);
                            return Component.text("Set " + target.getName() + "'s balance to " + balance);
                        case "infractions":
                            data = getPlugin().getDataManager().getPlayerData(target.getUniqueId());
                            if (data == null) return MessageDefaults.MSG_NOT_FOUND;

                            InfractionData inf = data.getInfractionData();
                            int infractions = Integer.parseInt(args[3]);
                            while (inf.getInfractions() > infractions) {
                                inf.decrement();
                            }

                            getPlugin().getDataManager().updatePlayer(target.getUniqueId(), data);
                            return Component.text("Set " + target.getName() + "'s infractions to " + infractions);
                        case "login":
                            data = getPlugin().getDataManager().getPlayerData(target.getUniqueId());
                            if (data == null) return MessageDefaults.MSG_NOT_FOUND;

                            String login = args[3];
                            data.setLoginMessage(login);
                            getPlugin().getDataManager().updatePlayer(target.getUniqueId(), data);
                            return Component.text("Set " + target.getName() + "'s login message to " + login);
                        default:
                            return Component.text("Invalid data type!").color(NamedTextColor.RED);
                    }
                } else {
                    return usage();
                }
            }
            default -> {
                return usage();
            }
        }
        return null;
    }

    private Component usage() {
        return Component
                .text("/<command> [<player> <info | reset <data> | set <data> <newValue>]")
                .color(NamedTextColor.GRAY);
    }
}
