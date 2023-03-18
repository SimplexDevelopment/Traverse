package mc.unraveled.reforged.listening;

import io.papermc.paper.event.player.AsyncChatEvent;
import mc.unraveled.reforged.plugin.Traverse;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class StaffChatListener extends AbstractListener {
    public StaffChatListener(Traverse plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        if (getPlugin().getStaffChat().inChat(event.getPlayer().getUniqueId())) {
            Component message = event.message();
            event.setCancelled(true);
            getPlugin().getServer().getOnlinePlayers().stream()
                    .filter(player -> player.hasPermission("traverse.staff"))
                    .forEach(player -> player.sendMessage(message));

        }
    }
}
