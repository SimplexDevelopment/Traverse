package mc.unraveled.reforged.data;

import lombok.SneakyThrows;
import mc.unraveled.reforged.api.Baker;
import mc.unraveled.reforged.api.Locker;
import mc.unraveled.reforged.plugin.Traverse;
import mc.unraveled.reforged.storage.DBUser;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public final class DataManager implements Baker, Locker {
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

    public void addPlayerData(PlayerData data) {
        if (baked) throw new IllegalStateException("Cannot add player data while the data manager is baked.");

        playerDataCache.add(data);
    }

    public void removePlayerData(PlayerData data) {
        if (baked) throw new IllegalStateException("Cannot remove player data while the data manager is baked.");

        playerDataCache.remove(data);
    }

    public void removePlayerData(UUID uuid) {
        if (baked) throw new IllegalStateException("Cannot remove player data while the data manager is baked.");

        playerDataCache.removeIf(data -> data.getUuid().equals(uuid));
    }

    public void removePlayerData(String playerName) {
        if (baked) throw new IllegalStateException("Cannot remove player data while the data manager is baked.");

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

    @SneakyThrows
    @Override
    public void bake() {
        if (baked) return;

        synchronized (lock()) {
            this.playerDataCache = playerDataCache.stream().collect(Collectors.toUnmodifiableSet());
            lock().wait(1000);
        }

        this.baked = true;
        lock().notify();
    }

    @SneakyThrows
    @Override
    public void unbake() {
        if (!baked) return;

        synchronized (lock()) {
            this.playerDataCache = new HashSet<>(playerDataCache);
            lock().wait(1000);
        }

        this.baked = false;
        lock().notify();
    }
}
