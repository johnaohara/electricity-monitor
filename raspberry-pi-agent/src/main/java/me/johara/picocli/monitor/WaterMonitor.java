package me.johara.picocli.monitor;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.inject.Inject;

public class WaterMonitor extends AbstractMonitor {

    @Inject
    @Channel("water")
    Emitter<Long> impressionEmitter;

    @Override
    public Emitter getEmitter() {
        return impressionEmitter;
    }

    @Override
    public void startCallback() {
        logger.info("Starting water monitor");
    }

    @Override
    public void stopCallback() {
        logger.info("Stopping water monitor");
    }


}
