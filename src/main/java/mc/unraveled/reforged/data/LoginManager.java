package mc.unraveled.reforged.data;

import lombok.Getter;
import mc.unraveled.reforged.api.Baker;
import mc.unraveled.reforged.listening.AbstractListener;
import mc.unraveled.reforged.plugin.Traverse;
import mc.unraveled.reforged.storage.DBUser;
import mc.unraveled.reforged.util.Pair;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class LoginManager implements Baker {
    private final Traverse plugin;
    private Set<Pair<OfflinePlayer, LoginInfo>> dataSet = new HashSet<>(); // VALUE ONLY MODIFIED BY BAKER
    private boolean baked = false;

    public LoginManager(Traverse plugin) {
        this.plugin = plugin;

        DBUser user = new DBUser(plugin.getSQLManager().establish());
        user.getLoginMessages().forEach((oPlayer, info) -> dataSet.add(new Pair<>(oPlayer, info)));
        user.close();

        bake();

        new LoginListener(plugin);
    }

    public void add(OfflinePlayer player, LoginInfo data) {
        dataSet.add(new Pair<>(player, data));
    }

    public void remove(OfflinePlayer player) {
        dataSet.removeIf(pair -> pair.getFirst().equals(player));
    }

    @Nullable
    public LoginInfo get(OfflinePlayer player) {
        return dataSet.stream()
                .filter(pair -> pair.getFirst().equals(player))
                .map(Pair::getSecond)
                .findFirst()
                .orElse(null);
    }

    public void set(OfflinePlayer player, LoginInfo data) {
        remove(player);
        add(player, data);
    }

    @Override
    public void bake() {
        if (baked) return;

        dataSet.forEach(pair -> {
            OfflinePlayer player = pair.getFirst();
            LoginInfo data = pair.getSecond();

            DBUser user = new DBUser(plugin.getSQLManager().establish());
            user.setLoginMessage(player.getUniqueId().toString(), data.getLoginMessage().toString());
            user.close();
        });

        this.dataSet = dataSet.stream().collect(Collectors.toUnmodifiableSet());
        this.baked = true;
    }

    @Override
    public void unbake() {
        if (!baked) return;

        this.dataSet = new HashSet<>(dataSet);
        this.baked = false;
    }

    private static final class LoginListener extends AbstractListener {
        public LoginListener(Traverse plugin) {
            super(plugin);
        }

        @EventHandler
        public void onLogin(PlayerJoinEvent event) {
            Player player = event.getPlayer();
            DBUser user = new DBUser(getPlugin().getSQLManager().establish());
            LoginInfo info = user.getLoginInfo(player.getUniqueId().toString());
            user.close();
            event.joinMessage(info.getLoginMessage());
        }
    }
}
