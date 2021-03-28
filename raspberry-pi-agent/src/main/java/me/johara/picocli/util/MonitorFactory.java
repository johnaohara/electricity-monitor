package me.johara.picocli.util;

import me.johara.picocli.monitor.ElectricityMonitor;
import me.johara.picocli.monitor.PinStateMonitor;
import me.johara.picocli.monitor.WaterMonitor;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MonitorFactory {

    Emitter<Long> timestampEmitter;

    private static final Logger logger = Logger.getLogger(MonitorFactory.class);

    @Inject
    public MonitorFactory(@Channel("electricity") Emitter<Long> timestampEmitter) {
        this.timestampEmitter = timestampEmitter;
    }

    public PinStateMonitor buildMonitor(String monitorName, String pinName, int debounce, String trigger, boolean debug){

        logger.infof("Building monitor for utility: %s\n", monitorName);

        switch (monitorName.toUpperCase()){
            case "WATER":
                return new WaterMonitor(timestampEmitter, pinName, debounce, trigger,debug);
            case "ELECTRICITY":
                return new ElectricityMonitor(timestampEmitter, pinName, debounce, trigger, debug);
            default:
                throw new IllegalStateException("Unknown utility: " + monitorName);
        }
    }
}
