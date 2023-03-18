package mc.unraveled.reforged.data;

import mc.unraveled.reforged.permission.RankAttachment;
import mc.unraveled.reforged.plugin.Traverse;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class StaffChat {
    private final Set<UUID> playersInChat = new HashSet<>();
    private final Traverse plugin;

    public StaffChat(Traverse plugin) {
        this.plugin = plugin;
    }

    public boolean inChat(UUID uuid) {
        return playersInChat.contains(uuid);
    }

    public void toggleChat(UUID uuid) {
        if (inChat(uuid)) {
            playersInChat.remove(uuid);
        } else {
            playersInChat.add(uuid);
        }
    }

    public Component formatStaffChatMessage(CommandSender player, String message) {
        RankAttachment rank = plugin.getRankManager().getRank(player).getAttachment();
        String legacyStaffFormat = "&8[&bStaff&8]&r";
        String playerName = "&c" + player.getName() + "&r";
        String rankFormat = "&8[" + rank.getColor() + rank.getTag() + "&8]&r";

        String appended = String.format("%s %s %s: %s", legacyStaffFormat, playerName, rankFormat, message);

        return LegacyComponentSerializer.legacyAmpersand().deserialize(appended);
    }

    public void distribute(CommandSender source, String message) {
        plugin.getServer().getOnlinePlayers().stream()
                .filter(player -> player.hasPermission("traverse.staff"))
                .forEach(player -> player.sendMessage(formatStaffChatMessage(source, message)));
    }
}
