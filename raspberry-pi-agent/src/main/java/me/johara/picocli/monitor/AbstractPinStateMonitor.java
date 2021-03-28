package me.johara.picocli.monitor;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import me.johara.picocli.UtilityMonitorCommand;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

public abstract class AbstractPinStateMonitor implements PinStateMonitor<Long> {

    protected static Logger logger = Logger.getLogger(UtilityMonitorCommand.class);

    private GpioController gpio;
    private GpioPinDigitalInput pin;
    private Emitter<Long> timestampEmitter;

    String trigger;
    String pinName;
    int debounce;
    boolean debug = false;

    public abstract String getName();


    public AbstractPinStateMonitor(Emitter<Long> timestampEmitter, String pinName, int debounce, String trigger, boolean debug) {
        this.timestampEmitter = timestampEmitter;
        this.pinName = pinName;
        this.debounce = debounce;
        this.trigger = trigger;
        this.debug = debug;
    }

    public GpioPinListenerDigital getListener() {
        return event -> {
            if (event.getState().getName().equals(trigger)) {
                Long timestamp = System.currentTimeMillis();
                logger.debugf("Sending Event: %ld", timestamp);
                if (!debug) {
                    timestampEmitter.send(timestamp);
                } else {
                    logger.infof("Sending Event: %ld", timestamp);
                }
            }
        };
    }

    @Override
    public void initialize() {
        this.gpio = GpioFactory.getInstance();
        Pin GPIO_PIN = RaspiPin.getPinByName(pinName);

        if (GPIO_PIN == null) {
            logger.errorf("Unknown pin: %s\n", pinName);
            throw new IllegalStateException("Unknown pin: " + pinName);
        }

        logger.infof("Configuring GPIO pin #%s\n", GPIO_PIN.getName());
        pin = gpio.provisionDigitalInputPin(GPIO_PIN);
        pin.setShutdownOptions(true);
        pin.setDebounce(debounce);

    }

    @Override
    public void terminate() {
        gpio.shutdown();
    }

    @Override
    public void start() {
        // create and register gpio pin listener
        pin.addListener(getListener());

        logger.infof("Starting %s monitor\n", getName());
    }

    @Override
    public void stop() {
        logger.infof("Stopping %s monitor\n", getName());
        pin.removeAllListeners();
    }


}
