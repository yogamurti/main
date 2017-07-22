package teamthree.twodo.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static teamthree.twodo.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static teamthree.twodo.logic.commands.FindCommand.COMMAND_WORD_HISTORY;
import static teamthree.twodo.logic.commands.FindCommand.MESSAGE_USAGE;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import teamthree.twodo.logic.commands.Command;
import teamthree.twodo.logic.commands.FindCommand;
import teamthree.twodo.logic.parser.exceptions.ParseException;

//author A0107433N
/**
* Tests parser() in FindCommandParser for different inputs
*/
public class FindCommandParserTest {

    public static final String INVALID_EMPTY = "";
    public static final String INVALID_EMPTY_HISTORY = " " + COMMAND_WORD_HISTORY;
    public static final String INVALID_SPACE = " ";

    public static final String VALID_NORMAL = "project";
    public static final String VALID_NORMAL_DOUBLE = "project school";
    public static final String VALID_NORMAL_HISTORY = " " + COMMAND_WORD_HISTORY + VALID_NORMAL;
    public static final String VALID_GIBBERISH = "@/ \f2e";

    private static final String MESSAGE_INVALID_FORMAT = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
            MESSAGE_USAGE);

    private FindCommandParser parser = new FindCommandParser();
    private FindCommand expectedCommand;

    @Test
    public void parseValidArguments() throws Exception {
        //Test incomplete tasks
        expectedCommand = prepareCommand(VALID_NORMAL, true);
        assertParseSuccess(VALID_NORMAL, expectedCommand);

        //Test complete tasks
        expectedCommand = prepareCommand(VALID_NORMAL, false);
        assertParseSuccess(VALID_NORMAL_HISTORY, expectedCommand);

        //Test non-existent task
        expectedCommand = prepareCommand(VALID_GIBBERISH, true);
        assertParseSuccess(VALID_GIBBERISH, expectedCommand);

        //Test multiple searches
        expectedCommand = prepareCommand(VALID_NORMAL_DOUBLE, true);
        assertParseSuccess(VALID_NORMAL_DOUBLE, expectedCommand);
    }

    @Test
    public void parseInvalidArgument() throws Exception {
        //Test incomplete tasks
        expectedCommand = prepareCommand(INVALID_EMPTY, true);
        assertParseFailure(INVALID_EMPTY, MESSAGE_INVALID_FORMAT);

        expectedCommand = prepareCommand(INVALID_EMPTY, true);
        assertParseFailure(INVALID_SPACE, MESSAGE_INVALID_FORMAT);

        //Test incomplete tasks
        expectedCommand = prepareCommand(INVALID_EMPTY, false);
        assertParseFailure(INVALID_EMPTY_HISTORY, MESSAGE_INVALID_FORMAT);
    }

    /**
     * Asserts the parsing of {@code userInput} is successful and the result
     * matches {@code expectedCommand}
     */
    private void assertParseSuccess(String userInput, FindCommand expectedCommand) throws Exception {
        Command command = parser.parse(userInput);
        assertTrue(expectedCommand.equals(command));
    }

    /**
     * Asserts the parsing of {@code userInput} is unsuccessful and the error
     * message equals to {@code expectedMessage}
     */
    private void assertParseFailure(String userInput, String expectedMessage) {
        try {
            parser.parse(userInput);
            fail("An exception should have been thrown.");
        } catch (ParseException pe) {
            assertEquals(expectedMessage, pe.getMessage());
        }

    }

    private FindCommand prepareCommand(String input, boolean listIncomplete) {
        String[] keywords = input.trim().split("\\s+");
        Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));
        return new FindCommand(keywordSet, listIncomplete);
    }
}
