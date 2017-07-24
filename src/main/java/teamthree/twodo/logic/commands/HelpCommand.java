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
    public static final String MESSAGE_HISTORY_USAGE = "Only find and list commands use this function.\n"
            + "Add " + ListCommand.COMMAND_WORD_HISTORY
            + " when using either commands to access the completed task list instead of the incomplete task list.\n"
            + "Example: find /h project";
    public static final String MESSAGE_FLOATING_USAGE = "Only list command uses this function.\n"
            + "Add " + ListCommand.COMMAND_WORD_FLOATING
            + " to access the floating task list instead of the entire task list."
            + "Example: list /f";

    private String message;

    public HelpCommand() {
    }

    public HelpCommand(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public CommandResult execute() {
        if (message == null) {
            EventsCenter.getInstance().post(new ShowHelpRequestEvent());
            return new CommandResult(SHOWING_HELP_MESSAGE);
        }
        return new CommandResult(message);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof HelpCommand)) {
            return false;
        }
        return this.message.equals(((HelpCommand) other).getMessage());
    }
}
