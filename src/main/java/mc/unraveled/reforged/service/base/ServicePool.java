package mc.unraveled.reforged.service.base;

import mc.unraveled.reforged.api.IService;
import mc.unraveled.reforged.plugin.Traverse;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public final class ServicePool {
    private final Set<IService> services;

    private final Scheduler scheduler;

    private final String name;

    public ServicePool(String name, Traverse plugin) {
        this.services = new HashSet<>();
        this.scheduler = new ReactorBukkitScheduler(plugin);
        this.name = name;
    }

    void addService(IService service) {
        this.services.add(service);
    }

    boolean isValidService(IService service) {
        return this.services.contains(service);
    }

    public Set<IService> getServices() {
        return this.services;
    }

    public @NotNull Mono<Disposable> queue(@NotNull IService service) {
        return Mono.just(service).map(s -> {
            if (s.isPeriodic()) {
                return scheduler.schedulePeriodically(s,
                        s.getInitialDelay() * 50,
                        s.getPeriod() * 50,
                        TimeUnit.MILLISECONDS);
            } else {
                return scheduler.schedule(s, s.getInitialDelay() * 50, TimeUnit.MILLISECONDS);
            }
        });
    }

    public @NotNull Flux<Disposable> queueServices() {
        Set<Disposable> disposables = new HashSet<>();
        return Flux.fromIterable(getServices())
                .filter(Objects::nonNull)
                .doOnEach(service -> disposables.add(queue(service.get()).block()))
                .flatMap(service -> Flux.fromIterable(disposables));
    }

    public @NotNull Mono<Void> stopServices(@NotNull Flux<Disposable> disposableThread) {
        getServices().forEach(service -> service.stop().subscribe());
        return disposableThread.doOnNext(Disposable::dispose).then();
    }

    public @NotNull Mono<Void> stopService(@NotNull String service_name, @Nullable Mono<Disposable> disposable) {
        return Mono.create(sink -> {
            getService(service_name).doOnNext(IService::stop).subscribe();
            if (disposable != null) {
                disposable.doOnNext(Disposable::dispose).subscribe();
            }
            sink.success();
        });
    }

    public @NotNull Mono<IService> getService(String service_name) {
        return Flux.fromIterable(getServices())
                .filter(service -> service.getName().equals(service_name))
                .next();
    }

    void removeService(IService service) {
        getServices().remove(service);
    }

    public @NotNull Mono<ServicePool> recycle() {
        this.getServices().clear();
        return Mono.create(sink -> sink.success(this));
    }

    @Contract(pure = true)
    public Scheduler getScheduler() {
        return scheduler;
    }

    public String getName() {
        return name;
    }
}
