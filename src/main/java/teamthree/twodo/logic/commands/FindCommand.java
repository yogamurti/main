package teamthree.twodo.logic.commands;

import java.util.Set;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";
    public static final String COMMAND_WORD_UNIXSTYLE = "-f";

    public static final String COMMAND_WORD_HISTORY = "-h";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all incomplete tasks whose names, descriptions, "
            + "or tags contain any of the specified keywords and displays them as a list with index numbers.\n"
            + "Add -h to find completed tasks instead of incomplete tasks containing the keywords.\n"
            + "Parameters: [-h] KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " -h cs school";

    private final Set<String> keywords;
    private boolean listIncomplete;

    public FindCommand(Set<String> keywords, boolean listIncomplete) {
        this.keywords = keywords;
        this.listIncomplete = listIncomplete;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredTaskListExtensively(keywords, listIncomplete);
        return new CommandResult(printSuccessMessage());
    }

    private String printSuccessMessage() {
        if (listIncomplete) {
            return "Found " + model.getFilteredAndSortedTaskList().size() + " incomplete tasks";
        } else {
            return "Found " + model.getFilteredAndSortedTaskList().size() + " complete tasks";
        }
    }

}
