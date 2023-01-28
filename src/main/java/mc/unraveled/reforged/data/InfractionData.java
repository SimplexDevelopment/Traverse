package mc.unraveled.reforged.data;

import lombok.Getter;
import lombok.Setter;
import mc.unraveled.reforged.plugin.Traverse;
import mc.unraveled.reforged.storage.DBInfraction;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class InfractionData {
    private static final Map<OfflinePlayer, InfractionData> playerInfractionDataMap = new HashMap<>() {{
        DBInfraction infraction = new DBInfraction(JavaPlugin.getPlugin(Traverse.class).getSQLManager().establish());
        for (InfractionData data : infraction.getStoredInfractionsFromUUID()) {
            put(data.getPlayer(), data);
        }
    }};

    @Getter
    private final OfflinePlayer player;
    @Getter
    private int infractions = 0;

    @Getter
    @Setter
    private boolean muted = false;
    @Getter
    @Setter
    private boolean frozen = false;
    @Getter
    @Setter
    private boolean locked = false;
    @Getter
    @Setter
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

    public void increment() {
        infractions++;
    }

    public void decrement() {
        infractions--;
    }
}
