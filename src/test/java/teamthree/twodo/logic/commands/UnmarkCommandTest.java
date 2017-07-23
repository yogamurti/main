package teamthree.twodo.logic.commands;

import static org.junit.Assert.assertTrue;
import static teamthree.twodo.testutil.TypicalTask.INDEX_FIRST_TASK;
import static teamthree.twodo.testutil.TypicalTask.INDEX_SECOND_TASK;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import teamthree.twodo.commons.core.Messages;
import teamthree.twodo.commons.core.index.Index;
import teamthree.twodo.logic.CommandHistory;
import teamthree.twodo.model.Model;
import teamthree.twodo.model.ModelManager;
import teamthree.twodo.model.TaskList;
import teamthree.twodo.model.UserPrefs;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.testutil.TypicalTask;
import teamthree.twodo.testutil.TypicalTask.TaskType;

//@@author A0139267W
public class UnmarkCommandTest {

    private Model model = new ModelManager(new TypicalTask(TaskType.INCOMPLETE).getTypicalTaskList(), new UserPrefs());
    @Test
    public void executeValidIndexUnfilteredListSuccess() throws Exception {
        //  Marks the indexed first task from the task book
        ReadOnlyTask taskToUnmark = model.getFilteredAndSortedTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        MarkCommand markCommand = prepareMarkCommand(INDEX_FIRST_TASK);

        Model expectedModel = new ModelManager(new TaskList(model.getTaskList()), new UserPrefs());
        expectedModel.markTask(taskToUnmark);

        markCommand.execute();

        /**
         *  Unmarks the marked task
         *  The recently marked task should be the only marked task in the model
         */
        expectedModel.updateFilteredTaskListToShowAll(null, false, false);
        assertTrue(expectedModel.getFilteredAndSortedTaskList().size() == 1);
        UnmarkCommand unmarkCommand = prepareUnmarkCommand(INDEX_FIRST_TASK);

        expectedModel.unmarkTask(taskToUnmark);
        String expectedUnmarkedMessage = getExpectedUnmarkedMessage(expectedModel, taskToUnmark);

        model.updateFilteredTaskListToShowAll(null, false, false);
        assertTrue(model.getFilteredAndSortedTaskList().size() == 1);
        CommandTestUtil.assertCommandSuccess(unmarkCommand, model, expectedUnmarkedMessage, expectedModel);
    }

    @Test
    public void executeInvalidIndexUnfilteredListFailure() throws Exception {
        model.updateFilteredTaskListToShowAll(null, false, false);
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredAndSortedTaskList().size() + 1);
        UnmarkCommand unmarkCommand = prepareUnmarkCommand(outOfBoundIndex);

        CommandTestUtil.assertCommandFailure(unmarkCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void executeValidIndexFilteredListSuccess() throws Exception {
        //  Marks the indexed first task from the task book
        showFirstIncompletedTaskOnly(model);
        ReadOnlyTask taskToUnmark = model.getFilteredAndSortedTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        MarkCommand markCommand = prepareMarkCommand(INDEX_FIRST_TASK);

        Model expectedModel = new ModelManager(model.getTaskList(), new UserPrefs());
        showFirstIncompletedTaskOnly(expectedModel);
        expectedModel.markTask(taskToUnmark);

        markCommand.execute();

        /**
         *  Unmarks the marked task
         *  The recently marked task should be the first marked task in the model
         */
        expectedModel.updateFilteredTaskListToShowAll(null, false, false);
        assertTrue(expectedModel.getFilteredAndSortedTaskList().size() == 1);
        showFirstCompletedTaskOnly(expectedModel);
        UnmarkCommand unmarkCommand = prepareUnmarkCommand(INDEX_FIRST_TASK);

        expectedModel.unmarkTask(taskToUnmark);
        String expectedUnmarkedMessage = getExpectedUnmarkedMessage(expectedModel, taskToUnmark);

        model.updateFilteredTaskListToShowAll(null, false, false);
        showFirstCompletedTaskOnly(model);
        assertTrue(model.getFilteredAndSortedTaskList().size() == 1);
        CommandTestUtil.assertCommandSuccess(unmarkCommand, model, expectedUnmarkedMessage, expectedModel);
    }

    @Test
    public void executeInvalidIndexFilteredListFailure() throws Exception {
        //  Marks the indexed first task from the task book
        showFirstIncompletedTaskOnly(model);
        MarkCommand markCommand = prepareMarkCommand(INDEX_FIRST_TASK);

        markCommand.execute();

        // UnmarkCommand attempt
        model.updateFilteredTaskListToShowAll(null, false, false);
        showFirstCompletedTaskOnly(model);
        Index outOfBoundIndex = INDEX_SECOND_TASK;
        // Ensures that outOfBoundIndex is still in bounds of task list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getTaskList().getTaskList().size());
        UnmarkCommand unmarkCommand = prepareUnmarkCommand(outOfBoundIndex);

        CommandTestUtil.assertCommandFailureWithoutTaskList(unmarkCommand, model,
                Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void executeValidIndexIncompletedTaskFailure() throws Exception {
        UnmarkCommand unmarkCommand = prepareUnmarkCommand(INDEX_FIRST_TASK);

        CommandTestUtil.assertCommandFailureWithoutTaskList(unmarkCommand, model,
                UnmarkCommand.MESSAGE_NOT_MARKED_TASK);
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

    // Obtains the appropriate expected message obtained after a successful UnmarkCommand
    private String getExpectedUnmarkedMessage(Model expectedModel, ReadOnlyTask taskToUnmark) {
        // Finds the updated task
        final String[] splitName = taskToUnmark.getName().fullName.split("\\s+");
        expectedModel.updateFilteredTaskListByKeywords(new HashSet<>(Arrays.asList(splitName)), true);
        assertTrue(expectedModel.getFilteredAndSortedTaskList().size() == 1);

        ReadOnlyTask unmarkedTask = expectedModel.getFilteredAndSortedTaskList().get(INDEX_FIRST_TASK.getZeroBased());

        /**
         *  Resets task list to its initial state
         *  Initial state is assumed to be the task list that lists all completed tasks
         */
        expectedModel.updateFilteredTaskListToShowAll(null, false, false);

        return String.format(UnmarkCommand.MESSAGE_UNMARK_TASK_SUCCESS, unmarkedTask);
    }

    /**
     * Updates {@code model}'s filtered list to show only the indexed first task from the task book
     * Does not show any task if the indexed first task has been marked as completed
     */
    private void showFirstIncompletedTaskOnly(Model model) {
        ReadOnlyTask task = model.getTaskList().getTaskList().get(0);
        final String[] splitName = task.getName().fullName.split("\\s+");
        model.updateFilteredTaskListByKeywords(new HashSet<>(Arrays.asList(splitName)), true);
    }

    /**
     * Updates {@code model}'s filtered list to show only the indexed first task from the task book
     * Does not show any task if the indexed first task has not been marked as completed
     */
    private void showFirstCompletedTaskOnly(Model model) {
        ReadOnlyTask task = model.getTaskList().getTaskList().get(0);
        final String[] splitName = task.getName().fullName.split("\\s+");
        model.updateFilteredTaskListByKeywords(new HashSet<>(Arrays.asList(splitName)), false);
    }

}
