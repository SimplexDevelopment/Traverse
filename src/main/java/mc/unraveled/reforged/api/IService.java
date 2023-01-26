package mc.unraveled.reforged.api;

import mc.unraveled.reforged.service.base.ServicePool;
import reactor.core.publisher.Mono;

public interface IService extends Runnable {
    Mono<ServicePool> getParentPool();

    String getName();

    int getServiceId();

    Mono<Void> start();

    Mono<Void> stop();

    boolean isPeriodic();

    long getInitialDelay();

    long getPeriod();

    @Override
    default void run() {
        start().subscribe();
    }

    Mono<Void> setParentPool(ServicePool servicePool);
}
