package mc.unraveled.reforged.service;

import mc.unraveled.reforged.data.InfractionData;
import mc.unraveled.reforged.service.base.AbstractService;
import mc.unraveled.reforged.service.base.ServicePool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

public class MuteService extends AbstractService {
    private InfractionData infractionData = null;

    public MuteService(@Nullable ServicePool parentPool, @NotNull String service_name, long delay) {
        super(parentPool, service_name, delay);
    }

    public void setInfractionData(InfractionData infractionData) {
        this.infractionData = infractionData;
    }

    @Override
    public int getServiceId() {
        return 0;
    }

    @Override
    public Mono<Void> start() {
        if (infractionData == null) return Mono.empty();

        if (infractionData.isMuted()) return Mono.empty();

        return Mono.create(sink -> {
            infractionData.setMuted(false);
            sink.success();
        });
    }

    @Override
    public Mono<Void> stop() {
        return Mono.empty();
    }
}
