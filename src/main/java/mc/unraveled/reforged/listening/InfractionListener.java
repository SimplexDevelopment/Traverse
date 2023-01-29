package mc.unraveled.reforged.listening;

import io.papermc.paper.event.player.AsyncChatEvent;
import mc.unraveled.reforged.banning.BanManager;
import mc.unraveled.reforged.data.PlayerData;
import mc.unraveled.reforged.plugin.Traverse;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;

public class InfractionListener extends AbstractListener {

    public InfractionListener(Traverse plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void banCheck(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        BanManager manager = getPlugin().getBanManager();
        if (manager.isBanned(player)) {
            Component reason = Component.text(manager.getBan(player).getReason());
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, reason);
        }
    }

    @EventHandler
    public void muteCheck(AsyncChatEvent event) {
        Player player = event.getPlayer();
        PlayerData data = getPlugin().getDataManager().getPlayerData(player.getUniqueId());
        if (data == null) return;
        if (data.getInfractionData().isMuted()) {
            event.setCancelled(true);
            player.sendMessage(Component.text("You are muted!"));
        }
    }

}
