package teamthree.twodo.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import teamthree.twodo.commons.core.options.Alarm;
import teamthree.twodo.commons.core.options.AutoMark;
import teamthree.twodo.commons.core.options.DefaultOption;
import teamthree.twodo.logic.CommandHistory;
import teamthree.twodo.logic.commands.exceptions.CommandException;
import teamthree.twodo.model.Model;
import teamthree.twodo.model.ModelManager;
import teamthree.twodo.model.UserPrefs;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.model.task.TaskWithDeadline;
import teamthree.twodo.testutil.TaskWithDeadlineBuilder;
import teamthree.twodo.testutil.TypicalTask;

/**
 * Contains integration tests (interaction with the Model) for
 * {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(new TypicalTask().getTypicalTaskBook(), new UserPrefs());
    }

    @Test
    public void execute_newTask_success() throws Exception {
        Task validTask = new TaskWithDeadlineBuilder().build();

        Model expectedModel = new ModelManager(model.getTaskBook(), new UserPrefs());
        expectedModel.addTask(validTask);

        CommandResult commandResult = prepareCommand(validTask, model).execute();

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validTask), commandResult.feedbackToUser);
        assertEquals(expectedModel, model);
    }

    @Test
    public void execute_duplicateTask_throwsCommandException() throws Exception {
        Task taskInList = new TaskWithDeadline(model.getTaskBook().getTaskList().get(0));

        Model expectedModel = new ModelManager(model.getTaskBook(), new UserPrefs());

        try {
            prepareCommand(taskInList, model).execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException ce) {
            assertEquals(AddCommand.MESSAGE_DUPLICATE_TASK, ce.getMessage());
            assertTrue(expectedModel.equals(model));
        }
    }

    /**
     * Generates a new {@code AddCommand} which upon execution, adds
     * {@code person} into the {@code model}.
     */
    private AddCommand prepareCommand(Task task, Model model) {
        AddCommand command = new AddCommand(task);
        DefaultOption optionsPrefs = new DefaultOption(new Alarm("LOLTEST"), new AutoMark("false"));
        command.setData(model, new CommandHistory(), null, optionsPrefs);
        return command;
    }
}
