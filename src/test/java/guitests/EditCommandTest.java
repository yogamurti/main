package guitests;

import static org.junit.Assert.assertTrue;
import static teamthree.twodo.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_DEADLINE_START;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_NAME;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_TAG;
import static teamthree.twodo.testutil.TypicalPersons.INDEX_FIRST_PERSON;
import static teamthree.twodo.testutil.TypicalPersons.INDEX_SECOND_PERSON;
import static teamthree.twodo.testutil.TypicalPersons.INDEX_THIRD_PERSON;

import org.junit.Test;

import guitests.guihandles.PersonCardHandle;
import teamthree.twodo.commons.core.Messages;
import teamthree.twodo.commons.core.index.Index;
import teamthree.twodo.logic.commands.EditCommand;
import teamthree.twodo.logic.commands.FindCommand;
import teamthree.twodo.model.tag.Tag;
import teamthree.twodo.model.task.Deadline;
import teamthree.twodo.model.task.Description;
import teamthree.twodo.model.task.Name;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.testutil.TaskWithDeadlineBuilder;

// TODO: reduce GUI tests by transferring some tests to be covered by lower level tests.
public class EditCommandTest extends AddressBookGuiTest {

    // The list of persons in the person list panel is expected to match this list.
    // This list is updated with every successful call to assertEditSuccess().
    private Task[] expectedPersonsList = td.getTypicalPersons();

    @Test
    public void edit_allFieldsSpecified_success() throws Exception {
        String detailsToEdit = PREFIX_NAME + "Bobby " + PREFIX_DEADLINE_START + "91234567 "
                + PREFIX_DESCRIPTION + "Block 123, Bobby Street 3 "
                + PREFIX_TAG + "husband";
        Index addressBookIndex = INDEX_FIRST_PERSON;

        Task editedPerson = new TaskWithDeadlineBuilder().withName("Bobby").withDeadline("91234567")
                .withDescription("Block 123, Bobby Street 3").withTags("husband").build();

        assertEditSuccess(addressBookIndex, addressBookIndex, detailsToEdit, editedPerson);
    }

    @Test
    public void edit_notAllFieldsSpecified_success() throws Exception {
        String detailsToEdit = PREFIX_TAG + "sweetie "
                + PREFIX_TAG + "bestie";
        Index addressBookIndex = INDEX_SECOND_PERSON;

        Task personToEdit = expectedPersonsList[addressBookIndex.getZeroBased()];
        Task editedPerson = new TaskWithDeadlineBuilder(personToEdit).withTags("sweetie", "bestie").build();

        assertEditSuccess(addressBookIndex, addressBookIndex, detailsToEdit, editedPerson);
    }

    @Test
    public void edit_clearTags_success() throws Exception {
        String detailsToEdit = PREFIX_TAG.getPrefix();
        Index addressBookIndex = INDEX_SECOND_PERSON;

        Task personToEdit = expectedPersonsList[addressBookIndex.getZeroBased()];
        Task editedPerson = new TaskWithDeadlineBuilder(personToEdit).withTags().build();

        assertEditSuccess(addressBookIndex, addressBookIndex, detailsToEdit, editedPerson);
    }

    @Test
    public void edit_findThenEdit_success() throws Exception {
        commandBox.runCommand(FindCommand.COMMAND_WORD + " Carl");

        String detailsToEdit = PREFIX_NAME + "Carrle";
        Index addressBookIndex = INDEX_THIRD_PERSON;

        Task personToEdit = expectedPersonsList[addressBookIndex.getZeroBased()];
        Task editedPerson = new TaskWithDeadlineBuilder(personToEdit).withName("Carrle").build();

        assertEditSuccess(INDEX_FIRST_PERSON, addressBookIndex, detailsToEdit, editedPerson);
    }

    @Test
    public void edit_missingPersonIndex_failure() {
        commandBox.runCommand(EditCommand.COMMAND_WORD + " " + PREFIX_NAME + "Bobby");
        assertResultMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
    }

    @Test
    public void edit_invalidPersonIndex_failure() {
        commandBox.runCommand(EditCommand.COMMAND_WORD + " 8 " + PREFIX_NAME + "Bobby");
        assertResultMessage(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void edit_noFieldsSpecified_failure() {
        commandBox.runCommand(EditCommand.COMMAND_WORD + " 1");
        assertResultMessage(EditCommand.MESSAGE_NOT_EDITED);
    }

    @Test
    public void edit_invalidValues_failure() {
        commandBox.runCommand(EditCommand.COMMAND_WORD + " 1 " + PREFIX_NAME + "*&");
        assertResultMessage(Name.MESSAGE_NAME_CONSTRAINTS);

        commandBox.runCommand(EditCommand.COMMAND_WORD + " 1 " + PREFIX_DEADLINE_START + "abcd");
        assertResultMessage(Deadline.MESSAGE_DEADLINE_CONSTRAINTS_STRICT);


        commandBox.runCommand(EditCommand.COMMAND_WORD + " 1 " + PREFIX_DESCRIPTION.getPrefix());
        assertResultMessage(Description.MESSAGE_DESCRIPTION_CONSTRAINTS);

        commandBox.runCommand(EditCommand.COMMAND_WORD + " 1 " + PREFIX_TAG + "*&");
        assertResultMessage(Tag.MESSAGE_TAG_CONSTRAINTS);
    }

    @Test
    public void edit_duplicatePerson_failure() {
        commandBox.runCommand(EditCommand.COMMAND_WORD + " 3 "
                + PREFIX_DEADLINE_START + "85355255 "
                + PREFIX_NAME + "Alice Pauline "
                + PREFIX_DESCRIPTION + "123, Jurong West Ave 6, #08-111 "
                + PREFIX_TAG + "friends");
        assertResultMessage(EditCommand.MESSAGE_DUPLICATE_TASK);
    }

    /**
     * Checks whether the edited person has the correct updated details.
     *
     * @param filteredPersonListIndex index of person to edit in filtered list
     * @param addressBookIndex index of person to edit in the address book.
     *      Must refer to the same person as {@code filteredPersonListIndex}
     * @param detailsToEdit details to edit the person with as input to the edit command
     * @param editedPerson the expected person after editing the person's details
     */
    private void assertEditSuccess(Index filteredPersonListIndex, Index addressBookIndex,
                                    String detailsToEdit, Task editedPerson) {
        commandBox.runCommand(EditCommand.COMMAND_WORD + " "
                + filteredPersonListIndex.getOneBased() + " " + detailsToEdit);

        // confirm the new card contains the right data
        PersonCardHandle editedCard = personListPanel.navigateToPerson(editedPerson.getName().fullName);
        assertMatching(editedPerson, editedCard);

        // confirm the list now contains all previous persons plus the person with updated details
        expectedPersonsList[addressBookIndex.getZeroBased()] = editedPerson;
        assertTrue(personListPanel.isListMatching(expectedPersonsList));
        assertResultMessage(String.format(EditCommand.MESSAGE_EDIT_TASK_SUCCESS, editedPerson));
    }
}
