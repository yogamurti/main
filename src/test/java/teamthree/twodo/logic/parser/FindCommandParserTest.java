package teamthree.twodo.logic.parser;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import teamthree.twodo.logic.commands.Command;
import teamthree.twodo.logic.commands.FindCommand;

public class FindCommandParserTest {

    public static final String INVALID_EMPTY = "";

    public static final String VALID_NORMAL = "project";
    public static final String VALID_GIBBERISH = "@/ \f2e";

    private FindCommandParser parser = new FindCommandParser();
    private FindCommand expectedCommand;

    @Test
    public void parseValidArguments() {
        expectedCommand = new FindCommand(null, false);
    }

    /**
     * Asserts the parsing of {@code userInput} is successful and the result
     * matches {@code expectedCommand}
     */
    private void assertParseSuccess(String userInput, FindCommand expectedCommand) throws Exception {
        Command command = parser.parse(userInput);
        assertTrue(expectedCommand.equals(command));
    }
}
