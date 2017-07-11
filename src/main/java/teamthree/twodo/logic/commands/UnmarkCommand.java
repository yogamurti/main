//@@author A0139267W
package teamthree.twodo.logic.commands;

import teamthree.twodo.commons.core.Messages;
import teamthree.twodo.commons.core.UnmodifiableObservableList;
import teamthree.twodo.commons.core.index.Index;
import teamthree.twodo.logic.commands.exceptions.CommandException;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.exceptions.TaskNotFoundException;

//Marks a task as incomplete using its last displayed index from the TaskBook.
public class UnmarkCommand extends Command {

    //Command word can be either one of the two
    public static final String COMMAND_WORD = "unmark";
    public static final String COMMAND_WORD_UNIXSTYLE = "-un";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the task identified by the index number used in the last task listing as incomplete.\n"
            + "Parameters: INDEX (must be a positive integer)\n" + "Example: " + COMMAND_WORD + " 4 ";

    public static final String MESSAGE_MARK_PERSON_SUCCESS = "Marked task as incomplete: %1$s";

    public final Index targetIndex;

    public UnmarkCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {

        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        ReadOnlyTask taskToUnmark = lastShownList.get(targetIndex.getZeroBased());

        try {
            model.unmarkTask(taskToUnmark);
        } catch (TaskNotFoundException pnfe) {
            assert false : "The target task cannot be missing";
        }

        return new CommandResult(String.format(MESSAGE_MARK_PERSON_SUCCESS, taskToUnmark));
    }
}
