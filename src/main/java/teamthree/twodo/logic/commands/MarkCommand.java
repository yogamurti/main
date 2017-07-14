//@@author A0139267W
package teamthree.twodo.logic.commands;

import teamthree.twodo.commons.core.Messages;
import teamthree.twodo.commons.core.UnmodifiableObservableList;
import teamthree.twodo.commons.core.index.Index;
import teamthree.twodo.logic.commands.exceptions.CommandException;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.exceptions.TaskNotFoundException;

// Marks a task as complete using its last displayed index from the TaskBook.
public class MarkCommand extends Command {

    // Command word can be either one of the two
    public static final String COMMAND_WORD = "mark";
    public static final String COMMAND_WORD_UNIXSTYLE = "-m";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the task identified by the index number used in the last task listing as complete.\n"
            + "Parameters: INDEX (must be a positive integer)\n" + "Example: " + COMMAND_WORD + " 4 ";

    public static final String MESSAGE_MARK_TASK_SUCCESS = "Marked task as complete: %1$s";

    public final Index targetIndex;

    public MarkCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {

        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        ReadOnlyTask taskToMark = lastShownList.get(targetIndex.getZeroBased());

        try {
            model.markTask(taskToMark);
            history.addToMarkHistory(taskToMark);
        } catch (TaskNotFoundException pnfe) {
            assert false : "The target task cannot be missing";
        }

        return new CommandResult(String.format(MESSAGE_MARK_TASK_SUCCESS, taskToMark));
    }
}
