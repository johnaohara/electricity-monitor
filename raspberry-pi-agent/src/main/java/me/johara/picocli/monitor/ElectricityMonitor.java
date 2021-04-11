package me.johara.picocli.monitor;

import me.johara.picocli.dto.UtilityTimestamp;
import org.eclipse.microprofile.reactive.messaging.Emitter;

public class ElectricityMonitor extends AbstractPinStateMonitor {

    public ElectricityMonitor(Emitter<UtilityTimestamp> timestampEmitter, String pinName, int debounce, String trigger, boolean debug) {
        super(timestampEmitter, pinName, debounce, trigger, debug);
    }

    @Override
    public String getName() {
        return "ELECTRICITY";
    }


}
