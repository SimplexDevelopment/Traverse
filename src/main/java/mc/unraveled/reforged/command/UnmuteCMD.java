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

@CommandInfo(name = "unmute", description = "Unmute a player", usage = "/unmute <player>", aliases = {"um"})
public class UnmuteCMD extends AbstractCommandBase {
    public UnmuteCMD(@NotNull Traverse plugin) {
        super(plugin, "unmute");
    }

    @Override
    public Component cmd(CommandSender sender, String[] args) {
        if (args.length != 1) return Component.text("Usage: /unmute <player>");

        String name = args[0];

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) return MessageDefaults.MSG_NOT_FOUND;

        PlayerData data = getPlugin().getDataManager().getPlayerData(player.getUniqueId());
        if (data == null) return Component.text("Failed to find data for " + name);

        InfractionData inf = data.getInfractionData();
        if (inf == null) return Component.text("Failed to find infraction data for " + name);

        if (inf.isMuted()) {
            inf.setMuted(false);
            return Component.text("Unmuted " + name);
        }
        else return Component.text(name + " is not muted");
    }
}
