package mc.unraveled.reforged.banning;

import mc.unraveled.reforged.api.Baker;
import mc.unraveled.reforged.plugin.Traverse;
import mc.unraveled.reforged.storage.DBBan;
import org.bukkit.OfflinePlayer;

import java.sql.Date;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public final class BanManager implements Baker {
    private final Traverse plugin;

    private Set<AbstractBan> storedBans; // This should only be reassigned by the baker.
    private boolean baked = false; // This should only be reassigned by the baker.

    public BanManager(Traverse plugin) {
        this.storedBans = new HashSet<>();
        this.plugin = plugin;

        DBBan banHandler = new DBBan(plugin.getSQLManager().establish());
        storedBans.addAll(banHandler.all());
        banHandler.close();

        bake();
    }

    public Traverse getPlugin() {
        return plugin;
    }

    public boolean isBaked() {
        return baked;
    }

    public void insert(AbstractBan ban) {
        if (baked) throw new IllegalStateException("Cannot insert into a baked list.");

        storedBans.add(ban);
        DBBan db = new DBBan(plugin.getSQLManager().establish());
        db.insert(ban);
        db.close();
    }

    public void eject(AbstractBan ban) {
        if (baked) throw new IllegalStateException("Cannot eject from a baked list.");

        storedBans.remove(ban);
        DBBan db = new DBBan(plugin.getSQLManager().establish());
        db.delete(ban);
        db.close();
    }

    public AbstractBan getBan(OfflinePlayer player) {
        DBBan db = new DBBan(plugin.getSQLManager().establish());
        AbstractBan ban = db.getBan(player.getUniqueId());
        db.close();
        return ban;
    }

    public boolean isBanned(OfflinePlayer player) {
        return storedBans.stream()
                .filter(AbstractBan::isActive)
                .filter(ban -> {
                    Date date = new Date(ban.getExpiry());
                    if (date.before(Date.from(Instant.now()))) {
                        eject(ban);
                        return false;
                    } else {
                        return true;
                    }
                })
                .anyMatch(ban -> ban.getUuid().equalsIgnoreCase(
                        player.getUniqueId().toString()));
    }

    public void save() {
        if (!baked) throw new IllegalStateException("Cannot save an unbaked list.");

        DBBan banHandler = new DBBan(plugin.getSQLManager().establish());
        storedBans.forEach(banHandler::insert);
        banHandler.close();
    }

    @Override
    public void bake() {
        if (baked) return;

        storedBans = storedBans.stream().collect(Collectors.toUnmodifiableSet());

        baked = true;
    }

    @Override
    public void unbake() {
        if (!baked) return;

        storedBans = new HashSet<>(storedBans);

        baked = false;
    }
}
