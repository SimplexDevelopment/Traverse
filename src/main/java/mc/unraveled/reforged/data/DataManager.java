package mc.unraveled.reforged.data;

import mc.unraveled.reforged.api.Baker;
import mc.unraveled.reforged.plugin.Traverse;
import mc.unraveled.reforged.storage.DBUser;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public final class DataManager implements Baker {
    public static final String MANAGER_IS_BAKED = "Cannot remove player data while the data manager is baked.";
    private final Traverse plugin;
    private Set<PlayerData> playerDataCache = new HashSet<>(); // Should only be set by the baker.
    private boolean baked = false; // Should only be set by the baker.

    public DataManager(Traverse plugin) {
        this.plugin = plugin;
        DBUser user = new DBUser(plugin.getSQLManager().establish());
        user.createTable();
        playerDataCache.addAll(user.all());
        user.close();
        bake();
    }

    @Nullable
    public PlayerData getPlayerData(String playerName) {
        return playerDataCache.stream()
                .filter(data -> data.getUserName().equals(playerName))
                .findFirst()
                .orElse(null);
    }

    @Nullable
    public PlayerData getPlayerData(UUID uuid) {
        return playerDataCache.stream()
                .filter(data -> data.getUuid().equals(uuid))
                .findFirst()
                .orElse(null);
    }

    public void updatePlayer(UUID uuid, PlayerData data) {
        if (baked) throw new IllegalStateException("Cannot update player data while the data manager is baked.");

        playerDataCache.removeIf(playerData -> playerData.getUuid().equals(uuid));
        playerDataCache.add(data);
    }

    public void addPlayerData(PlayerData data) {
        if (baked) throw new IllegalStateException("Cannot add player data while the data manager is baked.");

        playerDataCache.add(data);
    }

    public void removePlayerData(PlayerData data) {
        if (baked) throw new IllegalStateException(MANAGER_IS_BAKED);

        playerDataCache.remove(data);
    }

    public void removePlayerData(UUID uuid) {
        if (baked) throw new IllegalStateException(MANAGER_IS_BAKED);

        playerDataCache.removeIf(data -> data.getUuid().equals(uuid));
    }

    public void removePlayerData(String playerName) {
        if (baked) throw new IllegalStateException(MANAGER_IS_BAKED);

        playerDataCache.removeIf(data -> data.getUserName().equals(playerName));
    }

    public void saveData(PlayerData data) {
        DBUser user = new DBUser(plugin.getSQLManager().establish());
        user.insert(data);
        user.close();
    }

    public void saveCacheToDB() {
        DBUser user = new DBUser(plugin.getSQLManager().establish());
        playerDataCache.forEach(user::insert);
        user.close();
    }

    @Override
    public void bake() {
        if (baked) return;

        this.playerDataCache = playerDataCache.stream().collect(Collectors.toUnmodifiableSet());

        this.baked = true;
    }

    @Override
    public void unbake() {
        if (!baked) return;

        this.playerDataCache = new HashSet<>(playerDataCache);

        this.baked = false;
    }
}
