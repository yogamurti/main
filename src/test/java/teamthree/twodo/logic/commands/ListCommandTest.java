package teamthree.twodo.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import teamthree.twodo.commons.exceptions.IllegalValueException;
import teamthree.twodo.logic.CommandHistory;
import teamthree.twodo.logic.UndoCommandHistory;
import teamthree.twodo.logic.commands.ListCommand.AttributeInputted;
import teamthree.twodo.logic.commands.exceptions.CommandException;
import teamthree.twodo.model.Model;
import teamthree.twodo.model.ModelManager;
import teamthree.twodo.model.UserPrefs;
import teamthree.twodo.model.task.Deadline;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.testutil.TypicalTask;

//@@author A0107433N
/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;
    private ListCommand listCommand;
    private ListCommand listCommandWithDeadline;
    private boolean listIncomplete;

    @Before
    public void setUp() throws IllegalValueException {
        model = new ModelManager(new TypicalTask().getTypicalTaskBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getTaskBook(), new UserPrefs());
        listIncomplete = true;

    }

    @Test
    public void executeListIsNotFilteredShowsSameList() throws Exception {
        listCommand = new ListCommand(null, AttributeInputted.NONE, listIncomplete, false, null);
        listCommand.setData(model, new CommandHistory(), new UndoCommandHistory());

        expectedModel.updateFilteredListToShowAllIncomplete(null, false);
        assertCommandSuccess(listCommand, model, ListCommand.MESSAGE_SUCCESS_INCOMPLETE, expectedModel);
    }

    @Test
    public void executeListWithFilterByDeadlineStart() throws Exception {
        Deadline testDeadline = new Deadline("yesterday 10am", "yesterday 10am",
                Deadline.NULL_VALUE);
        AttributeInputted start = AttributeInputted.START;
        listCommandWithDeadline = new ListCommand(testDeadline, start, listIncomplete, false, null);
        listCommandWithDeadline.setData(model, new CommandHistory(), new UndoCommandHistory());

        expectedModel.updateFilteredTaskListToShowPeriod(testDeadline, start, listIncomplete, null);
        assertCommandSuccess(listCommandWithDeadline, model, String.format(
                ListCommand.MESSAGE_SUCCESS_INCOMPLETE_START, testDeadline.getStartDate()), expectedModel);
    }

    @Test
    public void executeListWithFilterByDeadlineEnd() throws Exception {
        Deadline testDeadline = new Deadline("tomorrow 10am", "tomorrow 10am",
                Deadline.NULL_VALUE);
        AttributeInputted end = AttributeInputted.END;
        listCommandWithDeadline = new ListCommand(testDeadline, end, listIncomplete, false, null);
        listCommandWithDeadline.setData(model, new CommandHistory(), new UndoCommandHistory());

        expectedModel.updateFilteredTaskListToShowPeriod(testDeadline, end, listIncomplete, null);
        assertCommandSuccess(listCommandWithDeadline, model, String.format(
                ListCommand.MESSAGE_SUCCESS_INCOMPLETE_END, testDeadline.getStartDate()), expectedModel);
    }

    @Test
    public void executeListWithFilterByDeadlineBoth() throws Exception {
        Deadline testDeadline = new Deadline("last week 10am", "next week 10am",
                Deadline.NULL_VALUE);
        AttributeInputted both = AttributeInputted.BOTH;
        listCommandWithDeadline = new ListCommand(testDeadline, both, listIncomplete, false, null);
        listCommandWithDeadline.setData(model, new CommandHistory(), new UndoCommandHistory());

        expectedModel.updateFilteredTaskListToShowPeriod(testDeadline, both, listIncomplete, null);
        assertCommandSuccess(listCommandWithDeadline, model, String.format(
                ListCommand.MESSAGE_SUCCESS_INCOMPLETE_BOTH, testDeadline.getStartDate(),
                testDeadline.getEndDate()), expectedModel);
    }

    /*@Test
    public void executeListWithFilterByTag() throws Exception {
        AttributeInputted both = AttributeInputted.BOTH;
        Set<Tag> testTagList = new TypicalTask().cs2103.getTags();
        listCommandWithDeadline = new ListCommand(null, both, listIncomplete, false, testTagList);
        listCommandWithDeadline.setData(model, new CommandHistory(), new UndoCommandHistory());

        expectedModel.updateFilteredTaskListToShowPeriod(null, both, listIncomplete, testTagList);
        assertCommandSuccess(listCommandWithDeadline, model, String.format(
                ListCommand.MESSAGE_SUCCESS_INCOMPLETE_BOTH, testDeadline.getStartDate(),
                testDeadline.getEndDate()), expectedModel);
    }*/

    @Test
    public void executeListIsFilteredShowsFirstTask() throws Exception {
        // resets modelManager to initial state for upcoming tests
        expectedModel.updateFilteredListToShowAllIncomplete(null, false);

        showFirstTaskOnly(model);
        assertCommandSuccess(listCommand, model, ListCommand.MESSAGE_SUCCESS_INCOMPLETE, expectedModel);
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

    @Test
    public void equalsReturnsTrueSuccessfully() throws IllegalValueException {
        Deadline testDeadline = new Deadline("yesterday 10am", "yesterday 10am",
                Deadline.NULL_VALUE);
        AttributeInputted start = AttributeInputted.START;
        listCommandWithDeadline = new ListCommand(testDeadline, start, listIncomplete, false, null);
        listCommandWithDeadline.setData(model, new CommandHistory(), new UndoCommandHistory());

        ListCommand command = new ListCommand(testDeadline, start, listIncomplete, false, null);
        assertTrue(command.equals(command));
        ListCommand other = new ListCommand(testDeadline, start, listIncomplete, false, null);
        assertTrue(command.equals(other));
    }
}
