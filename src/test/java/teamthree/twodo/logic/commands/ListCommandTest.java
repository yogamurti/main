package teamthree.twodo.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import teamthree.twodo.commons.exceptions.IllegalValueException;
import teamthree.twodo.logic.CommandHistory;
import teamthree.twodo.logic.commands.ListCommand.AttributeInputted;
import teamthree.twodo.logic.commands.exceptions.CommandException;
import teamthree.twodo.model.Model;
import teamthree.twodo.model.ModelManager;
import teamthree.twodo.model.UserPrefs;
import teamthree.twodo.model.task.Deadline;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.testutil.TypicalTask;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;
    private ListCommand listCommand;
    private ListCommand listCommandWithDeadline;
    private boolean listIncomplete;
    private AttributeInputted attInp;
    private Deadline deadline;

    @Before
    public void setUp() throws IllegalValueException {
        model = new ModelManager(new TypicalTask().getTypicalTaskBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getTaskBook(), new UserPrefs());
        expectedModel.updateFilteredListToShowAllIncomplete();
        deadline = new Deadline("yesterday 10am", "yesterday 10am",
                Deadline.NULL_VALUE);

        listCommand = new ListCommand(listIncomplete);
        listCommand.setData(model, new CommandHistory(), null);
        listCommandWithDeadline = new ListCommand(deadline, attInp, listIncomplete);
        listCommandWithDeadline.setData(model, new CommandHistory(), null);
    }

    @Test
    public void executeListIsNotFilteredShowsSameList() throws Exception {
        assertCommandSuccess(listCommand, model, ListCommand.MESSAGE_SUCCESS_INCOMPLETE, expectedModel);
    }

    @Test
    public void executeListWithDeadlineIsNotFilteredShowsSameList() throws Exception {
        assertCommandSuccess(listCommandWithDeadline, model,
                ListCommand.MESSAGE_SUCCESS_INCOMPLETE, expectedModel);
    }

    @Test
    public void executeListIsFiltered_showsEverything() throws Exception {
        showFirstTaskOnly(model);
        assertCommandSuccess(listCommand, model, ListCommand.MESSAGE_SUCCESS_INCOMPLETE, expectedModel);
        model.updateFilteredListToShowAllIncomplete(); // resets modelManager to initial state for upcoming tests
    }

    @Test
    public void execute_listWithDeadlineIsFiltered_showsEverything() throws Exception {
        showFirstTaskOnly(model);
        assertCommandSuccess(listCommandWithDeadline, model,
                ListCommand.MESSAGE_SUCCESS_INCOMPLETE, expectedModel);
        model.updateFilteredListToShowAllIncomplete(); // resets modelManager to initial state for upcoming tests
    }

    /**
     * Updates the filtered list to show only the first task in the {@code model}'s task book.
     */
    private void showFirstTaskOnly(Model model) {
        ReadOnlyTask task = model.getTaskBook().getTaskList().get(0);
        final String[] splitName = task.getName().fullName.split("\\s+");
        model.updateFilteredTaskList(new HashSet<>(Arrays.asList(splitName)), true);

        assertTrue(model.getFilteredAndSortedTaskList().size() == 1);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the result message matches {@code expectedMessage} <br>
     * - the address book and the filtered person list in the {@code model} matches that of {@code expectedModel}
     */

    public static void assertCommandSuccess(Command command, Model model, String expectedMessage, Model expectedModel)
            throws CommandException {
        CommandResult result = command.execute();
        assertEquals(expectedMessage, result.feedbackToUser);
        assertEquals(expectedModel, model);
    }
}
