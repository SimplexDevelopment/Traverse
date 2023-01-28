package mc.unraveled.reforged.data;

import mc.unraveled.reforged.permission.Rank;
import mc.unraveled.reforged.plugin.Traverse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.Instant;
import java.util.Date;

public class PlayerDataListener implements Listener {
    private final Traverse plugin;

    public PlayerDataListener(Traverse plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData data = plugin.getDataManager().getPlayerData(player.getUniqueId());

        if (data == null) {
            data = new PlayerData(player.getUniqueId(), player.getName(), Rank.NON_OP, 0L, 0, Date.from(Instant.now()), InfractionData.getCachedInfractionData(player));
            plugin.getDataManager().addPlayerData(data);
        }

        data.setLastLogin(Date.from(Instant.now()));
    }
}
