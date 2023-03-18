package mc.unraveled.reforged.listening;

import mc.unraveled.reforged.data.InfractionData;
import mc.unraveled.reforged.data.PlayerData;
import mc.unraveled.reforged.data.PlayerDataBuilder;
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
            PlayerDataBuilder builder = new PlayerDataBuilder()
                    .setUuid(player.getUniqueId())
                    .setUserName(player.getName())
                    .setRank(Rank.NON_OP)
                    .setPlaytime(0L)
                    .setCoins(0)
                    .setLastLogin(Date.from(Instant.now()))
                    .setLoginMessage(null)
                    .setInfractionData(InfractionData.getCachedInfractionData(player))
                    .compile();
            data = new PlayerData(builder);
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
