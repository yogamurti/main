package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import teamthree.twodo.commons.core.Messages;
import teamthree.twodo.logic.commands.ClearCommand;
import teamthree.twodo.logic.commands.FindCommand;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.testutil.TaskUtil;

public class FindCommandTest extends TaskListGuiTest {

    @Test
    public void findNonEmptyList() {
        commandBox.runCommand(listFloating);
        //add all tasks if not all present
        if (taskListPanel.getNumberOfTasks() < td.getTypicalTasks().length) {
            for (Task task : td.getTypicalTasks()) {
                commandBox.runCommand(TaskUtil.getAddCommand(task));
            }
        }
        assertFindResult(FindCommand.COMMAND_WORD + " cia"); // no results
        assertFindResult(FindCommand.COMMAND_WORD + " cs", td.cs2103, td.cs1020, td.cs2010); // multiple results
    }

    @Test
    public void findEmptyList() {
        commandBox.runCommand(ClearCommand.COMMAND_WORD);
        assertFindResult(FindCommand.COMMAND_WORD + " Jean"); // no results
    }

    @Test
    public void findInvalidCommandFail() {
        commandBox.runCommand(FindCommand.COMMAND_WORD + "cca");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    private void assertFindResult(String command, Task... expectedHits) {
        commandBox.runCommand(command);
        assertListSize(expectedHits.length);
        assertResultMessage(String.format(FindCommand.MESSAGE_SUCCESS_INCOMPLETE, expectedHits.length));
        assertTrue(taskListPanel.isListMatching(expectedHits));
    }
}
