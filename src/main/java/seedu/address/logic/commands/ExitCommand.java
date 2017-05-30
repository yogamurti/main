package seedu.address.logic.commands;

/**
 * Terminates the program.
 */
public class ExitCommand extends Command {

    public static final String COMMAND_WORD = "exit";

    public static final String MESSAGE_EXIT_ACKNOWLEDGEMENT = "Exiting Address Book as requested ...";

    @Override
    public CommandResult execute() {
        // TODO: (level3) exit event
        return new CommandResult(MESSAGE_EXIT_ACKNOWLEDGEMENT);
    }

}
