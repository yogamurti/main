package teamthree.twodo.logic.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import teamthree.twodo.commons.core.options.Alarm;
import teamthree.twodo.commons.core.options.AutoMark;
import teamthree.twodo.commons.core.options.DefaultOption;
import teamthree.twodo.logic.CommandHistory;
import teamthree.twodo.model.Model;
import teamthree.twodo.model.ModelManager;
import teamthree.twodo.model.UserPrefs;
import teamthree.twodo.testutil.TypicalTask;

public class ClearCommandTest {

    @Test
    public void execute_emptyTaskBook_success() {
        Model model = new ModelManager();
        assertCommandSuccess(model);
    }

    @Test
    public void execute_nonEmptyTaskBook_success() {
        Model model = new ModelManager(new TypicalTask().getTypicalTaskBook(), new UserPrefs());
        assertCommandSuccess(model);
    }

    /**
     * Executes {@code ClearCommand} on the given {@code model}, confirms that <br>
     * - the result message matches {@code ClearCommand.MESSAGE_SUCCESS} <br>
     * - the task book and filtered person list in {@code model} is empty <br>
     */
    private void assertCommandSuccess(Model model) {
        ClearCommand command = new ClearCommand();
        DefaultOption optionsPrefs = new DefaultOption(new Alarm("LOLTEST"), new AutoMark("false"));
        command.setData(model, new CommandHistory(), null, optionsPrefs);
        CommandResult result = command.execute();

        assertEquals(ClearCommand.MESSAGE_SUCCESS, result.feedbackToUser);
        assertEquals(new ModelManager(), model);
    }
}
