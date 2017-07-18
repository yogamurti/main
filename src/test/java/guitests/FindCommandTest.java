package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import teamthree.twodo.commons.core.Messages;
import teamthree.twodo.logic.commands.ClearCommand;
import teamthree.twodo.logic.commands.FindCommand;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.testutil.TaskUtil;

public class FindCommandTest extends TaskBookGuiTest {

    @Test
    public void find_nonEmptyList() {
        commandBox.runCommand(listFloating);
        //add all tasks if not all present
        if (personListPanel.getNumberOfTasks() < td.getTypicalTasks().length) {
            for (Task task : td.getTypicalTasks()) {
                commandBox.runCommand(TaskUtil.getAddCommand(task));
            }
        }
        assertFindResult(FindCommand.COMMAND_WORD + " cia"); // no results
        assertFindResult(FindCommand.COMMAND_WORD + " cs", td.cs2103, td.cs1020, td.cs2010); // multiple results
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
        assertResultMessage("Found " + expectedHits.length + " incomplete tasks");
        assertTrue(personListPanel.isListMatching(expectedHits));
    }
}
