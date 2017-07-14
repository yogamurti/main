/*package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import teamthree.twodo.commons.core.Messages;
import teamthree.twodo.logic.commands.ClearCommand;
import teamthree.twodo.logic.commands.DeleteCommand;
import teamthree.twodo.logic.commands.FindCommand;
import teamthree.twodo.model.task.Task;

public class FindCommandTest extends TaskBookGuiTest {

    @Test
    public void find_nonEmptyList() {
        assertFindResult(FindCommand.COMMAND_WORD + " Mark"); // no results
        assertFindResult(FindCommand.COMMAND_WORD + " Meier", td.benson, td.daniel); // multiple results

        //find after deleting one result
        commandBox.runCommand(DeleteCommand.COMMAND_WORD + " 1");
        assertFindResult(FindCommand.COMMAND_WORD + " Meier", td.daniel);
    }

    @Test
    public void find_emptyList() {
        commandBox.runCommand(ClearCommand.COMMAND_WORD);
        assertFindResult(FindCommand.COMMAND_WORD + " Jean"); // no results
    }

    @Test
    public void find_invalidCommand_fail() {
        commandBox.runCommand(FindCommand.COMMAND_WORD + "cca");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    private void assertFindResult(String command, Task... expectedHits) {
        commandBox.runCommand(command);
        assertListSize(expectedHits.length);
        assertResultMessage(expectedHits.length + " persons listed!");
        assertTrue(personListPanel.isListMatching(expectedHits));
    }
}
*/
