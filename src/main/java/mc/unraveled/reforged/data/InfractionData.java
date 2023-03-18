package mc.unraveled.reforged.data;

import mc.unraveled.reforged.plugin.Traverse;
import mc.unraveled.reforged.storage.DBInfraction;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class InfractionData {
    private static final Map<OfflinePlayer, InfractionData> playerInfractionDataMap = new HashMap<>();

    private final OfflinePlayer player;
    private int infractions = 0;
    private boolean muted = false;
    private boolean frozen = false;
    private boolean locked = false;
    private boolean jailed = false;

    public InfractionData(OfflinePlayer player) {
        this.player = player;
    }

    public InfractionData(OfflinePlayer player, int infractions, boolean muted, boolean frozen, boolean locked, boolean jailed) {
        this.player = player.getPlayer();
        this.infractions = infractions;
        this.muted = muted;
        this.frozen = frozen;
        this.locked = locked;
        this.jailed = jailed;

        DBInfraction infraction = new DBInfraction(JavaPlugin.getPlugin(Traverse.class).getSQLManager().establish());
        for (InfractionData data : infraction.getInfractionsList()) {
            playerInfractionDataMap.put(data.getPlayer(), data);
        }
    }

    public static InfractionData getCachedInfractionData(OfflinePlayer player) {
        return playerInfractionDataMap.computeIfAbsent(player, InfractionData::new);
    }

    public static InfractionData getInfractionFromDB(@NotNull OfflinePlayer player) {
        DBInfraction infraction = new DBInfraction(JavaPlugin.getPlugin(Traverse.class).getSQLManager().establish());
        InfractionData data = infraction.getInfraction(player.getUniqueId().toString());
        infraction.close();
        return data;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    public int getInfractions() {
        return infractions;
    }

    public void setInfractions(int infractions) {
        this.infractions = infractions;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isJailed() {
        return jailed;
    }

    public void setJailed(boolean jailed) {
        this.jailed = jailed;
    }

    public void increment() {
        infractions++;
    }

    public void decrement() {
        infractions--;
    }
}
