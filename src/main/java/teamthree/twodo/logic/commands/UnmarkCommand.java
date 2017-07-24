package teamthree.twodo.logic.commands;

import teamthree.twodo.commons.core.Messages;
import teamthree.twodo.commons.core.UnmodifiableObservableList;
import teamthree.twodo.commons.core.index.Index;
import teamthree.twodo.logic.commands.exceptions.CommandException;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.exceptions.TaskNotFoundException;

//@@author A0139267W
// Marks a task as incomplete using its last displayed index from the TaskList.
public class UnmarkCommand extends Command {

    // Command word can be either one of the two
    public static final String COMMAND_WORD = "unmark";
    public static final String COMMAND_WORD_FAST = "un";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the task identified by the index number used in the last task listing as incomplete.\n"
            + "The index must be a positive integer.\n"
            + "Parameters: {INDEX}\n"
            + "Example: " + COMMAND_WORD + " 4";

    public static final String MESSAGE_UNMARK_TASK_SUCCESS = "Marked task as incomplete: %1$s";
    public static final String MESSAGE_NOT_MARKED_TASK = "Task not marked as complete!";

    public final Index targetIndex;

    public UnmarkCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    private UnmodifiableObservableList<ReadOnlyTask> getLastShownList() {
        return model.getFilteredAndSortedTaskList();
    }

    private void checkForInvalidIndex(UnmodifiableObservableList<ReadOnlyTask> lastShownList) throws CommandException {
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
    }

    private ReadOnlyTask getTaskToUnmark(UnmodifiableObservableList<ReadOnlyTask> lastShownList) {
        return lastShownList.get(targetIndex.getZeroBased());
    }

    private void checkForIncompletedTask(ReadOnlyTask taskToUnmark) throws CommandException {
        if (!taskToUnmark.isCompleted()) {
            throw new CommandException(MESSAGE_NOT_MARKED_TASK);
        }
    }

    @Override
    public CommandResult execute() throws CommandException {
        UnmodifiableObservableList<ReadOnlyTask> lastShownList = getLastShownList();
        checkForInvalidIndex(lastShownList);

        ReadOnlyTask taskToUnmark = getTaskToUnmark(lastShownList);
        checkForIncompletedTask(taskToUnmark);

        try {
            model.unmarkTask(taskToUnmark);
            history.addToUnmarkHistory(taskToUnmark);
        } catch (TaskNotFoundException pnfe) {
            assert false : "The target task cannot be missing";
        }

        return new CommandResult(String.format(MESSAGE_UNMARK_TASK_SUCCESS, taskToUnmark));
    }

}
