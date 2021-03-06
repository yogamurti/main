package teamthree.twodo.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static teamthree.twodo.testutil.EditCommandTestUtil.DESC_CSMOD;
import static teamthree.twodo.testutil.EditCommandTestUtil.DESC_EVENT;
import static teamthree.twodo.testutil.EditCommandTestUtil.VALID_END_DATE;
import static teamthree.twodo.testutil.EditCommandTestUtil.VALID_NAME_EVENT;
import static teamthree.twodo.testutil.EditCommandTestUtil.VALID_START_DATE;
import static teamthree.twodo.testutil.EditCommandTestUtil.VALID_TAG_SPONGEBOB;
import static teamthree.twodo.testutil.TypicalTask.INDEX_FIRST_TASK;
import static teamthree.twodo.testutil.TypicalTask.INDEX_SECOND_TASK;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import teamthree.twodo.commons.core.Messages;
import teamthree.twodo.commons.core.index.Index;
import teamthree.twodo.logic.CommandHistory;
import teamthree.twodo.logic.commands.EditCommand.EditTaskDescriptor;
import teamthree.twodo.model.Model;
import teamthree.twodo.model.ModelManager;
import teamthree.twodo.model.TaskBook;
import teamthree.twodo.model.UserPrefs;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.model.task.TaskWithDeadline;
import teamthree.twodo.testutil.EditTaskDescriptorBuilder;
import teamthree.twodo.testutil.TaskWithDeadlineBuilder;
import teamthree.twodo.testutil.TypicalTask;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(new TypicalTask().getTypicalTaskBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws Exception {
        Task editedTask = new TaskWithDeadlineBuilder().build();
        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder(editedTask).build();
        EditCommand editCommand = prepareCommand(INDEX_FIRST_TASK, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_TASK_SUCCESS, editedTask);

        Model expectedModel = new ModelManager(new TaskBook(model.getTaskBook()), new UserPrefs());
        expectedModel.updateTask(model.getFilteredAndSortedTaskList().get(0), editedTask);

        CommandTestUtil.assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() throws Exception {
        Index indexLastTask = Index.fromOneBased(model.getFilteredAndSortedTaskList().size());
        ReadOnlyTask lastTask = model.getFilteredAndSortedTaskList().get(indexLastTask.getZeroBased());

        TaskWithDeadlineBuilder taskInList = new TaskWithDeadlineBuilder(lastTask);
        Task editedTask = taskInList.withName(VALID_NAME_EVENT).withEventDeadline(VALID_START_DATE, VALID_END_DATE)
                .withTags(VALID_TAG_SPONGEBOB).build();

        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withName(VALID_NAME_EVENT)
                .withStartAndEndDeadline(VALID_START_DATE, VALID_END_DATE).withTags(VALID_TAG_SPONGEBOB).build();
        EditCommand editCommand = prepareCommand(indexLastTask, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_TASK_SUCCESS, editedTask);

        Model expectedModel = new ModelManager(new TaskBook(model.getTaskBook()), new UserPrefs());
        expectedModel.updateTask(lastTask, editedTask);

        CommandTestUtil.assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() throws Exception {
        EditCommand editCommand = prepareCommand(INDEX_FIRST_TASK, new EditTaskDescriptor());
        ReadOnlyTask editedPerson = model.getFilteredAndSortedTaskList().get(INDEX_FIRST_TASK.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_TASK_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new TaskBook(model.getTaskBook()), new UserPrefs());

        CommandTestUtil.assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() throws Exception {
        showFirstTaskOnly();

        ReadOnlyTask personInFilteredList = model.getFilteredAndSortedTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        Task editedPerson = new TaskWithDeadlineBuilder(personInFilteredList).withName(VALID_NAME_EVENT).build();
        EditCommand editCommand = prepareCommand(INDEX_FIRST_TASK,
                new EditTaskDescriptorBuilder().withName(VALID_NAME_EVENT).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_TASK_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new TaskBook(model.getTaskBook()), new UserPrefs());
        expectedModel.updateTask(model.getFilteredAndSortedTaskList().get(0), editedPerson);

        CommandTestUtil.assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() throws Exception {

        Task firstTask = new TaskWithDeadline(model.getFilteredAndSortedTaskList()
                .get(INDEX_FIRST_TASK.getZeroBased()));
        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder(firstTask).build();
        EditCommand editCommand = prepareCommand(INDEX_SECOND_TASK, descriptor);

        CommandTestUtil.assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_TASK);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() throws Exception {
        showFirstTaskOnly();

        // edit person in filtered list into a duplicate in address book
        ReadOnlyTask taskInList = model.getTaskBook().getTaskList().get(INDEX_SECOND_TASK.getZeroBased());
        EditCommand editCommand = prepareCommand(INDEX_FIRST_TASK,
                new EditTaskDescriptorBuilder(taskInList).build());

        CommandTestUtil.assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_TASK);
    }

    @Test
    public void execute_invalidTaskIndexUnfilteredList_failure() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredAndSortedTaskList().size() + 1);
        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withName(VALID_NAME_EVENT).build();
        EditCommand editCommand = prepareCommand(outOfBoundIndex, descriptor);

        CommandTestUtil.assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list, but
     * smaller than size of task book
     */
    @Test
    public void execute_invalidTaskIndexFilteredList_failure() throws Exception {
        showFirstTaskOnly();
        Index outOfBoundIndex = INDEX_SECOND_TASK;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getTaskBook().getTaskList().size());

        EditCommand editCommand = prepareCommand(outOfBoundIndex,
                new EditTaskDescriptorBuilder().withName(VALID_NAME_EVENT).build());

        CommandTestUtil.assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_TASK, DESC_CSMOD);

        // same values -> returns true
        EditTaskDescriptor copyDescriptor = new EditTaskDescriptor(DESC_CSMOD);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_TASK, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_TASK, DESC_CSMOD)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_TASK, DESC_EVENT)));
    }

    /**
     * Returns an {@code EditCommand} with parameters {@code index} and
     * {@code descriptor}
     */
    private EditCommand prepareCommand(Index index, EditTaskDescriptor descriptor) {
        EditCommand editCommand = new EditCommand(index, descriptor);
        editCommand.setData(model, new CommandHistory(), null);
        return editCommand;
    }

    /**
     * Updates the filtered list to show only the first person in the
     * {@code model}'s address book.
     */
    private void showFirstTaskOnly() {
        ReadOnlyTask task = model.getTaskBook().getTaskList().get(0);
        final String[] splitName = task.getName().fullName.split("\\s+");
        model.updateFilteredTaskList(new HashSet<>(Arrays.asList(splitName)), true);

        assertTrue(model.getFilteredAndSortedTaskList().size() == 1);
    }
}
