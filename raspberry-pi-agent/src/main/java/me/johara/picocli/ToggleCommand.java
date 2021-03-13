package me.johara.picocli;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import me.johara.picocli.util.Raspberry;
import org.jboss.logging.Logger;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "toggle")
public class ToggleCommand implements Runnable {

    static Logger logger = Logger.getLogger(ToggleCommand.class);


    static GpioController gpio;
    static GpioPinDigitalOutput pin;


    @Option(names = {"--pin"}, description = "GPIO pin", defaultValue = "GPIO 17")
    String pinName;


    @Option(names = {"--sleep"}, description = "Sleep Duration", defaultValue = "500")
    int sleepDuration;


    @Override
    public void run() {

        boolean HIGH = false;

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
                pin = gpio.provisionDigitalOutputPin(GPIO_PIN, "MyLED", PinState.LOW);
                pin.setShutdownOptions(true, PinState.LOW);
//            pin.setDebounce(100);

                logger.info("Starting Toggling");

                // keep program running until user aborts (CTRL-C)
                while (true) {
                    try {
                        HIGH = !HIGH;

                        logger.infof("Toggling %s", HIGH ? "high" : "low");

                        if(HIGH){
                            pin.high();
                        } else {
                            pin.low();
                        }
                        Thread.sleep(sleepDuration);
                    } catch (InterruptedException e) {
                        // stop all GPIO activity/threads by shutting down the GPIO controller
                        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
                        logger.info("Shutting down GPIO controller");
                        gpio.shutdown();
                        Thread.currentThread().interrupt();
                    }
                }

            }

        }
    }
}
