package me.johara.picocli;

import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import org.jboss.logging.Logger;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "monitor")
public class MonitorCommand extends AbstractGpioMonitor {

    static Logger logger = Logger.getLogger(MonitorCommand.class);

    @CommandLine.Option(names = {"--broadcast"}, description = "Broadcast events to Kafka topic", defaultValue = "true")
    boolean broadcast;


//    @Inject
//    @Channel("impression")
//    Emitter<Long> impressionEmitter;

    public MonitorCommand() {
        logger.warn("Initialising MonitorCommand");
    }

    @Override
    public void eventCallback(GpioPinDigitalStateChangeEvent event) {
        logger.debug(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());

        if (event.getState().getName().equals(trigger)) {
            logger.debug("Event Matches");
            if (broadcast) {
                logger.debug("Sending Event");
//                impressionEmitter.send(System.currentTimeMillis());
            }
        } else {
            logger.debug("Event Does Not Match");
        }

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
