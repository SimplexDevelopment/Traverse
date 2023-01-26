package mc.unraveled.reforged.service.base;

import mc.unraveled.reforged.api.IService;
import mc.unraveled.reforged.plugin.Traverse;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ServiceManager {
    /**
     * A set of {@link ServicePool}s which are currently active.
     */
    private final Set<ServicePool> servicePools;

    /**
     * Creates a new instance of the Service Manager class.
     * This class acts as a Service Pool factory, and can be used to create
     * both single and multithreaded Service Pools, empty service pools, as well as
     * retrieve existing Service Pools. It also provides methods for you to add and remove
     * {@link IService}s from the {ServicePool} parameter.
     */
    public ServiceManager() {
        servicePools = new HashSet<>();
    }

    /**
     * @param poolName The name of the service pool.
     * @param plugin   The plugin which will be used to register the service pool.
     * @return A {@link Mono} object which contains a {@link ServicePool} element.
     * This Service Pool will execute each service within the main server thread.
     */
    @Contract(pure = true, value = "_, _ -> new")
    public @NotNull Mono<ServicePool> emptyBukkitServicePool(String poolName, Traverse plugin) {
        ServicePool pool = new ServicePool(poolName, plugin);
        servicePools.add(pool);
        return Mono.just(pool);

    }

    /**
     * @param poolName The name of the service pool.
     * @param plugin   The plugin which will be used to register the service pool.
     * @param services The services to register within the service pool.
     * @return A {@link Mono} object which contains a {@link ServicePool} element.
     * This Service Pool will execute each service within the main server thread.
     */
    @Contract(pure = true, value = "_, _, _ -> new")
    public @NotNull Mono<ServicePool> bukkitServicePool(String poolName, Traverse plugin, IService... services) {
        ServicePool pool = new ServicePool(poolName, plugin);
        Flux.fromIterable(Arrays.asList(services)).doOnEach(s -> pool.addService(s.get()));
        servicePools.add(pool);
        return Mono.just(pool);
    }

    /**
     * Adds a service to an existing service pool.
     *
     * @param poolName     The service pool to add to.
     * @param services The services to register within the service pool.
     * @return A {@link Mono} object which contains the {@link ServicePool} element that now contains the registered services.
     */
    @Contract("_, _ -> new")
    public @NotNull Mono<ServicePool> addToExistingPool(@NotNull String poolName, IService... services) {
        return Mono.create(sink -> {
            final ServicePool[] servicePool = new ServicePool[1];
            findPool(poolName).subscribe(pool -> {
                if (pool == null) throw new RuntimeException("There is no pool currently registered with that name.");
                servicePool[0] = pool;
            });
            List<IService> serviceList = Arrays.asList(services);
            Flux.fromIterable(serviceList).doOnEach(s -> servicePool[0].addService(s.get()));
            sink.success(servicePool[0]);
        });
    }

    /**
     * Finds a {@link ServicePool} within the ServiceManager's pool list.
     *
     * @param poolName The name of the pool.
     * @return A Mono object which holds the requested ServicePool, or an empty Mono if the pool does not exist.
     */
    @Contract()
    public @NotNull Mono<ServicePool> findPool(String poolName) {
        return getServicePools().filter(pool -> pool.getName().equalsIgnoreCase(poolName)).next();
    }

    /**
     * @param pool     The service pool to take from.
     * @param services The services to remove from the pool.
     * @return A {@link Mono} object which contains the {@link ServicePool} that no longer contains the removed services.
     */
    @Contract("_, _ -> new")
    public @NotNull Mono<ServicePool> takeFromExistingPool(@NotNull ServicePool pool, IService... services) {
        Flux.fromIterable(Arrays.asList(services)).doOnEach(s -> {
            pool.removeService(s.get());
        });
        return Mono.just(pool);
    }

    /**
     * @return A {@link Flux} object which contains all the service pools currently available.
     */
    @Contract(" -> new")
    public @NotNull Flux<ServicePool> getServicePools() {
        return Flux.fromIterable(servicePools);
    }

    /**
     * @param service The service to locate.
     * @return True if the service is somewhere within a service pool, false otherwise.
     */
    @Contract(pure = true)
    public boolean locateServiceWithinPools(IService service) {
        return servicePools.stream().map(p -> p.isValidService(service)).findFirst().orElseGet(() -> false);
    }

    /**
     * @param service The service pool to call from.
     * @return A {@link Mono} object which contains a {@link ServicePool} element which contains the specified service.
     * If no service pool can be found, an empty Mono is returned.
     */
    @Contract("_ -> new")
    public @NotNull Mono<ServicePool> getAssociatedServicePool(IService service) {
        if (!locateServiceWithinPools(service)) return Mono.empty();
        return getServicePools()
                .filter(p -> p.getServices().contains(service))
                .next();
    }
}
