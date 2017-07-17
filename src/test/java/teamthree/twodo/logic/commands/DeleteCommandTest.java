package teamthree.twodo.logic.commands;

import static org.junit.Assert.assertTrue;
import static teamthree.twodo.testutil.TypicalTask.INDEX_FIRST_TASK;
import static teamthree.twodo.testutil.TypicalTask.INDEX_SECOND_TASK;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.junit.Test;

import teamthree.twodo.commons.core.Messages;
import teamthree.twodo.commons.core.index.Index;
import teamthree.twodo.commons.core.options.Alarm;
import teamthree.twodo.commons.core.options.AutoMark;
import teamthree.twodo.commons.core.options.DefaultOption;
import teamthree.twodo.logic.CommandHistory;
import teamthree.twodo.model.Model;
import teamthree.twodo.model.ModelManager;
import teamthree.twodo.model.UserPrefs;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.testutil.TypicalTask;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(new TypicalTask().getTypicalTaskBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {

        ReadOnlyTask taskToDelete = model.getFilteredAndSortedTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        DeleteCommand deleteCommand = prepareCommand(INDEX_FIRST_TASK);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_TASK_SUCCESS, taskToDelete);

        ModelManager expectedModel = new ModelManager(model.getTaskBook(), new UserPrefs());
        expectedModel.deleteTask(taskToDelete);

        CommandTestUtil.assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredAndSortedTaskList().size() + 1);
        DeleteCommand deleteCommand = prepareCommand(outOfBoundIndex);

        CommandTestUtil.assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    /*@Test
    public void execute_validIndexFilteredList_success() throws Exception {
        showFirstTaskOnly(model);

        ReadOnlyTask taskToDelete = model.getFilteredAndSortedTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        DeleteCommand deleteCommand = prepareCommand(INDEX_FIRST_TASK);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_TASK_SUCCESS, taskToDelete);

        Model expectedModel = new ModelManager(model.getTaskBook(), new UserPrefs());
        expectedModel.deleteTask(taskToDelete);
        showNoTask(expectedModel);

        CommandTestUtil.assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }*/

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() throws Exception {
        showFirstTaskOnly(model);

        Index outOfBoundIndex = INDEX_SECOND_TASK;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getTaskBook().getTaskList().size());

        DeleteCommand deleteCommand = prepareCommand(outOfBoundIndex);

        CommandTestUtil.assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    /**
     * Returns a {@code DeleteCommand} with the parameter {@code index}.
     */
    private DeleteCommand prepareCommand(Index index) {
        DeleteCommand deleteCommand = new DeleteCommand(index);
        DefaultOption optionsPrefs = new DefaultOption(new Alarm("LOLTEST"), new AutoMark("false"));
        deleteCommand.setData(model, new CommandHistory(), null, optionsPrefs);
        return deleteCommand;
    }

    /**
     * Updates {@code model}'s filtered list to show only the first person from the address book.
     */
    private void showFirstTaskOnly(Model model) {
        ReadOnlyTask task = model.getTaskBook().getTaskList().get(0);
        final String[] splitName = task.getName().fullName.split("\\s+");
        model.updateFilteredTaskList(new HashSet<>(Arrays.asList(splitName)), true);

        assert model.getFilteredAndSortedTaskList().size() == 1;
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoTask(Model model) {
        model.updateFilteredTaskList(Collections.emptySet(), true);

        assert model.getFilteredAndSortedTaskList().isEmpty();
    }
}
