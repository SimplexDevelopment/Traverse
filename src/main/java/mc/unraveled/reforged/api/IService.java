package mc.unraveled.reforged.api;

import mc.unraveled.reforged.service.base.ServicePool;
import org.apache.commons.lang3.ArrayUtils;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

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
