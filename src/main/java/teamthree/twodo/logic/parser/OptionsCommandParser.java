package teamthree.twodo.logic.parser;

import static teamthree.twodo.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_AUTOMARK;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_NOTIFICATION_PERIOD;

import java.util.stream.Stream;

import teamthree.twodo.automark.AutoMarkManager;
import teamthree.twodo.commons.core.Config;
import teamthree.twodo.commons.core.options.Alarm;
import teamthree.twodo.commons.core.options.AutoMark;
import teamthree.twodo.commons.core.options.Options;
import teamthree.twodo.commons.exceptions.IllegalValueException;
import teamthree.twodo.logic.commands.OptionsCommand;
import teamthree.twodo.logic.parser.exceptions.ParseException;

//@@author A0139267W
// Parses input arguments and creates a new OptionsCommand object
public class OptionsCommandParser implements CommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the OptionsCommand
     * and returns a OptionsCommand object for execution.
     *
     * @throws ParseException
     *             if the user input does not conform the expected format
     */

    public OptionsCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NOTIFICATION_PERIOD, PREFIX_AUTOMARK);
        if (!arePrefixesPresent(argMultimap, PREFIX_NOTIFICATION_PERIOD)
                && !arePrefixesPresent(argMultimap, PREFIX_AUTOMARK)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, OptionsCommand.MESSAGE_USAGE));
        }
        try {
            Alarm alarm = ParserUtil.parseAlarm(argMultimap.getValue(PREFIX_NOTIFICATION_PERIOD))
                    .orElse(new Alarm(Config.defaultNotificationPeriodToString()));
            AutoMark autoMark = ParserUtil.parseAutoMark(argMultimap.getValue(PREFIX_AUTOMARK))
                    .orElse(new AutoMark(AutoMarkManager.getSetToRun()));
            Options option = new Options(alarm, autoMark);
            return new OptionsCommand(option);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional}
     * values in the given {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
