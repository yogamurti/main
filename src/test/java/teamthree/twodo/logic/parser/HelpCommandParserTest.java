package teamthree.twodo.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import teamthree.twodo.commons.core.Messages;
import teamthree.twodo.logic.commands.AddCommand;
import teamthree.twodo.logic.commands.HelpCommand;
import teamthree.twodo.logic.parser.exceptions.ParseException;

public class HelpCommandParserTest {
    private HelpCommandParser parser = new HelpCommandParser();
    @Test
    public void parseCommandSuccess() throws ParseException {
        HelpCommand actual = parser.parse(AddCommand.COMMAND_WORD);
        HelpCommand expected = new HelpCommand(AddCommand.MESSAGE_USAGE);
        assertTrue(actual.equals(expected));
    }
    @Test
    public void parseCommandThrowsParseException() {
        try {
            String userInput = "abdc";
            parser.parse(userInput);
            fail("An exception should have been thrown.");
        } catch (ParseException pe) {
            assertEquals(Messages.MESSAGE_UNKNOWN_COMMAND, pe.getMessage());
        }
    }
}
