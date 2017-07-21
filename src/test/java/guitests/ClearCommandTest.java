package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import teamthree.twodo.logic.commands.ClearCommand;
import teamthree.twodo.logic.commands.DeleteCommand;
import teamthree.twodo.testutil.TaskUtil;

public class ClearCommandTest extends TaskListGuiTest {

    @Test
    public void clear() {

        //verify a non-empty list can be cleared
        commandBox.runCommand(listFloating);
        //add task if list is empty
        if (taskListPanel.getNumberOfTasks() == 0) {
            commandBox.runCommand(TaskUtil.getAddCommand(td.supermarket));
        }
        assertClearCommandSuccess();

        //verify other commands can work after a clear command
        commandBox.runCommand(TaskUtil.getAddCommand(td.supermarket));
        assertTrue(taskListPanel.isListMatching(td.supermarket));
        commandBox.runCommand(DeleteCommand.COMMAND_WORD + " 1");
        assertListSize(0);

        //verify clear command works when the list is empty
        assertClearCommandSuccess();
    }

    private void assertClearCommandSuccess() {
        commandBox.runCommand(ClearCommand.COMMAND_WORD);
        assertListSize(0);
        assertResultMessage("Task List has been cleared!");
    }
}

