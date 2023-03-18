package mc.unraveled.reforged.service.base;


import mc.unraveled.reforged.api.IService;
import mc.unraveled.reforged.plugin.Traverse;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

import java.util.Objects;

public abstract class AbstractService implements IService {
    /**
     * The name of the service.
     */
    private final String service_name;
    /**
     * How long the service should wait before executing the first time.
     */
    private final long delay;
    /**
     * How long the service should wait between executions.
     */
    private final long period;
    /**
     * If the service should be executed once or continuously.
     */
    private final boolean repeating;
    /**
     * If the service should be allowed to stop while executing.
     */
    private final boolean mayInterruptWhenRunning;
    /**
     * The service's execution thread.
     */
    private ServicePool parentPool;
    /**
     * Whether the service has been cancelled or not.
     */
    private boolean cancelled = false;

    /**
     * Creates a new instance of an executable service.
     * Each service is registered with a {@link String},
     * to allow for easy identification within the associated {@link ServicePool}.
     *
     * @param service_name A namespaced key which can be used to identify the service.
     */
    protected AbstractService(@NotNull String service_name) {
        this((new ServicePool("defaultPool" + Scheduling.denom, JavaPlugin.getPlugin(Traverse.class))),
                service_name,
                0L,
                0L,
                false,
                false);

        Scheduling.denom++;
    }

    /**
     * Creates a new instance of an executable service.
     * Each service is registered with a {@link String},
     * to allow for easy identification within the associated {@link ServicePool}.
     *
     * @param parentPool   The {@link ServicePool} which this service is executing on.
     * @param service_name A namespaced key which can be used to identify the service.
     */
    public AbstractService(@Nullable ServicePool parentPool, @NotNull String service_name) {
        this(parentPool,
                service_name,
                0L,
                0L,
                false,
                false);
    }

    /**
     * Creates a new instance of an executable service.
     * The timings are measured in ticks (20 ticks per second).
     * You do not need to explicitly define a delay.
     * Each service is registered with a {@link String},
     * to allow for easy identification within the associated {@link ServicePool}.
     *
     * @param parentPool   The {@link ServicePool} which this service is executing on.
     * @param service_name A namespaced key which can be used to identify the service.
     * @param delay        A specified amount of time (in ticks) to wait before the service runs.
     */
    public AbstractService(
            @Nullable ServicePool parentPool,
            @NotNull String service_name,
            @Nullable Long delay) {
        this(parentPool,
                service_name,
                delay,
                0L,
                false,
                false);
    }

    /**
     * Creates a new instance of an executable service.
     * The timings are measured in ticks (20 ticks per second).
     * You do not need to explicitly define a delay or a period,
     * however if you have flagged {@link #repeating} as true, and the period is null,
     * then the period will automatically be set to 20 minutes.
     * Each service is registered with a {@link String},
     * to allow for easy identification within the associated {@link ServicePool}.
     *
     * @param parentPool   The {@link ServicePool} which this service is executing on.
     * @param service_name A namespaced key which can be used to identify the service.
     * @param delay        A specified amount of time (in ticks) to wait before the service runs.
     * @param period       How long the service should wait between service executions (in ticks).
     * @param repeating    If the service should be scheduled for repeated executions or not.
     */
    public AbstractService(
            @Nullable ServicePool parentPool,
            @NotNull String service_name,
            @NotNull Long delay,
            @NotNull Long period,
            @NotNull Boolean repeating) {
        this(parentPool,
                service_name,
                delay, period,
                repeating,
                false);
    }

    /**
     * Creates a new instance of an executable service.
     * The timings are measured in ticks (20 ticks per second).
     * You do not need to explicitly define a delay or a period,
     * however if you have flagged {@link #repeating} as true, and the period is null,
     * then the period will automatically be set to 20 minutes.
     * Each service is registered with a {@link String},
     * to allow for easy identification within the associated {@link ServicePool}.
     *
     * @param parentPool              The {@link ServicePool} which this service is executing on.
     * @param service_name            A namespaced key which can be used to identify the service.
     * @param delay                   A specified amount of time (in ticks) to wait before the service runs.
     * @param period                  How long the service should wait between service executions (in ticks).
     * @param repeating               If the service should be scheduled for repeated executions or not.
     * @param mayInterruptWhenRunning If the service can be cancelled during execution.
     */
    public AbstractService(
            @Nullable ServicePool parentPool,
            @NotNull String service_name,
            @Nullable Long delay,
            @Nullable Long period,
            @NotNull Boolean repeating,
            @NotNull Boolean mayInterruptWhenRunning) {
        this.service_name = service_name;
        this.repeating = repeating;
        this.delay = Objects.requireNonNullElse(delay, 0L);
        this.period = Objects.requireNonNullElse(period, (20L * 60L) * 20L);
        this.mayInterruptWhenRunning = mayInterruptWhenRunning;

        if (parentPool == null) {
            this.parentPool = new ServicePool("defaultPool" + Scheduling.denom, JavaPlugin.getPlugin(Traverse.class));
            Scheduling.denom++;
        } else {
            this.parentPool = parentPool;
        }

        this.parentPool.getServices().add(this);
    }

    @Override
    public long getInitialDelay() {
        return delay;
    }

    @Override
    public long getPeriod() {
        return period;
    }

    @Override
    public boolean isPeriodic() {
        return repeating;
    }

    /**
     * Cancels the execution of this service.
     *
     * @return true if the service was cancelled, false if not.
     */
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Cancels the execution of this service.
     *
     * @param cancel Whether the service should be cancelled or not.
     */
    public Mono<Void> setCancelled(boolean cancel) {
        if (!mayInterruptWhenRunning) {
            return Mono.empty();
        }

        cancelled = cancel;
        return cancel();
    }

    /**
     * Actual stop call, to ensure that the service actually #isCancelled().
     */
    @Contract(pure = true)
    Mono<Void> cancel() {
        if (isCancelled()) {
            return stop().then();
        }
        return Mono.empty();
    }

    @Override
    public Mono<ServicePool> getParentPool() {
        return Mono.just(parentPool);
    }

    @Override
    public String getName() {
        return service_name;
    }

    @Override
    public Mono<Void> setParentPool(ServicePool servicePool) {
        return Mono.create(sink -> {
            this.parentPool = servicePool;
            sink.success();
        });
    }
}
