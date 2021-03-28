package me.johara.picocli.util;

import me.johara.picocli.monitor.ElectricityMonitor;
import me.johara.picocli.monitor.PinStateMonitor;
import me.johara.picocli.monitor.WaterMonitor;
import org.jboss.logging.Logger;

public class MonitorFactory {

    private static final Logger logger = Logger.getLogger(MonitorFactory.class);

    public static PinStateMonitor buildMonitor(String monitorName, String pinName, int debounce, String trigger, boolean debug){

        logger.infof("Building monitor for utility: %s\n", monitorName);

        switch (monitorName.toUpperCase()){
            case "WATER":
                return new WaterMonitor(pinName, debounce, trigger,debug);
            case "ELECTRICITY":
                return new ElectricityMonitor(pinName, debounce, trigger, debug);
            default:
                throw new IllegalStateException("Unknown utility: " + monitorName);
        }
    }
}
