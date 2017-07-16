package teamthree.twodo.logic.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import teamthree.twodo.commons.core.options.Alarm;
import teamthree.twodo.commons.core.options.AutoMark;
import teamthree.twodo.commons.core.options.DefaultOption;
import teamthree.twodo.logic.CommandHistory;
import teamthree.twodo.logic.UndoCommandHistory;
import teamthree.twodo.model.Model;
import teamthree.twodo.model.ModelManager;

public class HistoryCommandTest {
    private HistoryCommand historyCommand;
    private CommandHistory history;
    private UndoCommandHistory undoHistory;

    @Before
    public void setUp() {
        Model model = new ModelManager();
        history = new CommandHistory();
        historyCommand = new HistoryCommand();
        DefaultOption optionsPrefs = new DefaultOption(new Alarm("LOLTEST"), new AutoMark("false"));
        historyCommand.setData(model, history, undoHistory, optionsPrefs);
    }

    @Test
    public void execute() {
        assertCommandResult(historyCommand, HistoryCommand.MESSAGE_NO_HISTORY);

        String command1 = "clear";
        history.add(command1);
        assertCommandResult(historyCommand, String.format(HistoryCommand.MESSAGE_SUCCESS, command1));

        String command2 = "randomCommand";
        String command3 = "select 1";
        history.add(command2);
        history.add(command3);

        String expectedMessage = String.format(HistoryCommand.MESSAGE_SUCCESS,
                String.join("\n", command1, command2, command3));

        assertCommandResult(historyCommand, expectedMessage);
    }

    /**
     * Asserts that the result message from the execution of {@code historyCommand} equals to {@code expectedMessage}
     */
    private void assertCommandResult(HistoryCommand historyCommand, String expectedMessage) {
        assertEquals(expectedMessage, historyCommand.execute().feedbackToUser);
    }
}
