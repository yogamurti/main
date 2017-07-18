package teamthree.twodo.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static teamthree.twodo.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import org.junit.Test;

import teamthree.twodo.logic.commands.HelpCommand;
import teamthree.twodo.logic.commands.UndoCommand;
import teamthree.twodo.logic.parser.exceptions.ParseException;

public class ParserTest {
    private final Parser parser = new Parser();
    @Test
    public void parseCommand_help() throws ParseException {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " add") instanceof HelpCommand);
    }
    @Test
    public void parseCommand_history() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD) instanceof UndoCommand);
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD + " 3") instanceof UndoCommand);

        try {
            parser.parseCommand("histories");
            fail("The expected ParseException was not thrown.");
        } catch (ParseException pe) {
            assertEquals(MESSAGE_UNKNOWN_COMMAND, pe.getMessage());
        }
    }
}
