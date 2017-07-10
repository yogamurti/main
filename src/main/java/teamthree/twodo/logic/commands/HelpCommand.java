package teamthree.twodo.logic.commands;

import teamthree.twodo.commons.core.EventsCenter;
import teamthree.twodo.commons.events.ui.ShowHelpRequestEvent;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n" + "Example: "
            + COMMAND_WORD;

    public static final String SHOWING_HELP_MESSAGE = "Opened help window.";

    private String message;

    public HelpCommand() {
    }

    public HelpCommand(String message) {
        this.message = message;
    }

    @Override
    public CommandResult execute() {
        if (message == null) {
            EventsCenter.getInstance().post(new ShowHelpRequestEvent());
            return new CommandResult(SHOWING_HELP_MESSAGE);
        }
        return new CommandResult(message);
    }
}
