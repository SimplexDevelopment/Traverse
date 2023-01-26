package mc.unraveled.reforged.permission;

import lombok.Data;
import mc.unraveled.reforged.api.Baker;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class Group implements Baker {
    private final Rank rank;
    private Set<String> permissions = new HashSet<>();
    private Set<OfflinePlayer> players = new HashSet<>();
    private boolean baked = false;

    public Group(Rank rank) {
        this.rank = rank;
    }

    public void insert(String permission) {
        if (baked) throw new IllegalStateException("Cannot modify a baked group.");
        permissions.add(permission);
    }

    public void insert(OfflinePlayer player) {
        if (baked) throw new IllegalStateException("Cannot modify a baked group.");
        players.add(player);
    }

    public void eject(String permission) {
        if (baked) throw new IllegalStateException("Cannot modify a baked group.");
        permissions.remove(permission);
    }

    public void eject(OfflinePlayer player) {
        if (baked) throw new IllegalStateException("Cannot modify a baked group.");
        players.remove(player);
    }

    @Override
    public void bake() {
        if (baked) return;

        this.permissions = permissions.stream().collect(Collectors.toUnmodifiableSet());
        this.players = players.stream().collect(Collectors.toUnmodifiableSet());

        this.baked = true;
    }

    @Override
    public void unbake() {
        if (!baked) return;

        this.permissions = new HashSet<>(permissions);
        this.players = new HashSet<>(players);

        this.baked = false;

    }
}
