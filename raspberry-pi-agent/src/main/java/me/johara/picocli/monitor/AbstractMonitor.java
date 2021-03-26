package me.johara.picocli.monitor;

import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import me.johara.picocli.MonitorCommand;
import org.jboss.logging.Logger;

public abstract class AbstractMonitor implements UtilityMonitor<Long>, Runnable {

    static Logger logger = Logger.getLogger(MonitorCommand.class);

    String trigger;

    public abstract void startCallback();
    public abstract void stopCallback();

    public AbstractMonitor(String trigger) {
        this.trigger = trigger;
    }

    public GpioPinListenerDigital getListener() {
        return event -> {
            if (event.getState().getName().equals(trigger)) {
                Long timestamp = System.currentTimeMillis();
                logger.infof("Sending Event: %ld", timestamp);
                getEmitter().send(timestamp);
            }
        };
    }


}
