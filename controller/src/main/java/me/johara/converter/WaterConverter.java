package me.johara.converter;

import io.micrometer.core.instrument.MeterRegistry;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import me.johara.dto.UtilityTimestamp;
import me.johara.dto.WaterDTO;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.atomic.AtomicLong;

@Singleton
public class WaterConverter implements UtilityConverter {

    @Inject
    @Channel("water")
    @Broadcast
    Emitter<WaterDTO> waterEmitter;

    Logger logger = Logger.getLogger(WaterConverter.class);

    public WaterConverter() {
    }

    @Override
    public void registerMetrics(MeterRegistry registry) {
        registry.gauge("water.current.flowRate", this,
                WaterConverter::flowrate);
        registry.gauge("water.current.milliLitres", this,
                WaterConverter::currentMilliLitres);
    }

    private AtomicLong lastTimestamp = new AtomicLong(0);
    private static final Double ONE_LITRE_PER_MIN_MS_DELTA = 60_000d / 450d;
    private static final Double ML_PER_PULSE = 1_000d / 450d;

    private AtomicLong flowrate = new AtomicLong(0);
    private AtomicLong pulses = new AtomicLong(0);

    public void process(UtilityTimestamp utilityTimestamp) {

        try {
            Long curTimestamp = utilityTimestamp.getTimestamp();

            logger.debugf("Water: Received timestamp: %s", curTimestamp);

            //first pulse
            if (lastTimestamp.get() == 0) {
                lastTimestamp.set(curTimestamp);
                return;
            }

            pulses.incrementAndGet();

            double diff = Double.valueOf(curTimestamp - lastTimestamp.get());

            logger.debugf("Water: Duration since previous timestamp: %s", diff);

            lastTimestamp.set(curTimestamp);

            double litresPerMinute = ONE_LITRE_PER_MIN_MS_DELTA / diff;

            logger.debugf("Water: Average Litres per Minute: %s", litresPerMinute);

            this.flowrate.set(Double.doubleToLongBits(litresPerMinute));

            waterEmitter.send(new WaterDTO(curTimestamp, getMilliLiters(), litresPerMinute));

        } catch (Exception e) {
            logger.errorf("An error occured: %s", e.getMessage());
//            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "WATER";
    }


    double flowrate() {
        return Double.longBitsToDouble(flowrate.get());
    }

    double currentMilliLitres() {
        Double curMl = getMilliLiters();
        //Double curMl = (Double.valueOf(pulses.get()));
        pulses.set(0);
        return curMl;
    }

    double getMilliLiters() {
        return (Double.valueOf(pulses.get()) * ML_PER_PULSE);
    }
}

