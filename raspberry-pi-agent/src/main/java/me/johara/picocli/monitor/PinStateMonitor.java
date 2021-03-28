package me.johara.picocli.monitor;

import org.eclipse.microprofile.reactive.messaging.Emitter;

public interface PinStateMonitor<T> {

    String getName();

    Emitter<T> getEmitter();

    void initialize();

    void terminate();

    void start();

    void stop();

}
