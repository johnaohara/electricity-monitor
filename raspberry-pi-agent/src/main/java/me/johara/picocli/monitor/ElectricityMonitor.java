package me.johara.picocli.monitor;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

import javax.inject.Inject;

public class ElectricityMonitor extends AbstractMonitor {

    Logger logger =  Logger.getLogger(ElectricityMonitor.class);

    @Inject
    @Channel("electricity")
    Emitter<Long> electricityTimestampEmitter;

    @Override
    public Emitter getEmitter() {
        return electricityTimestampEmitter;
    }

    @Override
    public void startCallback() {
        logger.info("Starting electricity monitor");
    }

    @Override
    public void stopCallback() {
        logger.info("Stopping electricity monitor");
    }

}
