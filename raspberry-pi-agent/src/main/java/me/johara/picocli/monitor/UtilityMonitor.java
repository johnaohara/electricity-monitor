package me.johara.picocli.monitor;

import org.eclipse.microprofile.reactive.messaging.Emitter;

public interface UtilityMonitor<T> {

    Emitter<T> getEmitter();
}
