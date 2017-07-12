package teamthree.twodo.logic.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import teamthree.twodo.logic.CommandHistory;
import teamthree.twodo.logic.commands.exceptions.CommandException;
import teamthree.twodo.logic.parser.exceptions.ParseException;
import teamthree.twodo.model.Model;
import teamthree.twodo.model.ModelManager;

public class HistoryCommandTest {
    private UndoCommand undoCommand;
    private CommandHistory history;

    @Before
    public void setUp() {
        Model model = new ModelManager();
        history = new CommandHistory();
        undoCommand = new UndoCommand();
        undoCommand.setData(model, history);
    }

    @Test
    public void execute() throws CommandException, ParseException {
        assertCommandResult(undoCommand, UndoCommand.MESSAGE_NO_HISTORY);

        String command1 = "clear";
        history.addToUserInputHistory(command1);
        assertCommandResult(undoCommand, String.format(UndoCommand.MESSAGE_SUCCESS, command1));

        String command2 = "randomCommand";
        String command3 = "select 1";
        history.addToUserInputHistory(command2);
        history.addToUserInputHistory(command3);

        String expectedMessage = String.format(UndoCommand.MESSAGE_SUCCESS,
                String.join("\n", command1, command2, command3));

        assertCommandResult(undoCommand, expectedMessage);
    }

    /**
     * Asserts that the result message from the execution of {@code undoCommand} equals to {@code expectedMessage}
     * @throws CommandException 
     */
    private void assertCommandResult(UndoCommand undoCommand, String expectedMessage) throws CommandException {
        assertEquals(expectedMessage, undoCommand.execute().feedbackToUser);
    }
}
