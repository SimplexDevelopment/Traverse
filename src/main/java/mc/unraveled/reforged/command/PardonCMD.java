package mc.unraveled.reforged.command;

import mc.unraveled.reforged.api.annotations.CommandInfo;
import mc.unraveled.reforged.banning.AbstractBan;
import mc.unraveled.reforged.command.base.AbstractCommandBase;
import mc.unraveled.reforged.plugin.Traverse;
import mc.unraveled.reforged.storage.DBBan;
import mc.unraveled.reforged.util.Utilities;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@CommandInfo(name = "pardon", description = "Unban a player", usage = "/pardon <player>", aliases = {"unban", "ub"})
public class PardonCMD extends AbstractCommandBase {

    public PardonCMD(@NotNull Traverse plugin) {
        super(plugin, "pardon");
    }

    @Override
    public Component cmd(CommandSender sender, String[] args) {
        if (args.length != 1) return Component.text("Usage: /pardon <player>");

        String name = args[0];
        OfflinePlayer player = Utilities.getOfflinePlayer(name).block();

        if (player == null) return MessageDefaults.MSG_NOT_FOUND;

        if (getPlugin().getBanManager().isBanned(player)) {
            getPlugin().getBanManager().unbake();

            DBBan ban = new DBBan(getPlugin().getSQLManager().establish());
            AbstractBan inst = ban.getBan(player.getUniqueId());

            if (inst == null) return Component.text("Failed to find ban for " + name);

            ban.delete(inst);
            getPlugin().getBanManager().eject(inst);
            getPlugin().getBanManager().bake();
            ban.close();

            return Component.text("Unbanned " + name);
        } else return Component.text(name + " is not banned");
    }
}
