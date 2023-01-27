package mc.unraveled.reforged.banning;

import lombok.Getter;
import lombok.SneakyThrows;
import mc.unraveled.reforged.api.Baker;
import mc.unraveled.reforged.api.Locker;
import mc.unraveled.reforged.plugin.Traverse;
import mc.unraveled.reforged.storage.DBBan;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public final class BanManager implements Locker, Baker {
    @Getter
    private final Traverse plugin;

    private Set<AbstractBan> storedBans; // This should only be reassigned by the baker.
    @Getter
    private boolean baked = false; // This should only be reassigned by the baker.

    @SneakyThrows
    public BanManager(Traverse plugin) {
        this.storedBans = new HashSet<>();
        this.plugin = plugin;

        synchronized (lock()) {
            DBBan banHandler = new DBBan(plugin.getSQLManager().establish());
            storedBans.addAll(banHandler.all());
            banHandler.close();

            lock().wait(1000);
        }

        bake();
    }

    public void insert(AbstractBan ban) {
        if (baked) throw new IllegalStateException("Cannot insert into a baked list.");

        lock().notify();
        storedBans.add(ban);
    }

    public void eject(AbstractBan ban) {
        if (baked) throw new IllegalStateException("Cannot eject from a baked list.");

        lock().notify();
        storedBans.remove(ban);
    }

    public void save() {
        if (!baked) throw new IllegalStateException("Cannot save an unbaked list.");
        lock().notify();

        DBBan banHandler = new DBBan(plugin.getSQLManager().establish());
        storedBans.forEach(banHandler::insert);
        banHandler.close();

    }

    @Override
    public void bake() {
        if (baked) return;

        lock().notify();
        storedBans = storedBans.stream().collect(Collectors.toUnmodifiableSet());

        baked = true;
    }

    @Override
    public void unbake() {
        if (!baked) return;

        lock().notify();
        storedBans = new HashSet<>(storedBans);

        baked = false;
    }
}
