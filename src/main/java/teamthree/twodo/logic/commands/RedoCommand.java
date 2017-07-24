package teamthree.twodo.logic.commands;

import teamthree.twodo.automark.AutoMarkManager;
import teamthree.twodo.commons.core.Config;
import teamthree.twodo.commons.core.options.Alarm;
import teamthree.twodo.commons.core.options.AutoMark;
import teamthree.twodo.commons.core.options.Options;
import teamthree.twodo.logic.commands.exceptions.CommandException;
import teamthree.twodo.logic.parser.exceptions.ParseException;
import teamthree.twodo.model.ReadOnlyTaskList;
import teamthree.twodo.model.TaskList;
import teamthree.twodo.model.tag.Tag;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.exceptions.DuplicateTaskException;
import teamthree.twodo.model.task.exceptions.TaskNotFoundException;

//@@author A0162253M
// Redoes the function that was undone in the previous command
public class RedoCommand extends Command {

    public static final String COMMAND_WORD = "redo";
    public static final String COMMAND_WORD_FAST = "r";
    public static final String MESSAGE_SUCCESS = "Successfully redid command.\n";
    public static final String MESSAGE_NO_HISTORY = "Failed to redo: You have not yet entered any undo commands.";
    public static final String MESSAGE_INVALID_PREVIOUS_COMMAND = "Failed to redo: Invalid previous command ";

    public static final String MESSAGE_USAGE = ": Redoes the previous undone command.";

    private static String fullMessage;

    @Override
    public CommandResult execute() throws CommandException {
        if (undoHistory.getUserInputHistory().isEmpty()) {
            return new CommandResult(MESSAGE_NO_HISTORY);
        }

        CommandResult redoResult = null;
        try {
            redoResult = processUndoCommand();
        } catch (DuplicateTaskException e) {
            throw new CommandException(AddCommand.MESSAGE_DUPLICATE_TASK);
        } catch (TaskNotFoundException e) {
            assert false : "The target task cannot be missing";
        } catch (ParseException e) {
            assert false : "The Command called previously is invalid";
        }

        return redoResult;
    }

    private CommandResult processUndoCommand() throws TaskNotFoundException, DuplicateTaskException, ParseException {
        final String previousCommandWord = undoHistory.getUserInputHistory().pop();
        history.addToUserInputHistory(previousCommandWord);

        switch (previousCommandWord) {

        case DeleteCommand.COMMAND_WORD:
        case DeleteCommand.COMMAND_WORD_QUICK:
        case DeleteCommand.COMMAND_WORD_FAST:
        case DeleteCommand.COMMAND_WORD_SHORT:
            return redoDeleteCommand();

        case UndoCommand.DELETE_TAG:
            return redoDeleteTagCommand();

        case UndoCommand.ADD_TAG:
            return redoAddTagCommand();

        case EditCommand.COMMAND_WORD:
        case EditCommand.COMMAND_WORD_FAST:
            return redoEditCommand();

        case AddCommand.COMMAND_WORD_QUICK:
        case AddCommand.COMMAND_WORD_FAST:
        case AddCommand.COMMAND_WORD:
            return redoAddCommand();

        case ClearCommand.COMMAND_WORD:
            return redoClearCommand();

        case UnmarkCommand.COMMAND_WORD:
        case UnmarkCommand.COMMAND_WORD_FAST:
            return redoUnmarkCommand();

        case MarkCommand.COMMAND_WORD:
        case MarkCommand.COMMAND_WORD_FAST:
            return redoMarkCommand();

        case OptionsCommand.COMMAND_WORD:
        case OptionsCommand.COMMAND_WORD_FAST:
            return redoOptionsCommand();

        default:
            String message = MESSAGE_INVALID_PREVIOUS_COMMAND.concat(previousCommandWord);
            return new CommandResult(message);
        }
    }

    /**
     * Restores options settings that was undone previously and stores {@code edittedOptions} for UndoCommand
     */
    private CommandResult redoOptionsCommand() {
        Options edittedOptions = undoHistory.getOptionsHistory().pop();
        assert edittedOptions != null;
        history.addToOptionsHistory(edittedOptions);
        Options currentOptions = updateOptions(edittedOptions);
        fullMessage = MESSAGE_SUCCESS.concat(OptionsCommand.MESSAGE_UPDATE_OPTIONS_SUCCESS);
        return new CommandResult(String.format(fullMessage, currentOptions));
    }

    /**
     * Mark a task that was previously unmark due to undoCommand and stores {@code taskToMark} for the next UndoCommand
     */
    private CommandResult redoMarkCommand() throws TaskNotFoundException {
        ReadOnlyTask taskToMark = undoHistory.getUnmarkHistory().pop();
        history.addToMarkHistory(taskToMark);
        model.markTask(taskToMark);
        fullMessage = MESSAGE_SUCCESS.concat(MarkCommand.MESSAGE_MARK_TASK_SUCCESS);
        return new CommandResult(String.format(fullMessage, taskToMark));
    }

    /**
     * Unmark a task that was previously mark due to undoCommand
     * and stores {@code taskToUnmark} for the next UndoCommand
     */
    private CommandResult redoUnmarkCommand() throws TaskNotFoundException {
        ReadOnlyTask taskToUnmark = undoHistory.getMarkHistory().pop();
        history.addToUnmarkHistory(taskToUnmark);
        model.unmarkTask(taskToUnmark);
        fullMessage = MESSAGE_SUCCESS.concat(UnmarkCommand.MESSAGE_UNMARK_TASK_SUCCESS);
        return new CommandResult(String.format(fullMessage, taskToUnmark));
    }

    /**
     * Clears taskList that was previously restored due to a undoCommand
     * and stores the current TaskList for the next UndoCommand
     */
    private CommandResult redoClearCommand() {
        history.addToClearHistory(new TaskList(model.getTaskList()));
        model.resetData(new TaskList());
        fullMessage = MESSAGE_SUCCESS.concat(ClearCommand.MESSAGE_SUCCESS);
        return new CommandResult(fullMessage);
    }

    /**
     * Add task that was previously deleted due to a undoCommand and stores {@code taskToAdd} for the next UndoCommand
     */
    private CommandResult redoAddCommand() throws DuplicateTaskException {
        ReadOnlyTask taskToAdd = undoHistory.getDeleteHistory().pop();
        history.addToAddHistory(taskToAdd);
        model.addTask(taskToAdd);
        fullMessage = MESSAGE_SUCCESS.concat(AddCommand.MESSAGE_SUCCESS);
        return new CommandResult(String.format(fullMessage, taskToAdd));
    }

    /**
     * Restores the task that was edited due to a previous undoCommand and
     * stores {@code taskToEdit} and {@code currentTask} for the next UndoCommand
     */
    private CommandResult redoEditCommand() throws DuplicateTaskException, TaskNotFoundException {
        ReadOnlyTask taskToEdit = undoHistory.getBeforeEditHistory().pop();
        ReadOnlyTask currentTask = undoHistory.getAfterEditHistory().pop();
        history.addToBeforeEditHistory(currentTask);
        history.addToAfterEditHistory(taskToEdit);
        model.updateTask(currentTask, taskToEdit);
        fullMessage = MESSAGE_SUCCESS.concat(EditCommand.MESSAGE_EDIT_TASK_SUCCESS);
        return new CommandResult(String.format(fullMessage, taskToEdit));
    }

    /**
     * Delete tag that was previously added due to a undoCommand
     * and stores {@code tag} and {@code taskList} for the next UndoCommand
     */
    private CommandResult redoDeleteTagCommand() {
        ReadOnlyTaskList taskList = undoHistory.getDelTagHistory().pop();
        Tag tag = undoHistory.getTagDeletedHistory().pop();
        history.addToDelTagHistory(new TaskList(model.getTaskList())); //Store current model b4 tag is deleted
        history.addToTagDeletedHistory(tag); //Store Tag for UndoCommand
        model.resetData(taskList);
        fullMessage = MESSAGE_SUCCESS.concat(String.format(DeleteCommand.MESSAGE_DELETE_TAG_SUCCESS, tag.tagName));
        return new CommandResult(fullMessage);
    }

    /**
     * Add tag that was previously deleted due to a undoCommand
     * and stores {@code tag} and {@code taskList} for the next UndoCommand
     */
    private CommandResult redoAddTagCommand() {
        ReadOnlyTaskList taskList = undoHistory.getAddTagHistory().pop();
        Tag tag = undoHistory.getTagAddedHistory().pop();
        history.addToAddTagHistory(new TaskList(model.getTaskList())); //Store current model b4 tag is added
        history.addToTagAddedHistory(tag); //Store Tag for UndoCommand
        model.resetData(taskList);
        fullMessage = MESSAGE_SUCCESS.concat(String.format(AddCommand.MESSAGE_SUCCESS_TAG, tag.tagName));
        return new CommandResult(fullMessage);
    }

    /**
     * Delete task that was previously added due to a undoCommand
     * and stores {@code taskToDelete} for the next UndoCommand
     */
    private CommandResult redoDeleteCommand() throws TaskNotFoundException {
        ReadOnlyTask taskToDelete = undoHistory.getAddHistory().pop();
        history.addToDeleteHistory(taskToDelete);
        model.deleteTask(taskToDelete);
        fullMessage = MESSAGE_SUCCESS.concat(DeleteCommand.MESSAGE_DELETE_TASK_SUCCESS);
        return new CommandResult(String.format(fullMessage, taskToDelete));
    }

    /**
     * Returns a new Object Options with default alarm and automark settings
     */
    private Options getDefaultOption() {
        Alarm alarm = new Alarm(Config.defaultNotificationPeriodToString());
        AutoMark autoMark = new AutoMark(AutoMarkManager.getSetToRun());
        return new Options(alarm, autoMark);
    }

    /**
     * Update Current Options settings with {@code editedOptions}
     * @param editedOptions must not be null
     */
    private Options updateOptions(Options editedOptions) {
        Options currentOption = getDefaultOption();
        if (!editedOptions.getAlarm().equals(currentOption.getAlarm())) {
            Config.changeDefaultNotificationPeriod(editedOptions.getAlarm().getValue());
            currentOption.editAlarm(editedOptions.getAlarm());
            // Checks if the alarm updates were properly executed for both components
            assert(Config.defaultNotificationPeriodToString() == currentOption.getAlarm().getValue());
        }
        if (!editedOptions.getAutoMark().equals(currentOption.getAutoMark())) {
            AutoMarkManager.setToRun(editedOptions.getAutoMark().getValue());
            currentOption.editAutoMark(editedOptions.getAutoMark());
            // Checks if the alarm updates were properly executed for both components
            assert(AutoMarkManager.getSetToRun() == currentOption.getAutoMark().getValue());
        }
        return currentOption;
    }
}
