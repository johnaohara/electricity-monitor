package me.johara.picocli;

import io.quarkus.picocli.runtime.annotations.TopCommand;
import picocli.CommandLine;

@TopCommand
@CommandLine.Command(mixinStandardHelpOptions = true, subcommands = { DebugCommand.class, UtilityMonitorCommand.class, PinsCommand.class, ToggleCommand.class })
public class EntryCommand {
}
