package mc.unraveled.reforged.service.base;

import mc.unraveled.reforged.api.IService;
import mc.unraveled.reforged.plugin.Traverse;
import org.jetbrains.annotations.NotNull;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public final class Scheduling {
    /**
     * A denominator to use when registering default service pool names.
     */
    static int denom = 0;
    /**
     * The service manager to use for controlling service pools.
     */
    private final ServiceManager serviceManager;
    /**
     * The plugin to use for registering tasks. This should be an instance of your plugin.
     */
    private final Traverse plugin;

    /**
     * Creates a new instance of the scheduling system. This is used to manage the scheduling of services.
     *
     * @param plugin The plugin to use for this scheduling system. This should be an instance of your plugin.
     */
    public Scheduling(Traverse plugin) {
        this.serviceManager = new ServiceManager();
        this.plugin = plugin;
    }

    public @NotNull Mono<ServiceManager> getServiceManager() {
        return Mono.just(serviceManager);
    }

    @NotNull
    public Mono<Disposable> queue(@NotNull IService service) {
        return getServiceManager()
                .flatMap(manager -> manager.getAssociatedServicePool(service))
                .flatMap(pool -> pool.queue(service));
    }

    public @NotNull Flux<Disposable> queueAll() {
        return getServiceManager()
                .flatMapMany(ServiceManager::getServicePools)
                .flatMap(ServicePool::queueServices);
    }

    public @NotNull Mono<Void> runOnce(IService service) {
        return Mono.create(sink -> service.start().then(service.stop()).subscribe(sink::success));
    }

    public Mono<Void> forceStop(@NotNull IService service) {
        return service.stop();
    }

    public Mono<Void> forceStart(@NotNull IService service) {
        return service.start();
    }

    public @NotNull Traverse getPlugin() {
        return plugin;
    }
}
