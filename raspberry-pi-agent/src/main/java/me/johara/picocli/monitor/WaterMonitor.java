package me.johara.picocli.monitor;

import org.eclipse.microprofile.reactive.messaging.Emitter;

public class WaterMonitor extends AbstractPinStateMonitor {

    public WaterMonitor(Emitter<Long> timestampEmitter, String pinName, int debounce, String trigger, boolean debug) {
        super(timestampEmitter, pinName, debounce, trigger, debug);
    }

    @Override
    public String getName() {
        return "water";
    }

}
