package teamthree.twodo.logic.commands;

import static java.util.Objects.requireNonNull;

import teamthree.twodo.model.TaskBook;

/**
 * Clears the TaskBooks.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Description book has been cleared!";


    @Override
    public CommandResult execute() {
        requireNonNull(model);
        model.resetData(new TaskBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
