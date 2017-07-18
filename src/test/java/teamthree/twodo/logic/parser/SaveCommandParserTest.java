package teamthree.twodo.logic.parser;

import static org.junit.Assert.assertEquals;
import static teamthree.twodo.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import teamthree.twodo.logic.commands.SaveCommand;
import teamthree.twodo.logic.parser.exceptions.ParseException;

public class SaveCommandParserTest {

    private static final String VALID_FILENAME = "Documents\taskbook\2Do.xml";
    private static final String INVALID_FILENAME = "C:/";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private SaveCommandParser parser = new SaveCommandParser();

    @Test
    public void parseValidArgsReturnsSaveCommand() throws Exception {
        SaveCommand command = parser.parse(VALID_FILENAME);
        assertEquals(VALID_FILENAME, command.filePath);
    }

    @Test
    public void parseInvalidArgsThrowsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SaveCommand.MESSAGE_USAGE));

        parser.parse(INVALID_FILENAME);
    }

}
