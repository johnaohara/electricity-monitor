package me.johara.picocli;

import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.jboss.logging.Logger;
import picocli.CommandLine.Command;

@Command(name = "debug")
public class DebugCommand extends AbstractGpioMonitor {

    static Logger logger = Logger.getLogger(DebugCommand.class);

    public DebugCommand() {
        logger.warn("Initialising DebugCommand");
    }

    @Override
    GpioPinListenerDigital getListener(){
        return event -> {
            if (event.getState().getName().equals(trigger)) {
                logger.info(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
            }
        };
    }

//    @Override
    public void eventCallback(GpioPinDigitalStateChangeEvent event) {
        logger.debug(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
    }

//    @Override
    public void startCallback() {
        logger.info("Starting DEBUG Monitoring");
    }

//    @Override
    public void stopCallback() {
        logger.info("Shutting down GPIO controller");
    }
}
