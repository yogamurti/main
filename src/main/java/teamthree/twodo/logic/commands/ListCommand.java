package teamthree.twodo.logic.commands;

import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_DEADLINE_END;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_DEADLINE_START;

import teamthree.twodo.model.task.Deadline;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";
    public static final String COMMAND_WORD_UNIXSTYLE = "-l";

    public static final String COMMAND_WORD_HISTORY = "-h";

    public static final String MESSAGE_SUCCESS_INCOMPLETE = "Listed all incomplete tasks";
    public static final String MESSAGE_SUCCESS_COMPLETE = "Listed all completed tasks";
    public static final String MESSAGE_SUCCESS_INCOMPLETE_START = "Listed all incomplete tasks after ";
    public static final String MESSAGE_SUCCESS_COMPLETE_START = "Listed all completed tasks after ";
    public static final String MESSAGE_SUCCESS_INCOMPLETE_END = "Listed all incomplete tasks before ";
    public static final String MESSAGE_SUCCESS_COMPLETE_END = "Listed all completed tasks before ";
    public static final String MESSAGE_SUCCESS_INCOMPLETE_BOTH = "Listed all incomplete tasks between ";
    public static final String MESSAGE_SUCCESS_COMPLETE_BOTH = "Listed all completed tasks between ";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists all incomplete tasks within specified period.\n"
            + "If only start date specified, list all tasks after start date. "
            + "If only end date specified, list all tasks before end date\n"
            + "Add " + COMMAND_WORD_HISTORY
            + " to list completed tasks instead of incomplete tasks within specified period.\n"
            + "Parameters: [" + COMMAND_WORD_HISTORY + "] " + PREFIX_DEADLINE_START + "[START] "
            + PREFIX_DEADLINE_END + "[END]\n"
            + "Example: " + COMMAND_WORD + " " + COMMAND_WORD_HISTORY + " " + PREFIX_DEADLINE_START + "today "
            + PREFIX_DEADLINE_END + "next week";

    private Deadline deadline;
    public enum AttributeInputted { START, END, BOTH };

    private AttributeInputted attInput;
    private boolean listIncomplete;

    public ListCommand(boolean listIncomplete) {
        this.deadline = null;
        this.listIncomplete = listIncomplete;
    }

    public ListCommand(Deadline deadline, AttributeInputted attInput, boolean listIncomplete) {
        this.deadline = deadline;
        this.attInput = attInput;
        this.listIncomplete = listIncomplete;
    }

    @Override
    public CommandResult execute() {
        if (deadline == null) {
            if (listIncomplete) {
                model.updateFilteredListToShowAllIncomplete();
                return new CommandResult(MESSAGE_SUCCESS_INCOMPLETE);
            } else {
                model.updateFilteredListToShowAllComplete();
                return new CommandResult(MESSAGE_SUCCESS_COMPLETE);
            }
        } else {
            model.updateFilteredListToShowPeriod(deadline, attInput, listIncomplete);
            String message;
            switch (attInput) {
            case START:
                if (listIncomplete) {
                    message = MESSAGE_SUCCESS_INCOMPLETE_START + deadline.getStartDate();
                } else {
                    message = MESSAGE_SUCCESS_COMPLETE_START + deadline.getStartDate();
                }
                break;
            case END:
                if (listIncomplete) {
                    message = MESSAGE_SUCCESS_INCOMPLETE_END + deadline.getEndDate();
                } else {
                    message = MESSAGE_SUCCESS_COMPLETE_END + deadline.getEndDate();
                }
                break;
            case BOTH:
                if (listIncomplete) {
                    message = MESSAGE_SUCCESS_INCOMPLETE_BOTH + deadline.getStartDate()
                        + " and " + deadline.getEndDate();
                } else {
                    message = MESSAGE_SUCCESS_COMPLETE_BOTH + deadline.getStartDate() + " and " + deadline.getEndDate();
                }
                break;
            default:
                if (listIncomplete) {
                    message = MESSAGE_SUCCESS_INCOMPLETE;
                } else {
                    message = MESSAGE_SUCCESS_COMPLETE;
                }
                break;
            }
            return new CommandResult(message);
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ListCommand)) {
            return false;
        }

        // state check
        ListCommand temp = (ListCommand) other;
        return this.toString().equals(temp.toString());
    }
}
