package teamthree.twodo.logic.commands;

import static org.junit.Assert.assertEquals;
import static teamthree.twodo.testutil.EditCommandTestUtil.VALID_END_DATE;
import static teamthree.twodo.testutil.EditCommandTestUtil.VALID_NAME_EVENT;
import static teamthree.twodo.testutil.EditCommandTestUtil.VALID_START_DATE;
import static teamthree.twodo.testutil.EditCommandTestUtil.VALID_TAG_SPONGEBOB;
import static teamthree.twodo.testutil.TypicalTask.INDEX_FIRST_TASK;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import teamthree.twodo.commons.core.index.Index;
import teamthree.twodo.commons.exceptions.IllegalValueException;
import teamthree.twodo.logic.CommandHistory;
import teamthree.twodo.logic.UndoCommandHistory;
import teamthree.twodo.logic.commands.EditCommand.EditTaskDescriptor;
import teamthree.twodo.logic.commands.exceptions.CommandException;
import teamthree.twodo.logic.parser.exceptions.ParseException;
import teamthree.twodo.model.Model;
import teamthree.twodo.model.ModelManager;
import teamthree.twodo.model.ReadOnlyTaskBook;
import teamthree.twodo.model.TaskBook;
import teamthree.twodo.model.UserPrefs;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.model.task.exceptions.DuplicateTaskException;
import teamthree.twodo.model.task.exceptions.TaskNotFoundException;
import teamthree.twodo.testutil.EditTaskDescriptorBuilder;
import teamthree.twodo.testutil.TaskWithDeadlineBuilder;
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
        model = new ModelManager(new TypicalTask().getTypicalTaskBook(), new UserPrefs());
        history = new CommandHistory();
        undoHistory = new UndoCommandHistory();
        undoCommand = new UndoCommand();
        undoCommand.setData(model, history, undoHistory, null);
        this.taskList = TestUtil.generateSampleTaskData();
    }


    @Test
    public void execute_undoAddCommand_success() throws DuplicateTaskException, CommandException, ParseException {

        Model expectedModel = new ModelManager(model.getTaskBook(), new UserPrefs());
        ReadOnlyTask taskToAdd = taskList.get(FIRST_INDEX);

        this.model.addTask(taskToAdd);
        this.history.addToAddHistory(taskToAdd);
        this.history.addToUserInputHistory(AddCommand.COMMAND_WORD);

        String expectedMessage = UndoCommand.MESSAGE_SUCCESS.concat(DeleteCommand.MESSAGE_DELETE_TASK_SUCCESS);
        CommandTestUtil.assertCommandSuccess(undoCommand, model,
                String.format(expectedMessage, taskToAdd), expectedModel);

    }

    @Test
    public void excute_noPreviousCommands_success() throws CommandException {
        assertCommandResult(undoCommand, UndoCommand.MESSAGE_NO_HISTORY);
    }

    /* @Test
    public void execute_undoMarkCommand_sucess()
            throws CommandException, DuplicateTaskException, TaskNotFoundException, ParseException {

        Model expectedModel = model;
        ReadOnlyTask taskToMark = model.getFilteredAndSortedTaskList().get(INDEX_FIRST_TASK.getZeroBased());

        this.model.markTask(taskToMark);
        this.history.addToMarkHistory(taskToMark);
        this.history.addToUserInputHistory(MarkCommand.COMMAND_WORD);

        String expectedMessage = UndoCommand.MESSAGE_SUCCESS.concat(UnmarkCommand.MESSAGE_UNMARK_TASK_SUCCESS);
        CommandTestUtil.assertCommandSuccess(undoCommand, model,
                String.format(expectedMessage, taskToMark), expectedModel);

    }*/

    @Test
    public void execute_undoClearCommand_success() throws CommandException, ParseException {

        Model expectedModel = new ModelManager(model.getTaskBook(), new UserPrefs());
        ReadOnlyTaskBook taskBook = expectedModel.getTaskBook();

        this.model.resetData(new TaskBook());
        this.history.addToClearHistory(taskBook);
        this.history.addToUserInputHistory(ClearCommand.COMMAND_WORD);

        CommandTestUtil.assertCommandSuccess(undoCommand, model,
                UndoCommand.MESSAGE_SUCCESS.concat("Restored TaskBook"), expectedModel);

    }

    @Test
    public void execute_undoDeleteCommand_success()
            throws DuplicateTaskException, CommandException, ParseException, TaskNotFoundException {

        ReadOnlyTask taskToDelete = model.getFilteredAndSortedTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_TASK);
        deleteCommand.setData(model, history, undoHistory, null);
        deleteCommand.execute();
        this.history.addToUserInputHistory(DeleteCommand.COMMAND_WORD);

        String expectedMessage = UndoCommand.MESSAGE_SUCCESS.concat(AddCommand.MESSAGE_SUCCESS);
        ModelManager expectedModel = new ModelManager(model.getTaskBook(), new UserPrefs());
        expectedModel.addTask(taskToDelete);

        CommandTestUtil.assertCommandSuccess(undoCommand, model,
                String.format(expectedMessage, taskToDelete), expectedModel);
    }

    @Test
    public void execute_undoEditCommand_success()
            throws CommandException, TaskNotFoundException, IllegalValueException {

        Index indexLastTask = Index.fromOneBased(model.getFilteredAndSortedTaskList().size());
        ReadOnlyTask lastTask = model.getFilteredAndSortedTaskList().get(indexLastTask.getZeroBased());

        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withName(VALID_NAME_EVENT)
                .withStartAndEndDeadline(VALID_START_DATE, VALID_END_DATE).withTags(VALID_TAG_SPONGEBOB).build();
        EditCommand editCommand = new EditCommand(indexLastTask, descriptor);
        editCommand.setData(model, history, undoHistory, null);
        editCommand.execute();
        this.history.addToUserInputHistory(EditCommand.COMMAND_WORD);

        TaskWithDeadlineBuilder taskInList = new TaskWithDeadlineBuilder(lastTask);
        Task editedTask = taskInList.withName(VALID_NAME_EVENT).withEventDeadline(VALID_START_DATE, VALID_END_DATE)
                .withTags(VALID_TAG_SPONGEBOB).build();

        model.updateTask(lastTask, editedTask);

        Model expectedModel = new ModelManager(model.getTaskBook(), new UserPrefs());
        expectedModel.updateTask(editedTask, lastTask);
        String expectedMessage = UndoCommand.MESSAGE_SUCCESS.concat(EditCommand.MESSAGE_EDIT_TASK_SUCCESS);

        CommandTestUtil.assertCommandSuccess(undoCommand, model,
                String.format(expectedMessage, editedTask), expectedModel);
    }


    /**
     * Asserts that the result message from the execution of {@code historyCommand} equals to {@code expectedMessage}
     * @throws CommandException
     */
    private void assertCommandResult(UndoCommand undoCommand, String expectedMessage) throws CommandException {
        assertEquals(expectedMessage, undoCommand.execute().feedbackToUser);
    }
}

