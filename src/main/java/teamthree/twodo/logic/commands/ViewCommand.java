package teamthree.twodo.logic.commands;

import teamthree.twodo.commons.core.Messages;
import teamthree.twodo.commons.core.UnmodifiableObservableList;
import teamthree.twodo.commons.core.index.Index;
import teamthree.twodo.logic.commands.exceptions.CommandException;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.exceptions.TaskNotFoundException;

public class ViewCommand extends Command{

    //Command word can be any one of the three
    public static final String COMMAND_WORD = "view";
    public static final String COMMAND_WORD_UNIXSTYLE = "-v";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Views a Task from current list. " + "Parameters: "
            + "INDEX " + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_VIEW_TASK_SUCCESS = "Viewing task(s): %1$s\n";
    public static final String MESSAGE_INVALID_INDEX = "Invalid index";

    private final Index targetIndex;

    /**
     * Creates an AddCommand to add the specified {@code ReadOnlyTask}
     */
    public ViewCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {
        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredPersonList();
        
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        
        ReadOnlyTask taskToView = lastShownList.get(targetIndex.getZeroBased());
        
        model.updateFilteredTaskList(taskToView);
        
        return new CommandResult(String.format(MESSAGE_VIEW_TASK_SUCCESS, taskToView));
    }
}
