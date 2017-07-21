package teamthree.twodo.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

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
import teamthree.twodo.model.tag.Tag;
import teamthree.twodo.model.task.Deadline;
import teamthree.twodo.testutil.TypicalTask;

//@@author A0107433N
/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;

    @Before
    public void setUp() throws IllegalValueException {
        model = new ModelManager(new TypicalTask().getTypicalTaskBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getTaskBook(), new UserPrefs());
    }

    @Test
    public void executeListUnfiltered() throws Exception {
        //Test incomplete tasks
        boolean listIncomplete = true;
        ListCommand listCommand = new ListCommand(null, AttributeInputted.NONE, listIncomplete, false, null);
        listCommand.setData(model, new CommandHistory(), new UndoCommandHistory());

        expectedModel.updateFilteredTaskListToShowAll(null, false, listIncomplete);
        assertCommandSuccess(listCommand, model, ListCommand.MESSAGE_SUCCESS_INCOMPLETE, expectedModel);

        //Test completed tasks
        listIncomplete = false;
        listCommand = new ListCommand(null, AttributeInputted.NONE, listIncomplete, false, null);
        listCommand.setData(model, new CommandHistory(), new UndoCommandHistory());

        expectedModel.updateFilteredTaskListToShowAll(null, false, listIncomplete);
        assertCommandSuccess(listCommand, model, ListCommand.MESSAGE_SUCCESS_COMPLETE, expectedModel);
    }

    @Test
    public void executeListFilteredByFloating() throws Exception {
        //Test incomplete tasks
        boolean listIncomplete = true;
        ListCommand listCommand = new ListCommand(null, AttributeInputted.NONE, listIncomplete, true, null);
        listCommand.setData(model, new CommandHistory(), new UndoCommandHistory());

        expectedModel.updateFilteredTaskListToShowAll(null, true, listIncomplete);
        assertCommandSuccess(listCommand, model, ListCommand.MESSAGE_SUCCESS_INCOMPLETE_FLOATING, expectedModel);

        //Test completed tasks
        listIncomplete = false;
        listCommand = new ListCommand(null, AttributeInputted.NONE, listIncomplete, true, null);
        listCommand.setData(model, new CommandHistory(), new UndoCommandHistory());

        expectedModel.updateFilteredTaskListToShowAll(null, true, listIncomplete);
        assertCommandSuccess(listCommand, model, ListCommand.MESSAGE_SUCCESS_COMPLETE_FLOATING, expectedModel);
    }

    @Test
    public void executeListFilteredByDeadlineStart() throws Exception {
        Deadline testDeadline = new Deadline("yesterday 10am", "yesterday 10am",
                Deadline.NULL_VALUE);
        AttributeInputted start = AttributeInputted.START;
        //Test incomplete tasks
        boolean listIncomplete = true;
        ListCommand listCommandWithDeadline = new ListCommand(testDeadline, start, listIncomplete, false, null);
        listCommandWithDeadline.setData(model, new CommandHistory(), new UndoCommandHistory());

        expectedModel.updateFilteredTaskListToShowPeriod(testDeadline, start, listIncomplete, null);
        assertCommandSuccess(listCommandWithDeadline, model, String.format(
                ListCommand.MESSAGE_SUCCESS_INCOMPLETE_START, testDeadline.getStartDate()), expectedModel);

        //Test completed tasks
        listIncomplete = false;
        listCommandWithDeadline = new ListCommand(testDeadline, start, listIncomplete, false, null);
        listCommandWithDeadline.setData(model, new CommandHistory(), new UndoCommandHistory());

        expectedModel.updateFilteredTaskListToShowPeriod(testDeadline, start, listIncomplete, null);
        assertCommandSuccess(listCommandWithDeadline, model, String.format(
                ListCommand.MESSAGE_SUCCESS_COMPLETE_START, testDeadline.getStartDate()), expectedModel);
    }

    @Test
    public void executeListFilteredByDeadlineEnd() throws Exception {
        Deadline testDeadline = new Deadline("tomorrow 10am", "tomorrow 10am",
                Deadline.NULL_VALUE);
        AttributeInputted end = AttributeInputted.END;
        //Test incomplete tasks
        boolean listIncomplete = true;
        ListCommand listCommandWithDeadline = new ListCommand(testDeadline, end, listIncomplete, false, null);
        listCommandWithDeadline.setData(model, new CommandHistory(), new UndoCommandHistory());

        expectedModel.updateFilteredTaskListToShowPeriod(testDeadline, end, listIncomplete, null);
        assertCommandSuccess(listCommandWithDeadline, model, String.format(
                ListCommand.MESSAGE_SUCCESS_INCOMPLETE_END, testDeadline.getStartDate()), expectedModel);

        //Test completed tasks
        listIncomplete = false;
        listCommandWithDeadline = new ListCommand(testDeadline, end, listIncomplete, false, null);
        listCommandWithDeadline.setData(model, new CommandHistory(), new UndoCommandHistory());

        expectedModel.updateFilteredTaskListToShowPeriod(testDeadline, end, listIncomplete, null);
        assertCommandSuccess(listCommandWithDeadline, model, String.format(
                ListCommand.MESSAGE_SUCCESS_COMPLETE_END, testDeadline.getStartDate()), expectedModel);
    }

    @Test
    public void executeListFilteredByDeadlineBoth() throws Exception {
        Deadline testDeadline = new Deadline("last week 10am", "next week 10am",
                Deadline.NULL_VALUE);
        AttributeInputted both = AttributeInputted.BOTH;
        //Test incomplete tasks
        boolean listIncomplete = true;
        ListCommand listCommandWithDeadline = new ListCommand(testDeadline, both, listIncomplete, false, null);
        listCommandWithDeadline.setData(model, new CommandHistory(), new UndoCommandHistory());

        expectedModel.updateFilteredTaskListToShowPeriod(testDeadline, both, listIncomplete, null);
        assertCommandSuccess(listCommandWithDeadline, model, String.format(
                ListCommand.MESSAGE_SUCCESS_INCOMPLETE_BOTH, testDeadline.getStartDate(), testDeadline.getEndDate()),
                expectedModel);

        //Test completed tasks
        listIncomplete = false;
        listCommandWithDeadline = new ListCommand(testDeadline, both, listIncomplete, false, null);
        listCommandWithDeadline.setData(model, new CommandHistory(), new UndoCommandHistory());

        expectedModel.updateFilteredTaskListToShowPeriod(testDeadline, both, listIncomplete, null);
        assertCommandSuccess(listCommandWithDeadline, model, String.format(
                ListCommand.MESSAGE_SUCCESS_COMPLETE_BOTH, testDeadline.getStartDate(), testDeadline.getEndDate()),
                expectedModel);
    }

    @Test
    public void executeListFilteredByTag() throws Exception {
        AttributeInputted none = AttributeInputted.NONE;
        Set<Tag> testTagList = new TypicalTask().cs2103.getTags();
        //Test incomplete tasks
        boolean listIncomplete = true;
        ListCommand listCommandWithDeadline = new ListCommand(null, none, listIncomplete, false, testTagList);
        listCommandWithDeadline.setData(model, new CommandHistory(), new UndoCommandHistory());

        expectedModel.updateFilteredTaskListToShowAll(testTagList, false, listIncomplete);
        assertCommandSuccess(listCommandWithDeadline, model, ListCommand.MESSAGE_SUCCESS_INCOMPLETE_TAG,
                expectedModel);

        //Test complete tasks
        listIncomplete = false;
        listCommandWithDeadline = new ListCommand(null, none, listIncomplete, false, testTagList);
        listCommandWithDeadline.setData(model, new CommandHistory(), new UndoCommandHistory());

        expectedModel.updateFilteredTaskListToShowAll(testTagList, false, listIncomplete);
        assertCommandSuccess(listCommandWithDeadline, model, ListCommand.MESSAGE_SUCCESS_COMPLETE_TAG,
                expectedModel);
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
        boolean listIncomplete = true;
        ListCommand listCommandWithDeadline = new ListCommand(testDeadline, start, listIncomplete, false, null);
        listCommandWithDeadline.setData(model, new CommandHistory(), new UndoCommandHistory());

        ListCommand command = new ListCommand(testDeadline, start, listIncomplete, false, null);
        assertTrue(command.equals(command));
        ListCommand other = new ListCommand(testDeadline, start, listIncomplete, false, null);
        assertTrue(command.equals(other));
    }
}
