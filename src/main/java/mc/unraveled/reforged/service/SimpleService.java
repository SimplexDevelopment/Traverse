package mc.unraveled.reforged.service;

import mc.unraveled.reforged.service.base.AbstractService;
import mc.unraveled.reforged.service.base.ServicePool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

public class SimpleService extends AbstractService {
    public SimpleService(@Nullable ServicePool parentPool, @NotNull String service_name) {
        super(parentPool, service_name);
    }

    @Override
    public int getServiceId() {
        return 0;
    }

    @Override
    public Mono<Void> start() {
        return null;
    }

    @Override
    public Mono<Void> stop() {
        return null;
    }
}
