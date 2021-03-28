package me.johara.picocli;

import me.johara.picocli.util.MonitorFactory;
import org.jboss.logging.Logger;
import picocli.CommandLine.Command;

@Command(name = "monitor")
public class UtilityMonitorCommand extends AbstractUtilityMonitorCommand {

    static Logger logger = Logger.getLogger(UtilityMonitorCommand.class);

    public UtilityMonitorCommand() {
        logger.info("Initialising MonitorCommand");
    }

    @Override
    public void instantiateCallback() {
        monitor = MonitorFactory.buildMonitor(utility, pinName, debounce, trigger,false);
    }
    @Override
    public void startCallback() {
        logger.info("Starting Monitoring");
    }

    @Override
    public void stopCallback() {
        logger.info("Stopping Monitoring");
    }


}
