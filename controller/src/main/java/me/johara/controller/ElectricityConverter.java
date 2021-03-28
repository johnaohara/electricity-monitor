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
public class ElectricityConverter {

    Logger logger = Logger.getLogger(ElectricityConverter.class);

    public static final int V_RMS = 240;
    private long lastTimestamp = 0;

    private final MeterRegistry registry;

    private AtomicLong watts = new AtomicLong(0);
    private AtomicLong milliAmps = new AtomicLong(0);
    private AtomicLong wattHours = new AtomicLong(0);

    @Inject
    ElectricityConverter(MeterRegistry registry) {
        this.registry = registry;

        registry.gauge("electricity.current.watts", this,
                ElectricityConverter::currentWatts);
        registry.gauge("electricity.current.milliamps", this,
                ElectricityConverter::currentAmps);
        registry.gauge("electricity.current.wattHours", this,
                ElectricityConverter::currentWattHours);
    }


    @Incoming("electricity")
    @Outgoing("watts")
    @Broadcast
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING)
    public ElectricityDTO process(long timestamp) {

        if(lastTimestamp == 0){
            lastTimestamp = timestamp;
            return null;
        }

        wattHours.incrementAndGet();
        logger.debugf("Received timestamp: %s", timestamp);

        double diff = Double.valueOf(timestamp - lastTimestamp);

        logger.debugf("Duration since previous timestamp: %s", diff);

        lastTimestamp = timestamp;

        double watts = 3_600_000 / diff ;

        double amps = watts / V_RMS;

        logger.debugf("Average Watts: %s", watts);
        logger.debugf("Average Amps: %s", amps);

        this.watts.set((long) watts);
        milliAmps.set((long) (amps * 1000));

        return new ElectricityDTO(timestamp, watts, amps, wattHours.get());
    }


    double currentWatts(){
        return watts.get();
    }

    long currentWattHours(){
        long curWattHours = wattHours.get();
        wattHours.set(0);
        return curWattHours;
    }

    double currentAmps(){
        return ((double) milliAmps.get());
    }

}
