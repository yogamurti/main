package teamthree.twodo.logic.commands;

import teamthree.twodo.automark.AutoMarkManager;
import teamthree.twodo.commons.core.Config;
import teamthree.twodo.commons.core.options.Alarm;
import teamthree.twodo.commons.core.options.AutoMark;
import teamthree.twodo.commons.core.options.Options;
import teamthree.twodo.commons.exceptions.IllegalValueException;
import teamthree.twodo.logic.commands.exceptions.CommandException;
import teamthree.twodo.logic.parser.exceptions.ParseException;
import teamthree.twodo.model.ReadOnlyTaskList;
import teamthree.twodo.model.TaskList;
import teamthree.twodo.model.tag.Tag;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.model.task.TaskWithDeadline;
import teamthree.twodo.model.task.exceptions.DuplicateTaskException;
import teamthree.twodo.model.task.exceptions.TaskNotFoundException;


// @@author A0162253M
// Undoes the previous command by the user
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";
    public static final String COMMAND_WORD_FAST = "u";
    public static final String MESSAGE_SUCCESS = "Successfully undid command.\n";
    public static final String MESSAGE_NO_HISTORY = "Failed to undo: You have not yet entered any commands. ";
    public static final String MESSAGE_INVALID_PREVIOUS_COMMAND = "Failed to undo: Invalid previous command ";
    public static final String MESSAGE_ADD_TAG_SUCCESS = "Added Tag ";

    public static final String DELETE_TAG = "tag";

    public static final String MESSAGE_USAGE = ": Undoes the previous command if undoable.";

    private static String fullMessage;

    @Override
    public CommandResult execute() throws CommandException {
        if (history.getUserInputHistory().isEmpty()) {
            return new CommandResult(MESSAGE_NO_HISTORY);
        }

        CommandResult undoResult = null;
        try {
            undoResult = processUserInput();
        } catch (DuplicateTaskException e) {
            throw new CommandException(AddCommand.MESSAGE_DUPLICATE_TASK);
        } catch (TaskNotFoundException e) {
            assert false : "The target task cannot be missing";
        } catch (ParseException e) {
            assert false : "The Command is invalid";
        } catch (IllegalValueException e) {
            assert false : "The Value is illegal";
        }
        return undoResult;
    }

    private CommandResult processUserInput() throws TaskNotFoundException, IllegalValueException, CommandException {
        final String previousCommandWord = history.getUserInputHistory().pop();
        undoHistory.addToUserInputHistory(previousCommandWord); //Save CommandWord for RedoCommand

        //Process previous Command Word and undo the command
        switch (previousCommandWord) {

        case AddCommand.COMMAND_WORD:
        case AddCommand.COMMAND_WORD_QUICK:
        case AddCommand.COMMAND_WORD_FAST:
            return undoAddCommand();

        case EditCommand.COMMAND_WORD:
            return undoEditCommand();

        case DeleteCommand.COMMAND_WORD_QUICK:
        case DeleteCommand.COMMAND_WORD_FAST:
        case DeleteCommand.COMMAND_WORD_SHORT:
        case DeleteCommand.COMMAND_WORD:
            return undoDeleteCommand();

        case DELETE_TAG:
            return undoDeleteTagCommand();

        case ClearCommand.COMMAND_WORD:
            return undoClearCommand();

        case MarkCommand.COMMAND_WORD:
        case MarkCommand.COMMAND_WORD_FAST:
            return undoMarkCommand();

        case UnmarkCommand.COMMAND_WORD:
        case UnmarkCommand.COMMAND_WORD_FAST:
            return undoUnmarkCommand();

        case OptionsCommand.COMMAND_WORD:
        case OptionsCommand.COMMAND_WORD_FAST:
            return undoOptionsCommand();

        default:
            //For Previous Command like find, list which cannot be undone
            String message = MESSAGE_INVALID_PREVIOUS_COMMAND.concat(previousCommandWord);
            return new CommandResult(message);
        }
    }

    /**
     * Restore previous options settings and store {@code edittedOptions} for RedoCommand
     */
    private CommandResult undoOptionsCommand() {
        undoHistory.addToOptionsHistory(getDefaultOption());
        Options edittedOptions = history.getOptionsHistory().pop();
        assert edittedOptions != null;
        Options currentOptions = updateOptions(edittedOptions);
        fullMessage = MESSAGE_SUCCESS.concat(OptionsCommand.MESSAGE_UPDATE_OPTIONS_SUCCESS);
        return new CommandResult(String.format(fullMessage, currentOptions));
    }

    private Options getDefaultOption() {
        Alarm alarm = new Alarm(Config.defaultNotificationPeriodToString());
        AutoMark autoMark = new AutoMark(AutoMarkManager.getSetToRun());
        return new Options(alarm, autoMark);
    }

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

    /**
     * Mark task that was marked and store {@code taskToUnmark} for RedoCommand
     */
    private CommandResult undoUnmarkCommand() throws TaskNotFoundException {
        ReadOnlyTask taskToMark = history.getUnmarkHistory().pop(); //Retrieve Task to mark
        undoHistory.addToMarkHistory(taskToMark); //Store Task for RedoCommand
        model.markTask(taskToMark);
        fullMessage = MESSAGE_SUCCESS.concat(MarkCommand.MESSAGE_MARK_TASK_SUCCESS);
        return new CommandResult(String.format(fullMessage, taskToMark));
    }

    /**
     * Unmark task that was marked and store {@code taskToUnmark} for RedoCommand
     */
    private CommandResult undoMarkCommand() throws TaskNotFoundException {
        ReadOnlyTask taskToUnmark = history.getMarkHistory().pop();
        undoHistory.addToUnmarkHistory(taskToUnmark); //Store Task for RedoCommand
        model.unmarkTask(taskToUnmark);
        fullMessage = MESSAGE_SUCCESS.concat(UnmarkCommand.MESSAGE_UNMARK_TASK_SUCCESS);
        return new CommandResult(String.format(fullMessage, taskToUnmark));
    }

    /**
     * Restores Original TaskLIst before Clear Command
     */
    private CommandResult undoClearCommand() {
        ReadOnlyTaskList taskBook = history.getClearHistory().pop();
        model.resetData(taskBook);
        fullMessage = MESSAGE_SUCCESS.concat("Restored TaskList");
        return new CommandResult(fullMessage);
    }

    /**
     * Add back Deleted Tag and Stores {@code tag} and {@code taskList} for RedoCommand
     */
    private CommandResult undoDeleteTagCommand() {
        ReadOnlyTaskList taskList = history.getDelTagHistory().pop();
        Tag tag = history.getTagHistory().pop();
        undoHistory.addToDelTagHistory(new TaskList(model.getTaskList())); //Store current model b4 tag is added
        undoHistory.addToTagHistory(tag); //Store Tag for RedoCommand
        model.resetData(taskList);
        fullMessage = MESSAGE_SUCCESS.concat(MESSAGE_ADD_TAG_SUCCESS + tag.tagName);
        return new CommandResult(fullMessage);
    }

    /**
     * Add back Deleted Task and Stores {@code taskToAdd} for RedoCommand
     */
    private CommandResult undoDeleteCommand() throws DuplicateTaskException {
        ReadOnlyTask taskToAdd = history.getDeleteHistory().pop();
        undoHistory.addToAddHistory(taskToAdd); //Store Task for RedoCommand
        model.addTask(taskToAdd);
        fullMessage = MESSAGE_SUCCESS.concat(AddCommand.MESSAGE_SUCCESS);
        return new CommandResult(String.format(fullMessage, taskToAdd));
    }

    /**
     * Restores back original Task and Stores {@code originalTask} and {@code edittedTask} for RedoCommand
     */
    private CommandResult undoEditCommand() throws DuplicateTaskException, TaskNotFoundException {
        ReadOnlyTask originalTask = history.getBeforeEditHistory().pop();
        ReadOnlyTask edittedTask = history.getAfterEditHistory().pop();
        saveTargetTaskForRedo(originalTask);
        saveEditedTargetTaskForRedo(edittedTask);
        model.updateTask(edittedTask, originalTask);
        fullMessage = MESSAGE_SUCCESS.concat(EditCommand.MESSAGE_EDIT_TASK_SUCCESS);
        return new CommandResult(String.format(fullMessage, edittedTask));
    }

    /**
     * Store Target Task to be edited during RedoCommand to reverse action of this current UndoCommand
     * Create a new Task/TaskWithDeadline depending on whether the task has deadline
     * @param originalTask must not be null
     */
    private void saveTargetTaskForRedo(ReadOnlyTask originalTask) {
        assert originalTask != null;
        if (originalTask.getDeadline().isPresent()) {
            undoHistory.addToAfterEditHistory(new TaskWithDeadline(originalTask));
        } else {
            undoHistory.addToAfterEditHistory(new Task(originalTask));
        }
    }

    /**
     * Store Editted Task that should be in model after RedoCommand.
     * Create a new Task/TaskWithDeadline depending on whether the task has deadline
     * @param edittedTask must not be null
     */
    private void saveEditedTargetTaskForRedo(ReadOnlyTask edittedTask) {
        assert edittedTask != null;
        if (edittedTask.getDeadline().isPresent()) {
            undoHistory.addToBeforeEditHistory(new TaskWithDeadline(edittedTask));
        } else {
            undoHistory.addToBeforeEditHistory(new Task(edittedTask));
        }
    }

    /**
     * Deleting the task added and stores {@code taskToDelete} for RedoCommand
     */
    private CommandResult undoAddCommand() throws TaskNotFoundException {
        ReadOnlyTask taskToDelete = history.getAddHistory().pop();
        assert taskToDelete != null;

        undoHistory.addToDeleteHistory(taskToDelete); //Store Task for RedoCommand
        model.deleteTask(taskToDelete);
        fullMessage = MESSAGE_SUCCESS.concat(DeleteCommand.MESSAGE_DELETE_TASK_SUCCESS);
        return new CommandResult(String.format(fullMessage, taskToDelete));
    }
}
