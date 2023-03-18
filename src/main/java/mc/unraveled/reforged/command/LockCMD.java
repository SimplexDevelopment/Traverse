package mc.unraveled.reforged.command;

import mc.unraveled.reforged.api.annotations.CommandInfo;
import mc.unraveled.reforged.command.base.AbstractCommandBase;
import mc.unraveled.reforged.data.InfractionData;
import mc.unraveled.reforged.data.PlayerData;
import mc.unraveled.reforged.plugin.Traverse;
import mc.unraveled.reforged.util.Utilities;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@CommandInfo(
        name = "lock",
        usage = "/<command> <player> <on | off>",
        description = "Prevents a player from inputting any actions."
)
public class LockCMD extends AbstractCommandBase {
    public LockCMD(@NotNull Traverse plugin) {
        super(plugin, "lock");
    }

    @Override
    public Component cmd(CommandSender sender, String[] args) {
        if (args.length != 2) {
            return Component.text("Usage: /lock <player> <on | off>");
        }

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            return Component.text("Player not found.");
        }

        PlayerData data = getPlugin().getDataManager().getPlayerData(player.getUniqueId());
        if (data == null) {
            return Component.text("PlayerData for target user cannot be found!");
        }

        switch (args[1]) {
            case "on" -> {
                data.getInfractionData().setLocked(true);
                data.getInfractionData().increment();
                return Component.text("Locked " + player.getName() + ".");
            }
            case "off" -> {
                data.getInfractionData().setLocked(false);
                return Component.text("Unlocked " + player.getName() + ".");
            }
            default -> {
                return Component.text("Usage: /lock <player> <on | off>");
            }
        }
    }


    @Override
    public List<String> tab(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Utilities.playerCompletions(args[0]);
        }

        if (args.length == 2) {
            return Utilities.stringCompletions(args[1], "on", "off");
        }

        return super.tab(sender, args);
    }
}
