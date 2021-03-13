package me.johara.picocli;

import com.pi4j.io.gpio.RaspiPin;
import me.johara.picocli.util.Raspberry;
import org.jboss.logging.Logger;
import picocli.CommandLine.Command;

import java.util.Arrays;

@Command(name = "pins")
public class PinsCommand implements Runnable {

    static Logger logger = Logger.getLogger(PinsCommand.class);

    @Override
    public void run() {

        if (!Raspberry.isPi()) {
            logger.error("Can not initialise Pi4J.  Platform is not Raspberry Pi!");
        } else {

            logger.info("Enumerating available pins");

            Arrays.stream(RaspiPin.allPins()).forEach(pin -> logger.info(pin.getName()));


        }
    }
}
