package teamthree.twodo.logic.commands;

import static java.util.Objects.requireNonNull;

import teamthree.twodo.automark.AutoMarkManager;
import teamthree.twodo.commons.core.Config;
import teamthree.twodo.commons.core.options.Alarm;
import teamthree.twodo.commons.core.options.AutoMark;
import teamthree.twodo.commons.core.options.Options;
import teamthree.twodo.logic.commands.exceptions.CommandException;

//@@author A0139267W
// Edits the global options settings
public class OptionsCommand extends Command {

    // Command word can be either one of the two
    public static final String COMMAND_WORD = "option";
    public static final String COMMAND_WORD_FAST = "o";

    public static final String MESSAGE_USAGE = "Current options: "
            + "Alarm = " + Config.defaultNotificationPeriodToString()
            + " Automark = " + Boolean.toString(AutoMarkManager.getSetToRun()) + "\n"
            + COMMAND_WORD + ": Edits 2Do's default options.\n"
            + "Parameters: alarm (minutes or days),"
            + " mark completion automatically (true or false)\n"
            + "Example: " + COMMAND_WORD + " a/1 minute m/true ";

    public static final String MESSAGE_UPDATE_OPTIONS_SUCCESS = "Default settings updated:%1$s\n";
    public static final String MESSAGE_DUPLICATE_OPTIONS = "The default settings "
            + "set are the same as the current settings";

    private final Options defaultOption;
    private final Options option;

    public OptionsCommand(Options option) {
        this.option = option;
        defaultOption = getDefaultOption();
    }

    @Override
    public CommandResult execute() throws CommandException {
        requireNonNull(defaultOption);
        if (option.equals(defaultOption)) {
            throw new CommandException(MESSAGE_DUPLICATE_OPTIONS);
        }
        if (!option.getAlarm().equals(defaultOption.getAlarm())) {
            Config.changeDefaultNotificationPeriod(option.getAlarm().getValue());
            defaultOption.editAlarm(option.getAlarm());
            // Checks if the alarm updates were properly executed for both components
            assert(Config.defaultNotificationPeriodToString() == defaultOption.getAlarm().getValue());
        }
        if (!option.getAutoMark().equals(defaultOption.getAutoMark())) {
            AutoMarkManager.setToRun(option.getAutoMark().getValue());
            defaultOption.editAutoMark(option.getAutoMark());
            // Checks if the alarm updates were properly executed for both components
            assert(AutoMarkManager.getSetToRun() == defaultOption.getAutoMark().getValue());
        }

        model.changeOptions();
        history.addToOptionsHistory(option);
        return new CommandResult(String.format(MESSAGE_UPDATE_OPTIONS_SUCCESS, defaultOption));
    }

    private Options getDefaultOption() {
        Alarm alarm = new Alarm(Config.defaultNotificationPeriodToString());
        AutoMark autoMark = new AutoMark(AutoMarkManager.getSetToRun());
        return new Options(alarm, autoMark);
    }

    public Options getOption() {
        return option;
    }
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof OptionsCommand // instanceof handles nulls
                && this.option.equals(((OptionsCommand) other).getOption())); // state check
    }
}
