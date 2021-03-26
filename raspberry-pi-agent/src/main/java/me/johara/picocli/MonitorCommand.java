package me.johara.picocli;

import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import me.johara.picocli.monitor.AbstractMonitor;
import me.johara.picocli.monitor.ElectricityMonitor;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import javax.inject.Inject;

@Command(name = "monitor")
public class MonitorCommand extends AbstractGpioMonitor {

    static Logger logger = Logger.getLogger(MonitorCommand.class);

    private final AbstractMonitor monitor;

    public MonitorCommand() {
        logger.info("Initialising MonitorCommand");
        monitor = new ElectricityMonitor();
    }

    @Override
    GpioPinListenerDigital getListener() {
        return monitor.getListener();
    }

    @Override
    public void startCallback() {
        logger.info("Starting Monitoring");
    }

    @Override
    public void stopCallback() {
        logger.info("Shutting down GPIO controller");
    }


}
