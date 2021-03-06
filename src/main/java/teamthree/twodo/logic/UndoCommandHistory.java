package teamthree.twodo.logic;

import static java.util.Objects.requireNonNull;
import static teamthree.twodo.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Stack;
import java.util.regex.Matcher;

import teamthree.twodo.logic.commands.HelpCommand;
import teamthree.twodo.logic.parser.Parser;
import teamthree.twodo.logic.parser.exceptions.ParseException;
import teamthree.twodo.model.task.ReadOnlyTask;

//@@author A0162253M
// Stores the history of Undo commands executed.
public class UndoCommandHistory {
    private Stack<String> userInputHistory;
    private Stack<ReadOnlyTask> beforeEditHistory;
    private Stack<ReadOnlyTask> afterEditHistory;
    private Stack<ReadOnlyTask> deleteHistory;
    private Stack<ReadOnlyTask> addHistory;
    private Stack<ReadOnlyTask> markHistory;
    private Stack<ReadOnlyTask> unmarkHistory;
    private Stack<ReadOnlyTask> undoHistory;

    public UndoCommandHistory() {
        beforeEditHistory = new Stack<ReadOnlyTask>();
        afterEditHistory = new Stack<ReadOnlyTask>();
        addHistory = new Stack<ReadOnlyTask>();
        deleteHistory = new Stack<ReadOnlyTask>();
        markHistory = new Stack<ReadOnlyTask>();
        unmarkHistory = new Stack<ReadOnlyTask>();
        userInputHistory = new Stack<String>();
        undoHistory = new Stack<ReadOnlyTask>();
    }

    /**
     * Appends {@code userInput} to the list of user input entered.
     * @throws ParseException
     */
    public void addToUserInputHistory(String userInput) throws ParseException {
        requireNonNull(userInput);
        String commandWord = getCommandWordFromInput(userInput);
        getUserInputHistory().push(commandWord);
    }

    /**
     * Appends {@code task} to the list of task before edits entered.
     */
    public void addToBeforeEditHistory(ReadOnlyTask task) {
        requireNonNull(task);
        beforeEditHistory.push(task);
    }

    /**
     * Appends {@code task} to the list of task after edits entered.
     */
    public void addToAfterEditHistory(ReadOnlyTask task) {
        requireNonNull(task);
        afterEditHistory.push(task);
    }

    /**
     * Appends {@code task} to the list of added tasks entered.
     */
    public void addToAddHistory(ReadOnlyTask task) {
        requireNonNull(task);
        addHistory.push(task);
    }

    /**
     * Appends {@code task} to the list of deleted tasks entered.
     */
    public void addToDeleteHistory(ReadOnlyTask task) {
        requireNonNull(task);
        deleteHistory.push(task);
    }

    /**
     * Appends {@code task} to the list of marked tasks entered.
     */
    public void addToMarkHistory(ReadOnlyTask task) {
        requireNonNull(task);
        markHistory.push(task);
    }

    /**
     * Appends {@code task} to the list of unmarked tasks entered.
     */
    public void addToUnmarkHistory(ReadOnlyTask task) {
        requireNonNull(task);
        unmarkHistory.push(task);
    }

    public void addToUndoHistory(ReadOnlyTask task) {
        requireNonNull(task);
        undoHistory.push(task);
    }

    public Stack<String> getUserInputHistory() {
        requireNonNull(userInputHistory);
        return userInputHistory;
    }

    public Stack<ReadOnlyTask> getBeforeEditHistory() {
        requireNonNull(beforeEditHistory);
        return beforeEditHistory;
    }

    public Stack<ReadOnlyTask> getAfterEditHistory() {
        requireNonNull(afterEditHistory);
        return afterEditHistory;
    }

    public Stack<ReadOnlyTask> getAddHistory() {
        requireNonNull(addHistory);
        return addHistory;
    }

    public Stack<ReadOnlyTask> getDeleteHistory() {
        requireNonNull(deleteHistory);
        return deleteHistory;
    }

    public Stack<ReadOnlyTask> getMarkHistory() {
        requireNonNull(markHistory);
        return markHistory;
    };

    public Stack<ReadOnlyTask> getUndokHistory() {
        requireNonNull(undoHistory);
        return undoHistory;
    };

    public Stack<ReadOnlyTask> getUnmarkHistory() {
        requireNonNull(unmarkHistory);
        return unmarkHistory;
    }

    private String getCommandWordFromInput(String userInput) throws ParseException {
        final Matcher matcher = Parser.BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        return commandWord;
    }
}
