package teamthree.twodo.logic.parser;

import static org.junit.Assert.assertEquals;
import static teamthree.twodo.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_AUTOMARK;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_NOTIFICATION_PERIOD;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import teamthree.twodo.automark.AutoMarkManager;
import teamthree.twodo.commons.core.Config;

import teamthree.twodo.commons.core.options.Alarm;
import teamthree.twodo.commons.core.options.AutoMark;
import teamthree.twodo.commons.core.options.Options;

import teamthree.twodo.logic.commands.OptionsCommand;

import teamthree.twodo.logic.parser.exceptions.ParseException;

//@@author A0139267W
public class OptionsCommandParserTest {
    private static final String MESSAGE_INVALID_FORMAT = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
            OptionsCommand.MESSAGE_USAGE);
    private static final String VALID_ALARM_WITHOUT_PREFIX = "2 days";
    private static final String VALID_ALARM_WITH_PREFIX = PREFIX_NOTIFICATION_PERIOD
            + VALID_ALARM_WITHOUT_PREFIX;
    private static final String VALID_AUTOMARK_WITHOUT_PREFIX = "true";
    private static final String VALID_AUTOMARK_WITH_PREFIX = PREFIX_AUTOMARK
            + VALID_AUTOMARK_WITHOUT_PREFIX;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private OptionsCommandParser parser = new OptionsCommandParser();

    @Test
    public void parse_missingPrefix_failure() throws ParseException {
        assertParseFailure(VALID_ALARM_WITHOUT_PREFIX, MESSAGE_INVALID_FORMAT);
        assertParseFailure(VALID_AUTOMARK_WITHOUT_PREFIX, MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parseAlarmOnlySuccess() throws ParseException {
        String userInput = " " + VALID_ALARM_WITH_PREFIX;
        Options expected = new Options(new Alarm(VALID_ALARM_WITHOUT_PREFIX),
                new AutoMark(AutoMarkManager.getSetToRun()));
        OptionsCommand expectedCommand = new OptionsCommand(expected);
        assertParseSuccess(userInput, expectedCommand);
    }

    @Test
    public void parseAutoMarkOnlySuccess() throws ParseException {
        String userInput = " " + VALID_AUTOMARK_WITH_PREFIX;
        Options expected = new Options(new Alarm(Config.defaultNotificationPeriodToString()),
                new AutoMark(Boolean.parseBoolean(VALID_AUTOMARK_WITHOUT_PREFIX)));
        OptionsCommand expectedCommand = new OptionsCommand(expected);
        assertParseSuccess(userInput, expectedCommand);
    }

    @Test
    public void parseAlarmAndAutoMarkSuccess() throws ParseException {
        String userInput = " " + VALID_AUTOMARK_WITH_PREFIX + " " + VALID_ALARM_WITH_PREFIX;
        Options expected = new Options(new Alarm(VALID_ALARM_WITHOUT_PREFIX),
                new AutoMark(Boolean.parseBoolean(VALID_AUTOMARK_WITHOUT_PREFIX)));
        OptionsCommand expectedCommand = new OptionsCommand(expected);
        assertParseSuccess(userInput, expectedCommand);
    }

    /**
     * Asserts the parsing of {@code userInput} is unsuccessful and the error
     * message equals to {@code expectedMessage}
     * @throws ParseException
     */
    private void assertParseFailure(String userInput, String expectedMessage) throws ParseException {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, OptionsCommand.MESSAGE_USAGE));

        parser.parse(userInput);
    }
    /**
     * Asserts the parsing of {@code userInput} is successful and the result
     * matches {@code expectedCommand}
     */
    private void assertParseSuccess(String userInput, OptionsCommand expectedCommand) throws ParseException {
        OptionsCommand command = parser.parse(userInput);
        assertEquals(command, expectedCommand);
    }
}
