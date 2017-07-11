package teamthree.twodo.logic.commands;

import static java.util.Objects.requireNonNull;
import static teamthree.twodo.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.List;
import java.util.Stack;

import teamthree.twodo.logic.CommandHistory;
import teamthree.twodo.logic.commands.exceptions.CommandException;
import teamthree.twodo.logic.parser.AddCommandParser;
import teamthree.twodo.logic.parser.DeleteCommandParser;
import teamthree.twodo.logic.parser.EditCommandParser;
import teamthree.twodo.logic.parser.FindCommandParser;
import teamthree.twodo.logic.parser.HelpCommandParser;
import teamthree.twodo.logic.parser.MarkCommandParser;
import teamthree.twodo.logic.parser.SaveCommandParser;
import teamthree.twodo.logic.parser.SelectCommandParser;
import teamthree.twodo.logic.parser.ViewCommandParser;
import teamthree.twodo.logic.parser.exceptions.ParseException;
import teamthree.twodo.model.Model;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.exceptions.DuplicateTaskException;
import teamthree.twodo.model.task.exceptions.TaskNotFoundException;

/**
 * Lists all the commands entered by user from the start of app launch.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";
    public static final String COMMAND_WORD_UNIXSTYLE = "-u";
    public static final String MESSAGE_SUCCESS = "Successfully undo command!!!\n";
    public static final String MESSAGE_NO_HISTORY = "Failed to undo: You have not yet entered any commands.";
    public static final String MESSAGE_INVALID_PREVIOUS_COMMAND = "Failed to undo: Invalid previous command ";
    
    public static String FULL_MESSAGE;
    
    @Override
    public CommandResult execute() throws CommandException  {
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
        }
        
        return undoResult;
    }
    
    private CommandResult processUserInput() throws TaskNotFoundException, DuplicateTaskException {
      final String previousCommandWord = history.getUserInputHistory().pop();
      switch (previousCommandWord) {

      case AddCommand.COMMAND_WORD:
      case AddCommand.COMMAND_WORD_QUICK:
      case AddCommand.COMMAND_WORD_UNIXSTYLE:
        ReadOnlyTask taskToDelete = history.getAddHistory().pop();
        model.deleteTask(taskToDelete);
        FULL_MESSAGE = MESSAGE_SUCCESS.concat(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS);
        return new CommandResult(String.format(FULL_MESSAGE, taskToDelete));
      
      case EditCommand.COMMAND_WORD:
        ReadOnlyTask taskToEdit= history.getBeforeEditHistory().pop();
        ReadOnlyTask originalTask= history.getAfterEditHistory().pop();
        model.updateTask(originalTask, taskToEdit);
        FULL_MESSAGE = MESSAGE_SUCCESS.concat(EditCommand.MESSAGE_EDIT_TASK_SUCCESS);
          return new CommandResult(String.format(FULL_MESSAGE, taskToEdit));

      case DeleteCommand.COMMAND_WORD_QUICK:
      case DeleteCommand.COMMAND_WORD_UNIXSTYLE:
      case DeleteCommand.COMMAND_WORD_SHORT:
      case DeleteCommand.COMMAND_WORD:
        ReadOnlyTask taskToAdd= history.getDeleteHistory().pop();
        model.addTask(taskToAdd);
        FULL_MESSAGE = MESSAGE_SUCCESS.concat(AddCommand.MESSAGE_SUCCESS);
        return new CommandResult(String.format(FULL_MESSAGE, taskToAdd));

      case ClearCommand.COMMAND_WORD:
        FULL_MESSAGE = MESSAGE_SUCCESS.concat("Restored TaskBook");
        return new CommandResult(FULL_MESSAGE);

      case MarkCommand.COMMAND_WORD:
      case MarkCommand.COMMAND_WORD_UNIXSTYLE:
        ReadOnlyTask taskToMark= history.getMarkHistory().pop();
        //TOADD: model.unmarkTask(taskToMark)
        //TOADD:FULL_MESSAGE = MESSAGE_SUCCESS.concat(MarkCommand.MESSAGE_UNMARK_PERSON_SUCCESS);
        return new CommandResult(FULL_MESSAGE);
      
      default:
        String message = MESSAGE_INVALID_PREVIOUS_COMMAND.concat(history.getUserInputHistory().peek());
        return new CommandResult(message);
      }
    }
}
