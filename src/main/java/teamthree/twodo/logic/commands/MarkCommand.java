package teamthree.twodo.logic.commands;

import teamthree.twodo.commons.core.Messages;
import teamthree.twodo.commons.core.UnmodifiableObservableList;
import teamthree.twodo.commons.core.index.Index;
import teamthree.twodo.logic.commands.exceptions.CommandException;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.exceptions.TaskNotFoundException;

//@@author A0139267W
// Marks a task as complete using its last displayed index from the TaskList.
public class MarkCommand extends Command {

    // Command word can be either one of the two
    public static final String COMMAND_WORD = "mark";
    public static final String COMMAND_WORD_FAST = "m";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the task identified by the index number used in the last task listing as complete.\n"
            + "The index must be a positive integer.\n"
            + "Parameters: {INDEX}\n"
            + "Example: " + COMMAND_WORD + " 4";

    public static final String MESSAGE_MARK_TASK_SUCCESS = "Marked task as complete: %1$s";
    public static final String MESSAGE_ALREADY_MARKED_TASK = "Task already marked as complete!";

    public final Index targetIndex;

    public MarkCommand(Index targetIndex) {
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

    private ReadOnlyTask getTaskToMark(UnmodifiableObservableList<ReadOnlyTask> lastShownList) {
        return lastShownList.get(targetIndex.getZeroBased());
    }

    private void checkForCompletedTask(ReadOnlyTask taskToMark) throws CommandException {
        if (taskToMark.isCompleted()) {
            throw new CommandException(MESSAGE_ALREADY_MARKED_TASK);
        }
    }

    @Override
    public CommandResult execute() throws CommandException {
        UnmodifiableObservableList<ReadOnlyTask> lastShownList = getLastShownList();
        checkForInvalidIndex(lastShownList);

        ReadOnlyTask taskToMark = getTaskToMark(lastShownList);
        checkForCompletedTask(taskToMark);

        try {
            model.markTask(taskToMark);
            history.addToMarkHistory(taskToMark);
        } catch (TaskNotFoundException pnfe) {
            assert false : "The target task cannot be missing";
        }

        return new CommandResult(String.format(MESSAGE_MARK_TASK_SUCCESS, taskToMark));
    }

}
