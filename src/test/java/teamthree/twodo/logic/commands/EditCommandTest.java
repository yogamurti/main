package teamthree.twodo.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static teamthree.twodo.testutil.EditCommandTestUtil.DESC_CSMOD;
import static teamthree.twodo.testutil.EditCommandTestUtil.DESC_EVENT;
import static teamthree.twodo.testutil.EditCommandTestUtil.VALID_END_DATE;
import static teamthree.twodo.testutil.EditCommandTestUtil.VALID_NAME_EVENT;
import static teamthree.twodo.testutil.EditCommandTestUtil.VALID_START_DATE;
import static teamthree.twodo.testutil.EditCommandTestUtil.VALID_TAG_SPONGEBOB;
import static teamthree.twodo.testutil.EditCommandTestUtil.VALID_TAG_WORK;
import static teamthree.twodo.testutil.TypicalPersons.INDEX_FIRST_PERSON;
import static teamthree.twodo.testutil.TypicalPersons.INDEX_SECOND_PERSON;

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
import teamthree.twodo.testutil.EditTaskDescriptorBuilder;
import teamthree.twodo.testutil.TaskWithDeadlineBuilder;
import teamthree.twodo.testutil.TypicalPersons;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(new TypicalPersons().getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws Exception {
        Task editedPerson = new TaskWithDeadlineBuilder().build();
        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder(editedPerson).build();
        EditCommand editCommand = prepareCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_TASK_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new TaskBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updateTask(model.getFilteredPersonList().get(0), editedPerson);

        CommandTestUtil.assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() throws Exception {
        Index indexLastTask = Index.fromOneBased(model.getFilteredPersonList().size());
        ReadOnlyTask lastTask = model.getFilteredPersonList().get(indexLastTask.getZeroBased());

        TaskWithDeadlineBuilder personInList = new TaskWithDeadlineBuilder(lastTask);
        Task editedPerson = personInList.withName(VALID_NAME_EVENT).withEventDeadline(VALID_START_DATE, VALID_END_DATE)
                .withTags(VALID_TAG_SPONGEBOB).build();

        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withName(VALID_NAME_EVENT)
                .withStartAndEndDeadline(VALID_START_DATE, VALID_END_DATE).withTags(VALID_TAG_WORK).build();
        EditCommand editCommand = prepareCommand(indexLastTask, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_TASK_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new TaskBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updateTask(lastTask, editedPerson);

        CommandTestUtil.assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() throws Exception {
        EditCommand editCommand = prepareCommand(INDEX_FIRST_PERSON, new EditTaskDescriptor());
        ReadOnlyTask editedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_TASK_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new TaskBook(model.getAddressBook()), new UserPrefs());

        CommandTestUtil.assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() throws Exception {
        showFirstPersonOnly();

        ReadOnlyTask personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Task editedPerson = new TaskWithDeadlineBuilder(personInFilteredList).withName(VALID_NAME_EVENT).build();
        EditCommand editCommand = prepareCommand(INDEX_FIRST_PERSON,
                new EditTaskDescriptorBuilder().withName(VALID_NAME_EVENT).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_TASK_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new TaskBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updateTask(model.getFilteredPersonList().get(0), editedPerson);

        CommandTestUtil.assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() throws Exception {
        Task firstPerson = new Task(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()));
        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder(firstPerson).build();
        EditCommand editCommand = prepareCommand(INDEX_SECOND_PERSON, descriptor);

        CommandTestUtil.assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_TASK);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() throws Exception {
        showFirstPersonOnly();

        // edit person in filtered list into a duplicate in address book
        ReadOnlyTask personInList = model.getAddressBook().getTaskList().get(INDEX_SECOND_PERSON.getZeroBased());
        EditCommand editCommand = prepareCommand(INDEX_FIRST_PERSON,
                new EditTaskDescriptorBuilder(personInList).build());

        CommandTestUtil.assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_TASK);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withName(VALID_NAME_EVENT).build();
        EditCommand editCommand = prepareCommand(outOfBoundIndex, descriptor);

        CommandTestUtil.assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list, but
     * smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() throws Exception {
        showFirstPersonOnly();
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getTaskList().size());

        EditCommand editCommand = prepareCommand(outOfBoundIndex,
                new EditTaskDescriptorBuilder().withName(VALID_NAME_EVENT).build());

        CommandTestUtil.assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_PERSON, DESC_CSMOD);

        // same values -> returns true
        EditTaskDescriptor copyDescriptor = new EditTaskDescriptor(DESC_CSMOD);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_PERSON, DESC_CSMOD)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_PERSON, DESC_EVENT)));
    }

    /**
     * Returns an {@code EditCommand} with parameters {@code index} and
     * {@code descriptor}
     */
    private EditCommand prepareCommand(Index index, EditTaskDescriptor descriptor) {
        EditCommand editCommand = new EditCommand(index, descriptor);
        editCommand.setData(model, new CommandHistory());
        return editCommand;
    }

    /**
     * Updates the filtered list to show only the first person in the
     * {@code model}'s address book.
     */
    private void showFirstPersonOnly() {
        ReadOnlyTask person = model.getAddressBook().getTaskList().get(0);
        final String[] splitName = person.getName().fullName.split("\\s+");
        model.updateFilteredTaskList(new HashSet<>(Arrays.asList(splitName)));

        assertTrue(model.getFilteredPersonList().size() == 1);
    }
}
