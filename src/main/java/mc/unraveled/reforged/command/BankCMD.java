package mc.unraveled.reforged.command;

import mc.unraveled.reforged.api.annotations.CommandInfo;
import mc.unraveled.reforged.command.base.AbstractCommandBase;
import mc.unraveled.reforged.economy.EconomyRequest;
import mc.unraveled.reforged.plugin.Traverse;
import mc.unraveled.reforged.util.Utilities;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@CommandInfo(
        name = "bank",
        description = "Bank command",
        usage = "/bank <info | (pay | request <player> <amount>)>"
)
public class BankCMD extends AbstractCommandBase {
    public BankCMD(Traverse plugin) {
        super(plugin, "bank", false);
    }

    @Override
    public Component cmd(CommandSender sender, String[] args) {
        if (args.length < 1 || args.length > 3) return Component.text("Usage: /bank <info | (pay | request <player> <amount>)>");

        Player player = (Player) sender;

        if (args.length == 3) {
            String action = args[0];

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) return MessageDefaults.MSG_NOT_FOUND;

            int amount = Integer.parseInt(args[2]);

            switch(action) {
                case "pay":
                    if (amount < 0) return MessageDefaults.MSG_INVALID_AMOUNT;

                    if (getPlugin().getEconomyManager().transfer(player, target, amount).transactionSuccess()) {
                        return Component.text("You have paid " + target.getName() + " " + amount + " coins.");
                    } else {
                        return Component.text("You do not have enough coins to pay " + target.getName() + " " + amount + " coins.");
                    }

                case "request":
                    if (amount < 0) return MessageDefaults.MSG_INVALID_AMOUNT;

                    getPlugin().getEconomyManager().request(player, target, amount);

                    break;
                default:
                    return Component.text("Usage: /bank <info | (pay | request <player> <amount>)>");
            }
        }

        if (args.length == 1) {
            String action = args[0];

            switch(action) {
                case "info":
                    return Component.text("You have " + getPlugin().getEconomyManager().balance(player) + " coins.");
                case "accept":
                    if (!getPlugin().getEconomyManager().getRequests().containsKey(player)) return Component.text("You do not have any pending requests.");

                    getPlugin().getEconomyManager().getRequests().get(player).forEach(EconomyRequest::accept);
                    // THIS IS GOING TO CHANGE, BY DEFAULT THIS ACCEPTS ALL REQUESTS. GOING TO IMPLEMENT A WAY TO ACCEPT EACH REQUEST INDIVIDUALLY.

                    break;
                case "deny":
                    if (!getPlugin().getEconomyManager().getRequests().containsKey(player)) return Component.text("You do not have any pending requests.");

                    getPlugin().getEconomyManager().getRequests().get(player).forEach(EconomyRequest::deny);
                    // THIS IS GOING TO CHANGE, BY DEFAULT THIS DENIES ALL REQUESTS. GOING TO IMPLEMENT A WAY TO DENY EACH REQUEST INDIVIDUALLY.

                    break;
                default:
                    return Component.text("Usage: /bank <info | (pay | request <player> <amount>)>");
            }
        }
        return Component.empty();
    }

    @Override
    public List<String> tab(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            return Utilities.playerCompletions(args[0]);
        }

        if (args.length == 2) {
            return Utilities.stringCompletions(args[1], "reset", "add", "set", "take");
        }

        if (args.length == 3) {
            completions.add("amount");
            return completions;
        }

        return completions;
    }
}
