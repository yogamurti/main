package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import guitests.guihandles.PersonCardHandle;
import teamthree.twodo.commons.core.Messages;
import teamthree.twodo.logic.commands.AddCommand;
import teamthree.twodo.logic.commands.ClearCommand;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.testutil.TaskUtil;
import teamthree.twodo.testutil.TestUtil;

public class AddCommandTest extends AddressBookGuiTest {

    @Test
    public void add() {
        //add one person
        Task[] currentList = td.getTypicalPersons();
        Task personToAdd = td.hoon;
        assertAddSuccess(personToAdd, currentList);
        currentList = TestUtil.addPersonsToList(currentList, personToAdd);

        //add another person
        personToAdd = td.ida;
        assertAddSuccess(personToAdd, currentList);
        currentList = TestUtil.addPersonsToList(currentList, personToAdd);

        //add duplicate person
        commandBox.runCommand(TaskUtil.getAddCommand(td.hoon));
        assertResultMessage(AddCommand.MESSAGE_DUPLICATE_TASK);
        assertTrue(personListPanel.isListMatching(currentList));

        //add to empty list
        commandBox.runCommand(ClearCommand.COMMAND_WORD);
        assertAddSuccess(td.alice);

        //invalid command
        commandBox.runCommand("adds Johnny");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    private void assertAddSuccess(Task personToAdd, Task... currentList) {
        commandBox.runCommand(TaskUtil.getAddCommand(personToAdd));

        //confirm the new card contains the right data
        PersonCardHandle addedCard = personListPanel.navigateToPerson(personToAdd.getName().fullName);
        assertMatching(personToAdd, addedCard);

        //confirm the list now contains all previous persons plus the new person
        Task[] expectedList = TestUtil.addPersonsToList(currentList, personToAdd);
        assertTrue(personListPanel.isListMatching(expectedList));
    }

}
