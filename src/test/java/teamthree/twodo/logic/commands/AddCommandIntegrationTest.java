package teamthree.twodo.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

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
        model = new ModelManager(new TypicalTask().getTypicalTaskList(), new UserPrefs());
    }

    @Test
    public void executeNewTaskSuccess() throws Exception {
        Task validTask = new TaskWithDeadlineBuilder().build();

        Model expectedModel = new ModelManager(model.getTaskList(), new UserPrefs());
        expectedModel.addTask(validTask);

        CommandResult commandResult = prepareCommand(validTask, model).execute();

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validTask), commandResult.feedbackToUser);
        assertEquals(expectedModel, model);
    }

    @Test
    public void executeDuplicateTaskFailure() throws Exception {
        Task taskInList = new TaskWithDeadline(model.getTaskList().getTaskList().get(0));

        Model expectedModel = new ModelManager(model.getTaskList(), new UserPrefs());

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
        command.setData(model, new CommandHistory(), null);
        return command;
    }
}
