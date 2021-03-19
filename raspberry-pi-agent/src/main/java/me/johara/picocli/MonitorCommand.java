package me.johara.picocli;

import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import javax.inject.Inject;

@Command(name = "monitor")
public class MonitorCommand extends AbstractGpioMonitor {

    static Logger logger = Logger.getLogger(MonitorCommand.class);

//    @CommandLine.Option(names = {"--broadcast"}, description = "Broadcast events to Kafka topic", defaultValue = "true")
//    boolean broadcast;


    @Inject
    @Channel("impression")
    Emitter<Long> impressionEmitter;

    public MonitorCommand() {
        logger.warn("Initialising MonitorCommand");
    }

    @Override
    GpioPinListenerDigital getListener() {
        return event -> {
            if (event.getState().getName().equals(trigger)) {
                Long timestamp = System.currentTimeMillis();
//                if (broadcast) {
                    logger.infof("Sending Event: %ld", timestamp);
                    impressionEmitter.send(timestamp);
//                } else {
//                    logger.debugf("Event NOT BROADCAST: %ld", timestamp);
//                }
            }
        };
    }

    @Override
    public void startCallback() {
        logger.info("Starting Monitoring");
    }

    @Override
    public void endCallback() {
        logger.info("Shutting down GPIO controller");
    }


}
