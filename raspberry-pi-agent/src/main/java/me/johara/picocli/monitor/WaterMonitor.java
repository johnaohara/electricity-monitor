package me.johara.picocli.monitor;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.inject.Inject;

public class WaterMonitor extends AbstractPinStateMonitor {

//    @Inject
//    @Channel("water")
    Emitter<Long> waterEmitter;

    public WaterMonitor() {
    }

    public WaterMonitor(String pinName, int debounce, String trigger, boolean debug) {
        super(pinName, debounce, trigger, debug);
    }


    @Override
    public String getName() {
        return "water";
    }

    @Override
    public Emitter getEmitter() {
        return waterEmitter;
    }

}
