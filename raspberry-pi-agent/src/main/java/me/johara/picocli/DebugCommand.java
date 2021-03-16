package me.johara.picocli;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import me.johara.picocli.util.Raspberry;
import org.jboss.logging.Logger;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "debug")
public class DebugCommand implements Runnable {

    static Logger logger = Logger.getLogger(DebugCommand.class);

    static GpioController gpio;
    static GpioPinDigitalInput pin;


    @CommandLine.Option(names = {"--pin"}, description = "GPIO pin", defaultValue = "GPIO 0")
    String pinName;

    @CommandLine.Option(names = {"--debounce"}, description = "Pin debounce threshold(ms)", defaultValue = "100")
    int debounce;

    @CommandLine.Option(names = {"--trigger"}, description = "State to trigger event \"HIGH\" or \"LOW\"", defaultValue = "HIGH")
    String trigger;


    public DebugCommand() {
        logger.warn("Initialising DebugCommand");
    }


    @Override
    public void run() {

        if (!Raspberry.isPi()) {
            logger.error("Can not initialise Pi4J.  Platform is not Raspberry Pi!");
        } else {
            logger.info("Initializing GPIO Factory");
            gpio = GpioFactory.getInstance();

            Pin GPIO_PIN = RaspiPin.getPinByName(pinName);

            if (GPIO_PIN == null) {
                logger.errorf("Unknown pin: %s", pinName);

            } else {

                logger.infof("Configuring GPIO pin #%s", GPIO_PIN.getName());
                pin = gpio.provisionDigitalInputPin(GPIO_PIN, PinPullResistance.PULL_DOWN);
                pin.setShutdownOptions(true);
                pin.setDebounce(debounce);

                // create and register gpio pin listener
                pin.addListener((GpioPinListenerDigital) event -> {
                    eventCallback(event);
                });

                startCallback();


                // keep program running until user aborts (CTRL-C)
                while (true) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // stop all GPIO activity/threads by shutting down the GPIO controller
                        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
                        endCallback();
                        gpio.shutdown();
                        Thread.currentThread().interrupt();
                    }
                }

            }

        }
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
    public void endCallback() {
        logger.info("Shutting down GPIO controller");
    }
}
