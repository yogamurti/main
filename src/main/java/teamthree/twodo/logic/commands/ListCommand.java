package teamthree.twodo.logic.commands;

import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_DEADLINE_END;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_DEADLINE_START;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_NAME;

import teamthree.twodo.model.task.Deadline;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";
    public static final String COMMAND_WORD_UNIXSTYLE = "-l";

    public static final String MESSAGE_SUCCESS = "Listed all tasks";
    public static final String MESSAGE_SUCCESS_START = "Listed all tasks after ";
    public static final String MESSAGE_SUCCESS_END = "Listed all tasks before ";
    public static final String MESSAGE_SUCCESS_BOTH = "Listed all tasks between ";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists tasks. " + "Parameters: "
            + PREFIX_DEADLINE_START + "03/05/17 " + PREFIX_DEADLINE_END + "03/06/17 "
            + "Example: " + COMMAND_WORD + " " + PREFIX_NAME + "Buy some lotion.";
    private Deadline deadline;
    public enum AttributeInputted { START, END, BOTH };

    private AttributeInputted attInput;

    public ListCommand() {
        this.deadline = null;
    }

    public ListCommand(Deadline deadline, AttributeInputted attInput) {
        this.deadline = deadline;
        this.attInput = attInput;
    }

    @Override
    public CommandResult execute() {
        if (deadline == null) {
            model.updateFilteredListToShowAll();
            return new CommandResult(MESSAGE_SUCCESS);
        } else {
            model.updateFilteredListToShowPeriod(deadline, attInput);
            String message;
            switch (attInput) {
            case START:
                message = MESSAGE_SUCCESS_START + deadline.getStartDate();
                break;
            case END:
                message = MESSAGE_SUCCESS_END + deadline.getEndDate();
                break;
            case BOTH:
                message = MESSAGE_SUCCESS_BOTH + deadline.getStartDate() + " and " + deadline.getEndDate();
                break;
            default:
                message = MESSAGE_SUCCESS;
                break;
            }
            return new CommandResult(message);
        }
    }
}
