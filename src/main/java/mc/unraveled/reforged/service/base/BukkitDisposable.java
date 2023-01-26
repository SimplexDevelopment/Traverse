package mc.unraveled.reforged.service.base;

import org.bukkit.scheduler.BukkitTask;
import reactor.core.Disposable;

public record BukkitDisposable(BukkitTask task) implements Disposable {
    /**
     * Disposes of the task upstream on the Bukkit scheduler.
     */
    @Override
    public void dispose() {
        task.cancel();
    }

    /**
     * Checks if the task is cancelled.
     *
     * @return true if the task is cancelled, false otherwise.
     */
    @Override
    public boolean isDisposed() {
        return task.isCancelled();
    }
}

