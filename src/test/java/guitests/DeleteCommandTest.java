package guitests;

import static org.junit.Assert.assertTrue;
import static teamthree.twodo.logic.commands.DeleteCommand.MESSAGE_DELETE_TASK_SUCCESS;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import teamthree.twodo.commons.core.index.Index;
import teamthree.twodo.logic.commands.DeleteCommand;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.testutil.TaskUtil;
import teamthree.twodo.testutil.TestUtil;
import teamthree.twodo.testutil.TypicalTask;

public class DeleteCommandTest extends TaskBookGuiTest {

    @Test
    public void delete() {
        //List all floating first
        commandBox.runCommand(listFloating);
        if (personListPanel.getNumberOfTasks() == 0) {
            commandBox.runCommand(TaskUtil.getAddCommand(td.ida));
        }
        List<ReadOnlyTask> currentList = new ArrayList<ReadOnlyTask>();
        currentList.addAll(personListPanel.getListView().getItems());
        //delete the first in the list
        Index targetIndex = TypicalTask.INDEX_FIRST_TASK;
        assertDeleteSuccess(targetIndex, currentList);

        //invalid index
        commandBox.runCommand(DeleteCommand.COMMAND_WORD + " " + currentList.size() + 1);
        assertResultMessage("The task index provided is invalid");

    }

    /**
     * Runs the delete command to delete the person at {@code index} and
     * confirms the result is correct.
     *
     * @param currentList
     *            A copy of the current list of persons (before deletion).
     */
    private void assertDeleteSuccess(Index index, final List<ReadOnlyTask> currentList) {
        ReadOnlyTask personToDelete = currentList.get(index.getZeroBased());
        List<ReadOnlyTask> expectedRemainder = TestUtil.removeTaskFromList(currentList, index);

        commandBox.runCommand(DeleteCommand.COMMAND_WORD + " " + index.getOneBased());

        //confirm the list now contains all previous persons except the deleted person
        assertTrue(personListPanel.isListMatching(expectedRemainder));

        //confirm the result message is correct
        assertResultMessage(String.format(MESSAGE_DELETE_TASK_SUCCESS, personToDelete));
    }

}
