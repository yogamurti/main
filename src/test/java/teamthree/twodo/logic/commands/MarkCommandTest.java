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

// @@author A0139267W
public class MarkCommandTest {

    private Model model = new ModelManager(new TypicalTask().getTypicalTaskList(), new UserPrefs());

    @Test
    public void executeValidIndexUnfilteredListSuccess() throws Exception {
        ReadOnlyTask taskToMark = model.getFilteredAndSortedTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        MarkCommand markCommand = prepareCommand(INDEX_FIRST_TASK);

        Model expectedModel = new ModelManager(new TaskList(model.getTaskList()), new UserPrefs());
        expectedModel.markTask(taskToMark);
        String expectedMessage = getExpectedMessage(expectedModel, taskToMark);

        CommandTestUtil.assertCommandSuccess(markCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void executeInvalidIndexUnfilteredListFailure() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredAndSortedTaskList().size() + 1);
        MarkCommand markCommand = prepareCommand(outOfBoundIndex);

        CommandTestUtil.assertCommandFailure(markCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void executeValidIndexFilteredListSuccess() throws Exception {
        showFirstTaskOnly(model);
        ReadOnlyTask taskToMark = model.getFilteredAndSortedTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        MarkCommand markCommand = prepareCommand(INDEX_FIRST_TASK);

        Model expectedModel = new ModelManager(model.getTaskList(), new UserPrefs());
        showFirstTaskOnly(expectedModel);
        expectedModel.markTask(taskToMark);
        String expectedMessage = getExpectedMessage(expectedModel, taskToMark);
        // Properly resets the task list to its prior state
        showFirstTaskOnly(expectedModel);

        CommandTestUtil.assertCommandSuccess(markCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void executeInvalidIndexFilteredListFailure() throws Exception {
        showFirstTaskOnly(model);
        Index outOfBoundIndex = INDEX_SECOND_TASK;
        // Ensures that outOfBoundIndex is still in bounds of task list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getTaskList().getTaskList().size());

        MarkCommand markCommand = prepareCommand(outOfBoundIndex);

        CommandTestUtil.assertCommandFailure(markCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void executeAlreadyMarkedTaskFailure() throws Exception {
        MarkCommand markCommand = prepareCommand(INDEX_FIRST_TASK);
        markCommand.execute();

        /**
         * Attempts to mark the marked task The recently marked task should be
         * the only marked task in the model
         */
        model.updateFilteredTaskListToShowAll(null, false, false);
        assertTrue(model.getFilteredAndSortedTaskList().size() == 2);
        CommandTestUtil.assertCommandFailureWithoutTaskList(markCommand, model,
                MarkCommand.MESSAGE_ALREADY_MARKED_TASK);
    }

    // Returns a {@code MarkCommand} with the parameter {@code index}
    private MarkCommand prepareCommand(Index index) {
        MarkCommand markCommand = new MarkCommand(index);
        markCommand.setData(model, new CommandHistory(), null);
        return markCommand;
    }

    // Obtains the appropriate expected message obtained after a successful MarkCommand
    private String getExpectedMessage(Model expectedModel, ReadOnlyTask taskToMark) {
        // Finds the updated task
        final String[] splitName = taskToMark.getName().fullName.split("\\s+");
        expectedModel.updateFilteredTaskListByKeywords(new HashSet<>(Arrays.asList(splitName)), false);
        assertTrue(expectedModel.getFilteredAndSortedTaskList().size() == 1);

        ReadOnlyTask markedTask = expectedModel.getFilteredAndSortedTaskList().get(INDEX_FIRST_TASK.getZeroBased());

        /**
         * Resets task list to its initial state Initial state is assumed to be
         * the task list that lists all incomplete tasks
         */
        expectedModel.updateFilteredTaskListToShowAll(null, false, true);

        return String.format(MarkCommand.MESSAGE_MARK_TASK_SUCCESS, markedTask);
    }

    /**
     * Updates {@code model}'s filtered list to show only the indexed first task
     * from the task book Does not show any task if the indexed first task has
     * been marked as completed
     */
    private void showFirstTaskOnly(Model model) {
        ReadOnlyTask task = model.getTaskList().getTaskList().get(0);
        final String[] splitName = task.getName().fullName.split("\\s+");
        model.updateFilteredTaskListByKeywords(new HashSet<>(Arrays.asList(splitName)), true);
    }
}
