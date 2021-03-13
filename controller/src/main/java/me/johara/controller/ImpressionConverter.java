package me.johara.controller;

import io.micrometer.core.instrument.MeterRegistry;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import me.johara.dto.ElectricityDTO;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class ImpressionConverter {

    Logger logger = Logger.getLogger(ImpressionConverter.class);

    private long lastTimestamp = 0;

    private final MeterRegistry registry;

    private AtomicLong currentWatts = new AtomicLong(0);
    private AtomicLong currentMilliAmps = new AtomicLong(0);

    @Inject
    ImpressionConverter(MeterRegistry registry) {
        this.registry = registry;

        registry.gauge("electricity.current.watts", this,
                ImpressionConverter::currentWatts);
        registry.gauge("electricity.current.milliamps", this,
                ImpressionConverter::currentAmps);
    }


    @Incoming("impressions")
    @Outgoing("watts")
    @Broadcast
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING)
    public ElectricityDTO process(long timestamp) {

        if(lastTimestamp == 0){
            lastTimestamp = timestamp;
            return null;
        }

        logger.debugf("Received timestamp: %s", timestamp);

        double diff = Double.valueOf(timestamp - lastTimestamp);

        logger.debugf("Duration since previous timestamp: %s", diff);

        lastTimestamp = timestamp;

        double watts = 3_600_000 / diff ;

        double amps = watts / 240;

        logger.debugf("Average Watts: %s", watts);
        logger.debugf("Average Amps: %s", amps);

        currentWatts.set((long) watts);
        currentMilliAmps.set((long) (amps * 1000));

        return new ElectricityDTO(timestamp, watts, amps);
    }


    double currentWatts(){
        return currentWatts.get();
    }

    double currentAmps(){
//        return ((double)currentMilliAmps.get()) / 1000.0d;
        return ((double)currentMilliAmps.get());
    }

}
