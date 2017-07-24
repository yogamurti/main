package teamthree.twodo.logic.commands;

import java.util.Set;

//@@author A0107433N
/**
 * Finds and lists all tasks in task book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";
    public static final String COMMAND_WORD_FAST = "f";
    public static final String COMMAND_WORD_HISTORY = "/h";

    public static final String MESSAGE_SUCCESS_INCOMPLETE = "%1$s incomplete tasks found";
    public static final String MESSAGE_SUCCESS_COMPLETE = "%1$s complete tasks found";
    public static final String MESSAGE_EMPTY_LIST = "No tasks found\n"
            + "Try finding for a different keyword";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all incomplete tasks whose names, descriptions, "
            + "or tags contain any of the specified keywords and displays them as a list with index numbers.\n"
            + "Add " + COMMAND_WORD_HISTORY
            + " to find completed tasks instead of incomplete tasks containing the keywords.\n"
            + "Parameters: [+ " + COMMAND_WORD_HISTORY + "] {KEYWORD} [MORE_KEYWORDS...]\n"
            + "Example: " + COMMAND_WORD + " " + COMMAND_WORD_HISTORY + " cs school";

    private final Set<String> keyWords;
    private boolean listIncomplete;

    public FindCommand(Set<String> keywords, boolean listIncomplete) {
        this.keyWords = keywords;
        this.listIncomplete = listIncomplete;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredTaskListByKeywords(keyWords, listIncomplete);
        return new CommandResult(printSuccessMessage());
    }

    private String printSuccessMessage() {
        if (model.getFilteredAndSortedTaskList().size() == 0) {
            return MESSAGE_EMPTY_LIST;
        }
        if (listIncomplete) {
            return String.format(MESSAGE_SUCCESS_INCOMPLETE, model.getFilteredAndSortedTaskList().size());
        } else {
            return String.format(MESSAGE_SUCCESS_COMPLETE, model.getFilteredAndSortedTaskList().size());
        }
    }

    @Override
    public String toString() {
        Boolean incomplete = listIncomplete;
        return keyWords.toString() + incomplete;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        // state check
        return this.toString().equals(other.toString());
    }

}
