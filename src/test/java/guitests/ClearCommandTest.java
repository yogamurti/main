package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import teamthree.twodo.logic.commands.ClearCommand;
import teamthree.twodo.logic.commands.DeleteCommand;
import teamthree.twodo.testutil.TaskUtil;

public class ClearCommandTest extends AddressBookGuiTest {

    @Test
    public void clear() {

        //verify a non-empty list can be cleared
        assertTrue(personListPanel.isListMatching(td.getTypicalPersons()));
        assertClearCommandSuccess();

        //verify other commands can work after a clear command
        commandBox.runCommand(TaskUtil.getAddCommand(td.hoon));
        assertTrue(personListPanel.isListMatching(td.hoon));
        commandBox.runCommand(DeleteCommand.COMMAND_WORD + " 1");
        assertListSize(0);

        //verify clear command works when the list is empty
        assertClearCommandSuccess();
    }

    private void assertClearCommandSuccess() {
        commandBox.runCommand(ClearCommand.COMMAND_WORD);
        assertListSize(0);
        assertResultMessage("Note book has been cleared!");
    }
}
