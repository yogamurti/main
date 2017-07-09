package guitests;

import static org.junit.Assert.assertEquals;
import static teamthree.twodo.testutil.TypicalPersons.INDEX_FIRST_PERSON;

import org.junit.Test;

import teamthree.twodo.commons.core.index.Index;
import teamthree.twodo.logic.commands.ClearCommand;
import teamthree.twodo.logic.commands.SelectCommand;
import teamthree.twodo.model.task.ReadOnlyTask;

public class SelectCommandTest extends TaskBookGuiTest {


    @Test
    public void selectPerson_nonEmptyList() {

        assertSelectionInvalid(Index.fromOneBased(10)); // invalid index
        assertNoPersonSelected();

        assertSelectionSuccess(INDEX_FIRST_PERSON); // first person in the list
        Index personCount = Index.fromOneBased(td.getTypicalPersons().length);
        assertSelectionSuccess(personCount); // last person in the list
        Index middleIndex = Index.fromOneBased(personCount.getOneBased() / 2);
        assertSelectionSuccess(middleIndex); // a person in the middle of the list

        assertSelectionInvalid(Index.fromOneBased(personCount.getOneBased() + 1)); // invalid index
        assertPersonSelected(middleIndex); // assert previous selection remains

        /* Testing other invalid indexes such as -1 should be done when testing the SelectCommand */
    }

    @Test
    public void selectPerson_emptyList() {
        commandBox.runCommand(ClearCommand.COMMAND_WORD);
        assertListSize(0);
        assertSelectionInvalid(INDEX_FIRST_PERSON); //invalid index
    }

    private void assertSelectionInvalid(Index index) {
        commandBox.runCommand(SelectCommand.COMMAND_WORD + " " + index.getOneBased());
        assertResultMessage("The person index provided is invalid");
    }

    private void assertSelectionSuccess(Index index) {
        commandBox.runCommand(SelectCommand.COMMAND_WORD + " " + index.getOneBased());
        assertResultMessage("Selected Task: " + index.getOneBased());
        assertPersonSelected(index);
    }

    private void assertPersonSelected(Index index) {
        assertEquals(personListPanel.getSelectedPersons().size(), 1);
        ReadOnlyTask selectedPerson = personListPanel.getSelectedPersons().get(0);
        assertEquals(personListPanel.getPerson(index.getZeroBased()), selectedPerson);
        //TODO: confirm the correct page is loaded in the Browser Panel
    }

    private void assertNoPersonSelected() {
        assertEquals(personListPanel.getSelectedPersons().size(), 0);
    }

}
