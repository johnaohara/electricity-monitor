package me.johara.picocli.util;

import me.johara.picocli.monitor.ElectricityMonitor;
import me.johara.picocli.monitor.PinStateMonitor;
import me.johara.picocli.monitor.WaterMonitor;

public class MonitorFactory {

    public static PinStateMonitor buildMonitor(String monitorName, String pinName, int debounce, String trigger, boolean debug){
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
