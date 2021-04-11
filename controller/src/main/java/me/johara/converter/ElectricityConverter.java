package me.johara.converter;

import io.micrometer.core.instrument.MeterRegistry;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import me.johara.dto.ElectricityDTO;
import me.johara.dto.UtilityTimestamp;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.atomic.AtomicLong;

@Singleton
public class ElectricityConverter implements UtilityConverter {

    @Inject
    @Channel("electricity")
    @Broadcast
    Emitter<ElectricityDTO> timestampEmitter;

    Logger logger = Logger.getLogger(ElectricityConverter.class);

    public static final int V_RMS = 240;
    private long lastTimestamp = 0;

    private AtomicLong watts = new AtomicLong(0);
    private AtomicLong milliAmps = new AtomicLong(0);
    private AtomicLong wattHours = new AtomicLong(0);

    public ElectricityConverter() {
    }

    @Override
    public void registerMetrics(MeterRegistry registry) {

        registry.gauge("electricity.current.watts", this,
                ElectricityConverter::currentWatts);
        registry.gauge("electricity.current.milliamps", this,
                ElectricityConverter::currentAmps);
        registry.gauge("electricity.current.wattHours", this,
                ElectricityConverter::currentWattHours);
    }

    @Override
    public void process(UtilityTimestamp utilityTimestamp) {

        Long curTimestamp = utilityTimestamp.getTimestamp();

        logger.infof("Received timestamp: %s", curTimestamp);

        if(lastTimestamp == 0){
            lastTimestamp = curTimestamp;
            return;
        }

        wattHours.incrementAndGet();
        logger.debugf("Received timestamp: %s", curTimestamp);

        double diff = Double.valueOf(curTimestamp - lastTimestamp);

        logger.debugf("Duration since previous timestamp: %s", diff);

        lastTimestamp = curTimestamp;

        double watts = 3_600_000 / diff ;

        double amps = watts / V_RMS;

        logger.debugf("Average Watts: %s", watts);
        logger.debugf("Average Amps: %s", amps);

        this.watts.set((long) watts);
        milliAmps.set((long) (amps * 1000));

        timestampEmitter.send(new ElectricityDTO(curTimestamp, watts, amps, wattHours.get()));
    }

    @Override
    public String getName() {
        return "ELECTRICITY";
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
