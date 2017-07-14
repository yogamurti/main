package teamthree.twodo.logic.commands;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import teamthree.twodo.logic.CommandHistory;
import teamthree.twodo.logic.UndoCommandHistory;
import teamthree.twodo.logic.commands.exceptions.CommandException;
import teamthree.twodo.model.Model;
import teamthree.twodo.model.ModelManager;
import teamthree.twodo.model.ReadOnlyTaskBook;
import teamthree.twodo.model.TaskBook;
import teamthree.twodo.model.UserPrefs;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.model.task.exceptions.DuplicateTaskException;
import teamthree.twodo.model.task.exceptions.TaskNotFoundException;
import teamthree.twodo.testutil.TestUtil;
import teamthree.twodo.testutil.TypicalTask;

public class UndoCommandTest {

    private static final int FIRST_INDEX = 1;

    private UndoCommand undoCommand;
    private CommandHistory history;
    private UndoCommandHistory undoHistory;
    private Model model;
    private List<Task> taskList;

    @Before
    public void setUp() {
        Model model = new ModelManager(new TaskBook(), new UserPrefs());
        undoCommand.setData(model, history, undoHistory);
        this.taskList = TestUtil.generateSampleTaskData();
        undoCommand = new UndoCommand();
    }


    @Test
    public void execute_undoAddCommand_sucess() throws DuplicateTaskException, CommandException {

        Model expectedModel = new ModelManager(new TaskBook(), new UserPrefs());
        ReadOnlyTask taskToAdd = taskList.get(FIRST_INDEX);

        model.addTask(taskToAdd);
        String expectedMessage = UndoCommand.MESSAGE_SUCCESS.concat(DeleteCommand.MESSAGE_DELETE_TASK_SUCCESS);
        CommandTestUtil.assertCommandSuccess(undoCommand, model,
                String.format(expectedMessage, taskToAdd), expectedModel);

    }

    @Test
    public void excute_noPreviousCommands_success() throws CommandException {
        assertCommandResult(undoCommand, UndoCommand.MESSAGE_NO_HISTORY);
    }

    @Test
    public void execute_undoMarkCommand_sucess()
            throws CommandException, DuplicateTaskException, TaskNotFoundException {

        Model expectedModel = new ModelManager(new TaskBook(), new UserPrefs());
        ReadOnlyTask taskToMark = taskList.get(FIRST_INDEX);
        expectedModel.addTask(taskToMark); //added Task automatically unmarked (incomplete)

        model.markTask(taskToMark);

        String expectedMessage = UndoCommand.MESSAGE_SUCCESS.concat(UnmarkCommand.MESSAGE_UNMARK_TASK_SUCCESS);
        CommandTestUtil.assertCommandSuccess(undoCommand, model,
                String.format(expectedMessage, taskToMark), expectedModel);

    }

    @Test
    public void execute_undoClearCommand_sucess() throws CommandException {

        Model expectedModel = new ModelManager(new TaskBook(), new UserPrefs());
        ReadOnlyTaskBook taskBook = new TypicalTask().getTypicalTaskBook();
        ReadOnlyTaskBook taskBookToClear = taskBook;
        model.setTaskBook(taskBookToClear);

        expectedModel.resetData(new TaskBook());

        CommandTestUtil.assertCommandSuccess(undoCommand, model,
                UndoCommand.MESSAGE_SUCCESS.concat("Restored TaskBook"), expectedModel);

    }

    /**
     * Asserts that the result message from the execution of {@code historyCommand} equals to {@code expectedMessage}
     * @throws CommandException
     */
    private void assertCommandResult(UndoCommand undoCommand, String expectedMessage) throws CommandException {
        assertEquals(expectedMessage, undoCommand.execute().feedbackToUser);
    }
}

