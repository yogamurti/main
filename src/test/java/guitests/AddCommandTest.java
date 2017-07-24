package guitests;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import teamthree.twodo.commons.core.Messages;
import teamthree.twodo.logic.commands.AddCommand;
import teamthree.twodo.logic.commands.ClearCommand;
import teamthree.twodo.model.category.Category;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.testutil.TaskUtil;
import teamthree.twodo.testutil.TestUtil;

public class AddCommandTest extends TaskListGuiTest {

    @Test
    public void add() {
        //add one task
        commandBox.runCommand(listFloating);
        Task[] currentList = td.getTypicalTasks();
        Task taskToAdd = td.supermarket;
        assertAddSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);

        //add another task
        taskToAdd = td.ida;
        assertAddSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);

        //add duplicate task
        commandBox.runCommand(TaskUtil.getAddCommand(td.supermarket));
        assertResultMessage(AddCommand.MESSAGE_DUPLICATE_TASK);

        //add to empty list
        commandBox.runCommand(ClearCommand.COMMAND_WORD);
        assertAddSuccess(td.cs2103);

        //invalid command
        commandBox.runCommand("adds Johnny");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void addTagSuccess() {
        clearList();
        String catName = "NEWTAG";
        int numCat = 2;
        String command = "add tag " + catName + " 1,2";
        Category newCategory = new Category(catName, numCat);
        assertAddTagSuccess(command, newCategory);
    }


    private void assertAddSuccess(Task taskToAdd, Task... currentList) {
        commandBox.runCommand(TaskUtil.getAddCommand(taskToAdd));

        mainGui.pressEnter();

        //confirm the new card has been created
        List<ReadOnlyTask> tasklist = taskListPanel.getListView().getItems();
        for (ReadOnlyTask task : tasklist) {
            System.out.println(task.getAsText());
        }
        assertTrue(tasklist.contains(taskToAdd));

    }

    private void assertAddTagSuccess(String command, Category newCategory) {
        commandBox.runCommand(command);

        mainGui.pressEnter();

        //confirm the new card has been created
        List<Category> catlist = categoryListPanel.getListView().getItems();
        assertTrue(catlist.contains(newCategory));

    }

    /*
     * Clears the list and adds two new tasks for testing.
     */
    private void clearList() {
        commandBox.runCommand(clear);
        commandBox.runCommand(TaskUtil.getAddCommand(td.supermarket));
        commandBox.runCommand(TaskUtil.getAddCommand(td.ida));
    }

}
