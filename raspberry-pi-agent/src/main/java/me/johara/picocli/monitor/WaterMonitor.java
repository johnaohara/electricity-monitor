package me.johara.picocli.monitor;

import me.johara.picocli.dto.UtilityTimestamp;
import org.eclipse.microprofile.reactive.messaging.Emitter;

public class WaterMonitor extends AbstractPinStateMonitor {

    public WaterMonitor(Emitter<UtilityTimestamp> timestampEmitter, String pinName, int debounce, String trigger, boolean debug) {
        super(timestampEmitter, pinName, debounce, trigger, debug);
    }

    @Override
    public String getName() {
        return "WATER";
    }

}
