package me.johara.picocli.monitor;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.inject.Inject;

public class ElectricityMonitor extends AbstractPinStateMonitor {

    @Inject
    @Channel("electricity")
    Emitter<Long> electricityTimestampEmitter;

    public ElectricityMonitor() {
    }

    public ElectricityMonitor(String pinName, int debounce, String trigger, boolean debug) {
        super(pinName, debounce, trigger, debug);
    }

    @Override
    public String getName() {
        return "electricity";
    }

    @Override
    public Emitter getEmitter() {
        return electricityTimestampEmitter;
    }

}
