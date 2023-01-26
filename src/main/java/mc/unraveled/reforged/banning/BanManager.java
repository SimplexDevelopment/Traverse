package mc.unraveled.reforged.banning;

import lombok.SneakyThrows;
import mc.unraveled.reforged.api.Baker;
import mc.unraveled.reforged.api.Locker;
import mc.unraveled.reforged.plugin.Traverse;
import mc.unraveled.reforged.storage.DBBan;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public final class BanManager implements Locker, Baker {
    private final Traverse plugin;

    private Set<AbstractBan> storedBans; // This should only be reassigned by the baker.
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

        storedBans.add(ban);

        lock().notify();
    }

    public void eject(AbstractBan ban) {
        if (baked) throw new IllegalStateException("Cannot eject from a baked list.");

        storedBans.remove(ban);

        lock().notify();
    }

    @Override
    public void bake() {
        if (baked) return;

        storedBans = storedBans.stream().collect(Collectors.toUnmodifiableSet());

        baked = true;
        lock().notify();
    }

    @Override
    public void unbake() {
        if (!baked) return;

        storedBans = new HashSet<>(storedBans);

        baked = false;
        lock().notify();
    }
}
