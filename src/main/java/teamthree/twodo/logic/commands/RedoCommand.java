package teamthree.twodo.logic.commands;

import teamthree.twodo.logic.commands.exceptions.CommandException;
import teamthree.twodo.logic.parser.exceptions.ParseException;
import teamthree.twodo.model.TaskBook;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.exceptions.DuplicateTaskException;
import teamthree.twodo.model.task.exceptions.TaskNotFoundException;

/**
 * Redo the function that was undo in the previous command
 */
public class RedoCommand extends Command {

    public static final String COMMAND_WORD = "redo";
    public static final String COMMAND_WORD_UNIXSTYLE = "-r";
    public static final String MESSAGE_SUCCESS = "Successfully redo command!!!\n";
    public static final String MESSAGE_NO_HISTORY = "Failed to redo: You have not yet entered any undo commands.";
    public static final String MESSAGE_INVALID_PREVIOUS_COMMAND = "Failed to undo: Invalid previous command ";

    private static String fullMessage;

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
        case DeleteCommand.COMMAND_WORD_UNIXSTYLE:
        case DeleteCommand.COMMAND_WORD_SHORT:
            ReadOnlyTask taskToDelete = undoHistory.getAddHistory().pop();
            history.addToDeleteHistory(taskToDelete);
            model.deleteTask(taskToDelete);
            fullMessage = MESSAGE_SUCCESS.concat(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS);
            return new CommandResult(String.format(fullMessage, taskToDelete));

        case EditCommand.COMMAND_WORD:
            ReadOnlyTask taskToEdit = undoHistory.getBeforeEditHistory().pop();
            ReadOnlyTask currentTask = undoHistory.getAfterEditHistory().pop();
            history.addToBeforeEditHistory(currentTask);
            history.addToAfterEditHistory(taskToEdit);
            model.updateTask(currentTask, taskToEdit);
            fullMessage = MESSAGE_SUCCESS.concat(EditCommand.MESSAGE_EDIT_TASK_SUCCESS);
            return new CommandResult(String.format(fullMessage, taskToEdit));

        case AddCommand.COMMAND_WORD_QUICK:
        case AddCommand.COMMAND_WORD_UNIXSTYLE:
        case AddCommand.COMMAND_WORD:
            ReadOnlyTask taskToAdd = undoHistory.getDeleteHistory().pop();
            history.addToAddHistory(taskToAdd);
            model.addTask(taskToAdd);
            fullMessage = MESSAGE_SUCCESS.concat(AddCommand.MESSAGE_SUCCESS);
            return new CommandResult(String.format(fullMessage, taskToAdd));

        case ClearCommand.COMMAND_WORD:
            model.resetData(new TaskBook());
            fullMessage = MESSAGE_SUCCESS.concat("Restored TaskBook");
            return new CommandResult(fullMessage);

        case UnmarkCommand.COMMAND_WORD:
        case UnmarkCommand.COMMAND_WORD_UNIXSTYLE:
            ReadOnlyTask taskToUnmark = undoHistory.getMarkHistory().pop();
            history.addToUnmarkHistory(taskToUnmark);
            model.unmarkTask(taskToUnmark);
            fullMessage = MESSAGE_SUCCESS.concat(UnmarkCommand.MESSAGE_UNMARK_TASK_SUCCESS);
            return new CommandResult(String.format(fullMessage, taskToUnmark));

        case MarkCommand.COMMAND_WORD:
        case MarkCommand.COMMAND_WORD_UNIXSTYLE:
            ReadOnlyTask taskToMark = undoHistory.getUnmarkHistory().pop();
            history.addToMarkHistory(taskToMark);
            model.markTask(taskToMark);
            fullMessage = MESSAGE_SUCCESS.concat(MarkCommand.MESSAGE_MARK_TASK_SUCCESS);
            return new CommandResult(String.format(fullMessage, taskToMark));

        default:
            String message = MESSAGE_INVALID_PREVIOUS_COMMAND.concat(history.getUserInputHistory().peek());
            return new CommandResult(message);
        }
    }
}
