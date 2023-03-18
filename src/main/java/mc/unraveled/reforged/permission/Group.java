package mc.unraveled.reforged.permission;

import mc.unraveled.reforged.api.Baker;
import org.bukkit.OfflinePlayer;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Group implements Baker {

    public static final String BAKED_GROUP = "Cannot modify a baked group.";
    private final Rank rank;
    private Set<String> permissions = new HashSet<>();
    private Set<OfflinePlayer> players = new HashSet<>();
    private boolean baked = false;

    public Group(Rank rank) {
        this.rank = rank;
    }

    public Rank getRank() {
        return rank;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public Set<OfflinePlayer> getPlayers() {
        return players;
    }

    public boolean isBaked() {
        return baked;
    }

    public void setBaked(boolean baked) {
        this.baked = baked;
    }

    public void insert(String permission) {
        if (baked) throw new IllegalStateException(BAKED_GROUP);
        permissions.add(permission);
    }

    public void insert(OfflinePlayer player) {
        if (baked) throw new IllegalStateException(BAKED_GROUP);
        players.add(player);
    }

    public void eject(String permission) {
        if (baked) throw new IllegalStateException(BAKED_GROUP);
        permissions.remove(permission);
    }

    public void eject(OfflinePlayer player) {
        if (baked) throw new IllegalStateException(BAKED_GROUP);
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
