package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import teamthree.twodo.commons.core.Messages;
import teamthree.twodo.logic.commands.ClearCommand;
import teamthree.twodo.logic.commands.ListCommand;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.testutil.TaskUtil;

//@@author A0107433N
public class ListCommandTest extends TaskListGuiTest {

    @Test
    public void listAllTasks() {
        commandBox.runCommand(ClearCommand.COMMAND_WORD);
        //add 3 tasks
        commandBox.runCommand(TaskUtil.getAddCommand(td.cs1020));
        commandBox.runCommand(TaskUtil.getAddCommand(td.cs2010));
        commandBox.runCommand(TaskUtil.getAddCommand(td.cs2103));
        assertListResult(ListCommand.COMMAND_WORD, String.format(ListCommand.MESSAGE_SUCCESS_INCOMPLETE, 9),
                td.cs1020, td.cs2010, td.cs2103); // multiple results
    }

    @Test
    public void listEmptyList() {
        commandBox.runCommand(ClearCommand.COMMAND_WORD);
        assertListResult(ListCommand.COMMAND_WORD, ListCommand.MESSAGE_EMPTY_LIST); // no results
    }

    @Test
    public void listInvalidCommandFail() {
        commandBox.runCommand(ListCommand.COMMAND_WORD + "cca");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    private void assertListResult(String command, String expectedMessage, Task... expectedHits) {
        commandBox.runCommand(command);

        assertListSize(expectedHits.length);
        assertResultMessage(expectedMessage);
        assertTrue(taskListPanel.isListMatching(expectedHits));
    }
}
