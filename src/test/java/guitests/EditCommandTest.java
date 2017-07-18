package guitests;

import static org.junit.Assert.assertTrue;
import static teamthree.twodo.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_DEADLINE_START;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_NAME;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_TAG;
import static teamthree.twodo.testutil.TypicalTask.INDEX_FIRST_TASK;

import org.junit.Test;

import teamthree.twodo.commons.core.Messages;
import teamthree.twodo.commons.core.index.Index;
import teamthree.twodo.logic.commands.AddCommand;
import teamthree.twodo.logic.commands.EditCommand;
import teamthree.twodo.model.tag.Tag;
import teamthree.twodo.model.task.Deadline;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.testutil.FloatingTaskBuilder;
import teamthree.twodo.testutil.TaskUtil;

// TODO: reduce GUI tests by transferring some tests to be covered by lower
// level tests.
public class EditCommandTest extends TaskBookGuiTest {


    /*
     * @Test public void edit_allFieldsSpecified_success() throws Exception {
     * String detailsToEdit = PREFIX_NAME + "Bobby " + PREFIX_DEADLINE_START +
     * "91234567 " + PREFIX_DESCRIPTION + "Block 123, Bobby Street 3 " +
     * PREFIX_TAG + "husband"; Index addressBookIndex = INDEX_FIRST_TASK;
     *
     * Task editedPerson = new
     * TaskWithDeadlineBuilder().withName("Bobby").withDeadline("91234567")
     * .withDescription("Block 123, Bobby Street 3"
     * ).withTags("husband").build();
     *
     * assertEditSuccess(addressBookIndex, addressBookIndex, detailsToEdit,
     * editedPerson); }
     */
    @Test
    public void editNotAllFieldsSpecifiedSuccess() throws Exception {
        commandBox.runCommand(listFloating);
        //add task if list is empty
        if (personListPanel.getNumberOfTasks() == 0) {
            commandBox.runCommand(TaskUtil.getAddCommand(td.supermarket));
        }

        String detailsToEdit = PREFIX_TAG + "sweetie " + PREFIX_TAG + "bestie";
        Index filteredListIndex = INDEX_FIRST_TASK;

        ReadOnlyTask personToEdit = personListPanel.getListView().getItems().get(filteredListIndex.getZeroBased());
        Task editedPerson = new FloatingTaskBuilder(personToEdit).withTags("sweetie", "bestie").build();

        assertEditSuccess(filteredListIndex, detailsToEdit, editedPerson);
    }

    @Test
    public void editClearTagsSuccess() throws Exception {
        commandBox.runCommand(listFloating);
        //add task if list is empty
        if (personListPanel.getNumberOfTasks() == 0) {
            commandBox.runCommand(TaskUtil.getAddCommand(td.supermarket));
        }

        String detailsToEdit = PREFIX_TAG.getPrefix();
        Index filteredListIndex = INDEX_FIRST_TASK;

        ReadOnlyTask personToEdit = personListPanel.getListView().getItems().get(filteredListIndex.getZeroBased());
        Task editedPerson = new FloatingTaskBuilder(personToEdit).withTags().build();

        assertEditSuccess(filteredListIndex, detailsToEdit, editedPerson);
    }

    @Test
    public void editMissingPersonIndexFailure() {
        commandBox.runCommand(listFloating);
        //add task if list is empty
        if (personListPanel.getNumberOfTasks() == 0) {
            commandBox.runCommand(TaskUtil.getAddCommand(td.supermarket));
        }

        commandBox.runCommand(EditCommand.COMMAND_WORD + " " + PREFIX_NAME + "Bobby");
        assertResultMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
    }

    @Test
    public void edit_invalidPersonIndex_failure() {
        commandBox.runCommand(listFloating);
        //add task if list is empty
        if (personListPanel.getNumberOfTasks() == 0) {
            commandBox.runCommand(TaskUtil.getAddCommand(td.supermarket));
        }
        commandBox.runCommand(EditCommand.COMMAND_WORD + " 8 " + PREFIX_NAME + "Bobby");
        assertResultMessage(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void editNoFieldsSpecifiedFailure() {
        commandBox.runCommand(listFloating);
        //add task if list is empty
        if (personListPanel.getNumberOfTasks() == 0) {
            commandBox.runCommand(TaskUtil.getAddCommand(td.supermarket));
        }

        commandBox.runCommand(EditCommand.COMMAND_WORD + " 1");
        assertResultMessage(EditCommand.MESSAGE_NOT_EDITED);
    }

    @Test
    public void editInvalidValuesFailure() {
        commandBox.runCommand(listFloating);
        //add task if list is empty
        if (personListPanel.getNumberOfTasks() == 0) {
            commandBox.runCommand(TaskUtil.getAddCommand(td.supermarket));
        }
        commandBox.runCommand(EditCommand.COMMAND_WORD + " 1 " + PREFIX_DEADLINE_START + "abcd");
        assertResultMessage(Deadline.MESSAGE_DEADLINE_CONSTRAINTS_STRICT);

        commandBox.runCommand(EditCommand.COMMAND_WORD + " 1 " + PREFIX_TAG + "*&");
        assertResultMessage(Tag.MESSAGE_TAG_CONSTRAINTS);
    }

    @Test
    public void editDuplicatePersonFailure() {
        commandBox.runCommand(listFloating);

        commandBox.runCommand(AddCommand.COMMAND_WORD + " " + PREFIX_NAME + "Alice Pauline " + PREFIX_DESCRIPTION
                + "123, Jurong West Ave 6, #08-111 " + PREFIX_TAG + "friends");
        commandBox.runCommand(AddCommand.COMMAND_WORD + " " + PREFIX_NAME + "CS1020 " + PREFIX_DESCRIPTION + "lab "
                + PREFIX_TAG + "school");
        commandBox.runCommand(EditCommand.COMMAND_WORD + " 1 " + PREFIX_NAME + "CS1020 " + PREFIX_DESCRIPTION + "lab "
                + PREFIX_TAG + "school");
        assertResultMessage(EditCommand.MESSAGE_DUPLICATE_TASK);
    }

    /**
     * Checks whether the edited person has the correct updated details.
     *
     * @param filteredPersonListIndex
     *            index of person to edit in filtered list
     * @param detailsToEdit
     *            details to edit the person with as input to the edit command
     * @param editedPerson
     *            the expected person after editing the person's details
     */
    private void assertEditSuccess(Index filteredPersonListIndex, String detailsToEdit, Task editedPerson) {
        commandBox.runCommand(
                EditCommand.COMMAND_WORD + " " + filteredPersonListIndex.getOneBased() + " " + detailsToEdit);

        // confirm the new card contains the right data
        assertTrue(personListPanel.getListView().getItems().contains(editedPerson));

        assertResultMessage(String.format(EditCommand.MESSAGE_EDIT_TASK_SUCCESS, editedPerson));
    }
}
