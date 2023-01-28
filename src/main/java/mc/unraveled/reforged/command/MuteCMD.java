package mc.unraveled.reforged.command;

import mc.unraveled.reforged.api.annotations.CommandInfo;
import mc.unraveled.reforged.command.base.AbstractCommandBase;
import mc.unraveled.reforged.data.InfractionData;
import mc.unraveled.reforged.data.PlayerData;
import mc.unraveled.reforged.plugin.Traverse;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@CommandInfo(name = "mute", description = "Mute a player.", usage = "/mute <player> <duration>")
public class MuteCMD extends AbstractCommandBase {
    public MuteCMD(@NotNull Traverse plugin) {
        super(plugin, "mute");
    }

    @Override
    public Component cmd(CommandSender sender, String[] args) {
        if (args.length != 2) {
            return Component.text("Usage: /mute <player> <duration>");
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            return MessageDefaults.MSG_NOT_FOUND;
        }

        PlayerData pData = getPlugin().getDataManager().getPlayerData(target.getUniqueId());
        if (pData == null) throw new IllegalStateException("PlayerData is null!");

        InfractionData infData = pData.getInfractionData();
        if (infData == null) throw new IllegalStateException("InfractionData is null!");

        if (!infData.isMuted()) {
            infData.setMuted(true);
            return Component.text("You have muted " + target.getName() + " for " + args[1] + " seconds.");
        } else {
            return Component.text("Target is already muted.");
        }
    }
}
