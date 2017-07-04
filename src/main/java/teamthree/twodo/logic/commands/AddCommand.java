package teamthree.twodo.logic.commands;

import static java.util.Objects.requireNonNull;
//import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_ADDRESS;
//import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_EMAIL;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_NAME;
//import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_PHONE;
//import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_TAG;

import teamthree.twodo.logic.commands.exceptions.CommandException;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.model.task.exceptions.DuplicateTaskException;

/**
 * Adds a person to the address book.
 */
public class AddCommand extends Command {

    //Command word can be any one of the three
    public static final String COMMAND_WORD = "add";

    /*    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person to the address book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_TAG + "friends "
            + PREFIX_TAG + "owesMoney";
*/
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a Task that you need 2Do. "
            + "Parameters: "
            + PREFIX_NAME + "TASK "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Buy some lotion.";

    public static final String MESSAGE_SUCCESS = "New task added: %1$s";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in your 2Do list";

    private final Task toAdd;

    /**
     * Creates an AddCommand to add the specified {@code ReadOnlyTask}
     */
    public AddCommand(ReadOnlyTask person) {
        toAdd = new Task(person);
    }

    @Override
    public CommandResult execute() throws CommandException {
        requireNonNull(model);
        try {
            model.addTask(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (DuplicateTaskException e) {
            throw new CommandException(MESSAGE_DUPLICATE_TASK);
        }

    }

}
