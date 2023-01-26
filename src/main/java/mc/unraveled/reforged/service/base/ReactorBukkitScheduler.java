package mc.unraveled.reforged.service.base;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.Disposable;
import reactor.core.scheduler.Scheduler;

import java.util.concurrent.TimeUnit;

public final class ReactorBukkitScheduler
        implements Scheduler, Scheduler.Worker {
    /**
     * The plugin instance.
     */
    private final JavaPlugin plugin;
    /**
     * The bukkit scheduler.
     */
    private final BukkitScheduler scheduler;

    public ReactorBukkitScheduler(JavaPlugin plugin) {
        this.plugin = plugin;
        this.scheduler = plugin.getServer().getScheduler();
    }

    /**
     * Delegates to the {@link BukkitScheduler}.
     *
     * @param task The task to delegate.
     * @return A disposable that can be used to cancel the task.
     */
    @Override
    public @NotNull Disposable schedule(@NotNull Runnable task) {
        return new BukkitDisposable(scheduler.runTask(plugin, task));
    }

    /**
     * Delegates to the {@link BukkitScheduler} with a delay.
     *
     * @param task  The task to delegate
     * @param delay The amount of time to wait before running the task
     * @param unit  Unused parameter in this implementation.
     *              Regardless of what value you use, this parameter will never be called.
     * @return A disposable that can be used to cancel the task.
     */
    @Override
    public @NotNull Disposable schedule(@NotNull Runnable task, long delay, @Deprecated @Nullable TimeUnit unit) {
        return new BukkitDisposable(scheduler.runTaskLater(plugin, task, delay));
    }

    /**
     * Delegates to the {@link BukkitScheduler} with a delay and a period.
     * The initial delay may be 0L, but the period must be greater than 0L.
     *
     * @param task         The task to delegate.
     * @param initialDelay The amount of time to wait before running the task.
     * @param period       The amount of time to wait between each execution of the task.
     * @param unit         Unused parameter in this implementation.
     *                     Regardless of what value you use, this parameter will never be called.
     * @return A disposable that can be used to cancel the task.
     */
    @Override
    public @NotNull Disposable schedulePeriodically(@NotNull Runnable task, long initialDelay, long period, @Deprecated @Nullable TimeUnit unit) {
        if (period <= 0L) {
            throw new IllegalArgumentException("Period must be greater than 0L");
        }

        return new BukkitDisposable(scheduler.runTaskTimer(plugin, task, initialDelay, period));
    }

    /**
     * A new {@link Worker}.
     *
     * @return This class instance, as it implements {@link Worker}.
     */
    @Override
    public @NotNull Scheduler.Worker createWorker() {
        return this;
    }

    /**
     * This method does nothing and is unused.
     */
    @Override
    @Deprecated
    public void dispose() {
    }
}
