package me.johara.picocli;

import me.johara.picocli.util.MonitorFactory;
import org.jboss.logging.Logger;
import picocli.CommandLine.Command;

@Command(name = "debug")
public class DebugCommand extends AbstractUtilityMonitorCommand {

    static Logger logger = Logger.getLogger(DebugCommand.class);

    public DebugCommand() {
        logger.warn("Initialising DebugCommand");
        monitor = MonitorFactory.buildMonitor(utility, pinName, debounce, trigger,true);
    }

    @Override
    public void startCallback() {
        logger.info("Starting DEBUG Monitoring");
    }

    @Override
    public void stopCallback() {
        logger.info("Stopping DEBUG Monitoring");
    }
}
