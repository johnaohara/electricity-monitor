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
        registry.gauge("water.current.millilitres", this,
                WaterConverter::currentMilliLitres);
    }

    public static final int V_RMS = 240;
    private long lastTimestamp = 0;

    private AtomicLong flowrate = new AtomicLong(0);
    private AtomicLong millilitres = new AtomicLong(0);


    public void process(UtilityTimestamp utilityTimestamp) {

        try {
            Long curTimestamp = utilityTimestamp.getTimestamp();

            logger.infof("Water: Received timestamp: %s", curTimestamp);

            if (lastTimestamp == 0) {
                lastTimestamp = curTimestamp;
                return;
            }

            millilitres.incrementAndGet();
            logger.debugf("Water: Received timestamp: %s", curTimestamp);

            double diff = Double.valueOf(curTimestamp - lastTimestamp);

            logger.debugf("Water: Duration since previous timestamp: %s", diff);

            lastTimestamp = curTimestamp;

            double litresPerSecond = 460 / diff;


            logger.debugf("Water: Average Litres per Second: %s", litresPerSecond);

            this.flowrate.set((long) litresPerSecond);

            waterEmitter.send(new WaterDTO(curTimestamp, millilitres.get(), litresPerSecond));
        } catch (Exception e){
            logger.errorf("An error occured: %s", e.getMessage());
//            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "WATER";
    }


    double flowrate(){
        return flowrate.get();
    }

    double currentMilliLitres(){
        return ((double) millilitres.get());
    }

}

