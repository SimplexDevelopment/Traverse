package mc.unraveled.reforged.banning;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.Date;

public class SimpleBan extends AbstractBan {
    public SimpleBan(Player player, CommandSender source, String reason, Date expiry, boolean active) {
        super(player.getUniqueId().toString(),
                player.getAddress().getAddress().getHostAddress(),
                source.getName(),
                reason,
                Date.from(Instant.now()).getTime(),
                expiry.getTime(),
                active);
    }

    public SimpleBan(OfflinePlayer player, CommandSender source, String reason, Date expiry, boolean active) {
        super(player.getUniqueId().toString(),
                "",
                source.getName(),
                reason,
                Date.from(Instant.now()).getTime(),
                expiry.getTime(),
                active);
    }

    public SimpleBan(String uuid, String ip, String source, String reason, long propogated, long expiry, boolean active) {
        super(uuid, ip, source, reason, propogated, expiry, active);
    }
}
