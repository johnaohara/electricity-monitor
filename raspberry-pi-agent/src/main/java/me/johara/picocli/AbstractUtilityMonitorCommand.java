package me.johara.picocli;

import me.johara.picocli.monitor.PinStateMonitor;
import me.johara.picocli.util.Raspberry;
import org.jboss.logging.Logger;
import picocli.CommandLine;

public abstract class AbstractUtilityMonitorCommand implements Runnable {

    @CommandLine.Option(names = {"--pin"}, description = "GPIO pin", defaultValue = "GPIO 0", required = true)
    String pinName;

    @CommandLine.Option(names = {"--debounce"}, description = "Pin debounce threshold(ms)", defaultValue = "100")
    int debounce;

    @CommandLine.Option(names = {"--trigger"}, description = "State to trigger event \"HIGH\" or \"LOW\"", defaultValue = "HIGH")
    String trigger;

    @CommandLine.Option(names = {"--utility"}, description = "Utility to monitor", required = true)
    String utility;


    static Logger logger = Logger.getLogger(UtilityMonitorCommand.class);

    PinStateMonitor monitor;


    public abstract void startCallback();
    public abstract void stopCallback();

    @Override
    public void run() {

        if (!Raspberry.isPi()) {
            logger.error("Can not initialise Pi4J.  Platform is not Raspberry Pi!");
        } else {
            logger.infof("Initializing Monitor: %s", monitor.getName());

            monitor.initialize();

            startCallback();

            monitor.start();

            // keep program running until user aborts (CTRL-C)
            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    stopCallback();
                    monitor.stop();
                    monitor.terminate();
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

}
