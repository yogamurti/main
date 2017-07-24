package teamthree.twodo.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import teamthree.twodo.commons.core.Messages;
import teamthree.twodo.logic.commands.AddCommand;
import teamthree.twodo.logic.commands.ClearCommand;
import teamthree.twodo.logic.commands.DeleteCommand;
import teamthree.twodo.logic.commands.EditCommand;
import teamthree.twodo.logic.commands.ExitCommand;
import teamthree.twodo.logic.commands.FindCommand;
import teamthree.twodo.logic.commands.HelpCommand;
import teamthree.twodo.logic.commands.HistoryCommand;
import teamthree.twodo.logic.commands.ListCommand;
import teamthree.twodo.logic.commands.LoadCommand;
import teamthree.twodo.logic.commands.MarkCommand;
import teamthree.twodo.logic.commands.OptionsCommand;
import teamthree.twodo.logic.commands.RedoCommand;
import teamthree.twodo.logic.commands.SaveCommand;
import teamthree.twodo.logic.commands.UndoCommand;
import teamthree.twodo.logic.commands.UnmarkCommand;
import teamthree.twodo.logic.parser.exceptions.ParseException;

//@@author A0107433N
public class HelpCommandParserTest {

    private HelpCommandParser parser = new HelpCommandParser();

    @Test
    public void parseCommandSuccess() throws ParseException {
        HelpCommand actual;
        HelpCommand expected;

        actual = parser.parse(AddCommand.COMMAND_WORD);
        expected = new HelpCommand(AddCommand.MESSAGE_USAGE);
        assertTrue(actual.equals(expected));

        actual = parser.parse(ClearCommand.COMMAND_WORD);
        expected = new HelpCommand(ClearCommand.MESSAGE_USAGE);
        assertTrue(actual.equals(expected));

        actual = parser.parse(DeleteCommand.COMMAND_WORD);
        expected = new HelpCommand(DeleteCommand.MESSAGE_USAGE);
        assertTrue(actual.equals(expected));

        actual = parser.parse(EditCommand.COMMAND_WORD);
        expected = new HelpCommand(EditCommand.MESSAGE_USAGE);
        assertTrue(actual.equals(expected));

        actual = parser.parse(ExitCommand.COMMAND_WORD);
        expected = new HelpCommand(ExitCommand.MESSAGE_USAGE);
        assertTrue(actual.equals(expected));

        actual = parser.parse(FindCommand.COMMAND_WORD);
        expected = new HelpCommand(FindCommand.MESSAGE_USAGE);
        assertTrue(actual.equals(expected));

        actual = parser.parse(HistoryCommand.COMMAND_WORD);
        expected = new HelpCommand(HistoryCommand.MESSAGE_USAGE);
        assertTrue(actual.equals(expected));

        actual = parser.parse(ListCommand.COMMAND_WORD);
        expected = new HelpCommand(ListCommand.MESSAGE_USAGE);
        assertTrue(actual.equals(expected));

        actual = parser.parse(LoadCommand.COMMAND_WORD);
        expected = new HelpCommand(LoadCommand.MESSAGE_USAGE);
        assertTrue(actual.equals(expected));

        actual = parser.parse(MarkCommand.COMMAND_WORD);
        expected = new HelpCommand(MarkCommand.MESSAGE_USAGE);
        assertTrue(actual.equals(expected));

        actual = parser.parse(OptionsCommand.COMMAND_WORD);
        expected = new HelpCommand(OptionsCommand.MESSAGE_USAGE);
        assertTrue(actual.equals(expected));

        actual = parser.parse(RedoCommand.COMMAND_WORD);
        expected = new HelpCommand(RedoCommand.MESSAGE_USAGE);
        assertTrue(actual.equals(expected));

        actual = parser.parse(SaveCommand.COMMAND_WORD);
        expected = new HelpCommand(SaveCommand.MESSAGE_USAGE);
        assertTrue(actual.equals(expected));

        actual = parser.parse(UndoCommand.COMMAND_WORD);
        expected = new HelpCommand(UndoCommand.MESSAGE_USAGE);
        assertTrue(actual.equals(expected));

        actual = parser.parse(UnmarkCommand.COMMAND_WORD);
        expected = new HelpCommand(UnmarkCommand.MESSAGE_USAGE);
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
