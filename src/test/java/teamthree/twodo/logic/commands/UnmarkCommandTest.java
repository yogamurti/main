package teamthree.twodo.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static teamthree.twodo.testutil.TypicalTask.INDEX_FIRST_TASK;
import static teamthree.twodo.testutil.TypicalTask.INDEX_SECOND_TASK;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import teamthree.twodo.commons.core.Messages;
import teamthree.twodo.commons.core.index.Index;
import teamthree.twodo.logic.CommandHistory;
import teamthree.twodo.logic.commands.exceptions.CommandException;
import teamthree.twodo.model.Model;
import teamthree.twodo.model.ModelManager;
import teamthree.twodo.model.TaskBook;
import teamthree.twodo.model.UserPrefs;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.testutil.TypicalTask;

//@@author A0139267W
public class UnmarkCommandTest {
    private Model model = new ModelManager(new TypicalTask().getTypicalTaskBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        /**
         *  Marks the indexed first task from the task book
         *  Reuses code from MarkCommandTest
         */
        ReadOnlyTask taskToMark = model.getFilteredAndSortedTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        MarkCommand markCommand = prepareMarkCommand(INDEX_FIRST_TASK);

        Model expectedModel = new ModelManager(new TaskBook(model.getTaskBook()), new UserPrefs());
        expectedModel.markTask(taskToMark);

        String expectedMarkedMessage = getExpectedMarkedMessage(expectedModel, taskToMark);

        CommandTestUtil.assertCommandSuccess(markCommand, model, expectedMarkedMessage, expectedModel);

        /**
         *  Unmarks the marked task
         *  The recently marked task should be the only marked task in the model
         */
        expectedModel.updateFilteredListToShowAllComplete(null, false);
        assertTrue(expectedModel.getFilteredAndSortedTaskList().size() == 1);

        ReadOnlyTask taskToUnmark = expectedModel.getFilteredAndSortedTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        assertTrue(taskToMark.isSameStateAs(taskToUnmark));
        UnmarkCommand unmarkCommand = prepareUnmarkCommand(INDEX_FIRST_TASK);

        expectedModel.unmarkTask(taskToUnmark);

        String expectedUnmarkedMessage = getExpectedUnmarkedMessage(expectedModel, taskToUnmark);

        model.updateFilteredListToShowAllComplete(null, false);
        CommandTestUtil.assertCommandSuccess(unmarkCommand, model, expectedUnmarkedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        model.updateFilteredListToShowAllComplete(null, false);
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredAndSortedTaskList().size() + 1);
        UnmarkCommand unmarkCommand = prepareUnmarkCommand(outOfBoundIndex);

        CommandTestUtil.assertCommandFailure(unmarkCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() throws Exception {
        /**
         *  Marks the indexed first task from the task book
         *  Reuses code from MarkCommandTest
         */
        showFirstIncompletedTaskOnly(model);
        ReadOnlyTask taskToMark = model.getFilteredAndSortedTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        MarkCommand markCommand = prepareMarkCommand(INDEX_FIRST_TASK);

        Model expectedModel = new ModelManager(model.getTaskBook(), new UserPrefs());
        showFirstIncompletedTaskOnly(expectedModel);
        expectedModel.markTask(taskToMark);

        String expectedMessage = getExpectedMarkedMessage(expectedModel, taskToMark);
        // Properly resets the task list to its prior state
        showFirstIncompletedTaskOnly(expectedModel);

        CommandTestUtil.assertCommandSuccess(markCommand, model, expectedMessage, expectedModel);

        /**
         *  Unmarks the marked task
         *  The recently marked task should be the first marked task in the model
         */
        expectedModel.updateFilteredListToShowAllComplete(null, false);
        assertTrue(expectedModel.getFilteredAndSortedTaskList().size() == 1);
        showFirstCompletedTaskOnly(expectedModel);

        ReadOnlyTask taskToUnmark = expectedModel.getFilteredAndSortedTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        assertTrue(taskToMark.isSameStateAs(taskToUnmark));
        UnmarkCommand unmarkCommand = prepareUnmarkCommand(INDEX_FIRST_TASK);

        expectedModel.unmarkTask(taskToUnmark);

        String expectedUnmarkedMessage = getExpectedUnmarkedMessage(expectedModel, taskToUnmark);

        model.updateFilteredListToShowAllComplete(null, false);
        showFirstCompletedTaskOnly(model);
        CommandTestUtil.assertCommandSuccess(unmarkCommand, model, expectedUnmarkedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() throws Exception {
        /**
         *  Marks the indexed first task from the task book
         *  Reuses code from MarkCommandTest
         */
        showFirstIncompletedTaskOnly(model);
        ReadOnlyTask taskToMark = model.getFilteredAndSortedTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        MarkCommand markCommand = prepareMarkCommand(INDEX_FIRST_TASK);

        Model expectedModel = new ModelManager(model.getTaskBook(), new UserPrefs());
        showFirstIncompletedTaskOnly(expectedModel);
        expectedModel.markTask(taskToMark);

        String expectedMessage = getExpectedMarkedMessage(expectedModel, taskToMark);
        // Properly resets the task list to its prior state
        showFirstIncompletedTaskOnly(expectedModel);

        CommandTestUtil.assertCommandSuccess(markCommand, model, expectedMessage, expectedModel);

        // UnmarkCommand attempt
        model.updateFilteredListToShowAllComplete(null, false);
        showFirstCompletedTaskOnly(model);

        Index outOfBoundIndex = INDEX_SECOND_TASK;
        // Ensures that outOfBoundIndex is still in bounds of task list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getTaskBook().getTaskList().size());

        UnmarkCommand unmarkCommand = prepareUnmarkCommand(outOfBoundIndex);

        assertUndoCommandFailure(unmarkCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexIncompletedTask_success() throws Exception {
        ReadOnlyTask taskToUnmark = model.getFilteredAndSortedTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        UnmarkCommand unmarkCommand = prepareUnmarkCommand(INDEX_FIRST_TASK);

        Model expectedModel = new ModelManager(new TaskBook(model.getTaskBook()), new UserPrefs());
        expectedModel.unmarkTask(taskToUnmark);

        String expectedUnmarkedMessage = getExpectedUnmarkedMessage(expectedModel, taskToUnmark);
        expectedModel.updateFilteredListToShowAllIncomplete(null, false);

        CommandTestUtil.assertCommandSuccess(unmarkCommand, model, expectedUnmarkedMessage, expectedModel);
    }
    // Returns a {@code MarkCommand} with the parameter {@code index}
    private MarkCommand prepareMarkCommand(Index index) {
        MarkCommand markCommand = new MarkCommand(index);
        markCommand.setData(model, new CommandHistory(), null);
        return markCommand;
    }

    // Returns a {@code UnmarkCommand} with the parameter {@code index}
    private UnmarkCommand prepareUnmarkCommand(Index index) {
        UnmarkCommand unmarkCommand = new UnmarkCommand(index);
        unmarkCommand.setData(model, new CommandHistory(), null);
        return unmarkCommand;
    }

    // Obtains the appropriate expected message obtained after a successful MarkCommand
    private String getExpectedMarkedMessage(Model expectedModel, ReadOnlyTask taskToMark) {
        // Finds the updated task
        final String[] splitName = taskToMark.getName().fullName.split("\\s+");
        expectedModel.updateFilteredTaskList(new HashSet<>(Arrays.asList(splitName)), false);
        assertTrue(expectedModel.getFilteredAndSortedTaskList().size() == 1);

        ReadOnlyTask markedTask = expectedModel.getFilteredAndSortedTaskList().get(INDEX_FIRST_TASK.getZeroBased());

        /**
         *  Resets task list to its initial state
         *  Initial state is assumed to be the task list that lists all incomplete tasks
         */
        expectedModel.updateFilteredListToShowAllIncomplete(null, false);

        return String.format(MarkCommand.MESSAGE_MARK_TASK_SUCCESS, markedTask);
    }

    // Obtains the appropriate expected message obtained after a successful UnmarkCommand
    private String getExpectedUnmarkedMessage(Model expectedModel, ReadOnlyTask taskToUnmark) {
        // Finds the updated task
        final String[] splitName = taskToUnmark.getName().fullName.split("\\s+");
        expectedModel.updateFilteredTaskList(new HashSet<>(Arrays.asList(splitName)), true);
        assertTrue(expectedModel.getFilteredAndSortedTaskList().size() == 1);

        ReadOnlyTask unmarkedTask = expectedModel.getFilteredAndSortedTaskList().get(INDEX_FIRST_TASK.getZeroBased());

        /**
         *  Resets task list to its initial state
         *  Initial state is assumed to be the task list that lists all completed tasks
         */
        expectedModel.updateFilteredListToShowAllComplete(null, false);

        return String.format(UnmarkCommand.MESSAGE_UNMARK_TASK_SUCCESS, unmarkedTask);
    }

    /**
     * Updates {@code model}'s filtered list to show only the indexed first task from the task book
     * Does not show any task if the indexed first task has been marked as completed
     */
    private void showFirstIncompletedTaskOnly(Model model) {
        ReadOnlyTask task = model.getTaskBook().getTaskList().get(0);
        final String[] splitName = task.getName().fullName.split("\\s+");
        model.updateFilteredTaskList(new HashSet<>(Arrays.asList(splitName)), true);
    }

    /**
     * Updates {@code model}'s filtered list to show only the indexed first task from the task book
     * Does not show any task if the indexed first task has not been marked as completed
     */
    private void showFirstCompletedTaskOnly(Model model) {
        ReadOnlyTask task = model.getTaskBook().getTaskList().get(0);
        final String[] splitName = task.getName().fullName.split("\\s+");
        model.updateFilteredTaskList(new HashSet<>(Arrays.asList(splitName)), false);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the filtered person list in the {@code actualModel} remains unchanged
     * - allows the task book to differ as the test successfully marks a task first
     */
    public static void assertUndoCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.

        TaskBook expectedTaskBook = new TaskBook(actualModel.getTaskBook());
        List<ReadOnlyTask> expectedFilteredList = new ArrayList<>(actualModel.getFilteredAndSortedTaskList());

        try {
            command.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException e) {
            assertEquals(expectedMessage, e.getMessage());
            assertEquals(expectedFilteredList, actualModel.getFilteredAndSortedTaskList());
        }
    }
}
