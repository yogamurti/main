package teamthree.twodo.logic.commands;

import static java.util.Objects.requireNonNull;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_DEADLINE_END;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_DEADLINE_START;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_NAME;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_TAG;

import teamthree.twodo.commons.core.EventsCenter;
import teamthree.twodo.commons.events.model.AddOrEditCommandExecutedEvent;
import teamthree.twodo.logic.commands.exceptions.CommandException;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.model.task.TaskWithDeadline;
import teamthree.twodo.model.task.exceptions.DuplicateTaskException;

/**
 * Adds a task to the TaskBook.
 */
public class AddCommand extends Command {

    //Command word can be any one of the three
    public static final String COMMAND_WORD = "add";
    public static final String COMMAND_WORD_QUICK = "+";
    public static final String COMMAND_WORD_UNIXSTYLE = "-a";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a Task that you need 2Do. " + "Parameters: "
            + PREFIX_NAME + "TASK " + "Example: " + COMMAND_WORD + " " + PREFIX_NAME + " Buy some lotion "
            + PREFIX_DESCRIPTION + " Must be water-based\n" + "Example for deadline: " + PREFIX_NAME + "Buy some lotion"
            + PREFIX_DEADLINE_END + " friday 10am\n" + "Example for events: " + PREFIX_NAME + "Attend ComicCon"
            + PREFIX_DEADLINE_START + " friday 10am " + PREFIX_DEADLINE_END + "friday 10pm\n" + PREFIX_TAG + "Otaku";

    public static final String MESSAGE_SUCCESS = "New task added: %1$s\n";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in your 2Do list";

    private final Task toAdd;

    // Creates an AddCommand to add the specified {@code ReadOnlyTask}
    public AddCommand(ReadOnlyTask task) {
        if (task instanceof TaskWithDeadline) {
            toAdd = new TaskWithDeadline(task);
        } else {
            toAdd = new Task(task);
        }
    }

    @Override
    public CommandResult execute() throws CommandException {
        requireNonNull(model);
        try {
            model.addTask(toAdd);
            history.addToAddHistory(toAdd);
            EventsCenter.getInstance().post(new AddOrEditCommandExecutedEvent(AddOrEditCommandExecutedEvent.ADD_EVENT));
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (DuplicateTaskException e) {
            throw new CommandException(MESSAGE_DUPLICATE_TASK);
        }

    }
    @Override
    public String toString() {
        return this.toAdd.getAsText();
    }
    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand)) {
            return false;
        }

        // state check
        AddCommand temp = (AddCommand) other;
        return this.toString().equals(temp.toString());
    }


}
