package teamthree.twodo.logic.parser;

import static org.junit.Assert.assertEquals;
import static teamthree.twodo.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import teamthree.twodo.logic.commands.LoadCommand;
import teamthree.twodo.logic.parser.exceptions.ParseException;

public class LoadCommandParserTest {

    private static final String VALID_FILENAME = "Desktop\taskbook\2Do.xml";
    private static final String INVALID_FILENAME = "...";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private LoadCommandParser parser = new LoadCommandParser();

    @Test
    public void parse_validArgs_returnsLoadCommand() throws Exception {
        LoadCommand command = parser.parse(VALID_FILENAME);
        assertEquals(VALID_FILENAME, command.filePath);
    }

    @Test
    public void parse_invalidArgs_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, LoadCommand.MESSAGE_USAGE));

        parser.parse(INVALID_FILENAME);
    }

}
