package mc.unraveled.reforged.listening;

import mc.unraveled.reforged.data.InfractionData;
import mc.unraveled.reforged.data.PlayerData;
import mc.unraveled.reforged.permission.Rank;
import mc.unraveled.reforged.plugin.Traverse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.Instant;
import java.util.Date;

public class PlayerDataListener extends AbstractListener {
    public PlayerDataListener(Traverse plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData data = getPlugin().getDataManager().getPlayerData(player.getUniqueId());

        if (data == null) {
            data = new PlayerData(player.getUniqueId(),
                    player.getName(),
                    Rank.NON_OP,
                    0L,
                    0,
                    Date.from(Instant.now()),
                    null,
                    InfractionData.getCachedInfractionData(player));
            getPlugin().getDataManager().addPlayerData(data);
        }

        if (getPlugin().getEconomyManager().hasAccount(player)) {
            data.setCoins((int) getPlugin().getEconomyManager().balance(player).balance);
        } else {
            getPlugin().getEconomyManager().newAccount(player);
        }

        data.setLastLogin(Date.from(Instant.now()));
    }
}
