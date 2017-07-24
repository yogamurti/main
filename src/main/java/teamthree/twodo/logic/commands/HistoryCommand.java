package teamthree.twodo.logic.commands;

import java.util.List;

/**
 * Lists all the commands entered by user from the start of this 2Do session.
 */
public class HistoryCommand extends Command {

    public static final String COMMAND_WORD = "history";
    public static final String MESSAGE_SUCCESS = "Entered commands (from earliest to most recent):\n%1$s";
    public static final String MESSAGE_NO_HISTORY = "You have not yet entered any commands.";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows the command history.";

    @Override
    public CommandResult execute() {
        List<String> previousCommands = history.getHistory();

        if (previousCommands.isEmpty()) {
            return new CommandResult(MESSAGE_NO_HISTORY);
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, String.join("\n", previousCommands)));
    }
}
