//@@author A0139267W
package teamthree.twodo.logic.commands;

import static java.util.Objects.requireNonNull;

import teamthree.twodo.commons.core.options.Options;
// import teamthree.twodo.commons.core.EventsCenter;
// import teamthree.twodo.commons.core.Messages;

// import teamthree.twodo.logic.commands.exceptions.CommandException;
import teamthree.twodo.logic.commands.exceptions.CommandException;

// Edits the global options settings
public class OptionsCommand extends Command {

    // Command word can be either one of the two
    public static final String COMMAND_WORD = "option";
    public static final String COMMAND_WORD_UNIXSTYLE = "-o";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits 2Do's default options.\n"
            + "Parameters: ALARM (minutes or days),"
            + " MARK COMPLETION AUTOMATICALLY (true or false)\n"
            + "Example: " + COMMAND_WORD + " a/1 minute m/true ";

    public static final String MESSAGE_UPDATE_OPTIONS_SUCCESS = "Default settings updated:%1$s\n";
    public static final String MESSAGE_DUPLICATE_OPTIONS = "The default settings "
            + "set are the same as the current settings";

    private final Options option;

    public OptionsCommand(Options options) {
        this.option = options;
    }

    @Override
    public CommandResult execute() throws CommandException {
        requireNonNull(optionsPrefs);
        if (option.equals(optionsPrefs)) {
            throw new CommandException(MESSAGE_DUPLICATE_OPTIONS);
        }
        if (!option.getAlarm().isEmpty() && !option.getAlarm().equals(optionsPrefs.getAlarm())) {
            optionsPrefs.editAlarm(option.getAlarm());
        } else {
            optionsPrefs.editAlarm(optionsPrefs.getAlarm());
        }
        if (!option.getAutoMark().isEmpty() && !option.getAutoMark().equals(optionsPrefs.getAutoMark())) {
            optionsPrefs.editAutoMark(option.getAutoMark());
        } else {
            optionsPrefs.editAutoMark(optionsPrefs.getAutoMark());
        }
        // history.addToAddHistory(toAdd);
        // EventsCenter.getInstance().post(new asEvent(AddOrEditCommandExecutedEvent.ADD_EVENT));
        return new CommandResult(String.format(MESSAGE_UPDATE_OPTIONS_SUCCESS, optionsPrefs));
    }

}
