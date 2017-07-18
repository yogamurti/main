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

//@@author A0162253M
public class RedoCommandTest {

    private static final int FIRST_INDEX = 1;

    private UndoCommand undoCommand;
    private RedoCommand redoCommand;
    private CommandHistory history;
    private UndoCommandHistory undoHistory;
    private Model model;
    private List<Task> taskList;

    @Before
    public void setUp() {
        model = new ModelManager(new TypicalTask().getTypicalTaskBook(), new UserPrefs());
        history = new CommandHistory();
        undoHistory = new UndoCommandHistory();
        redoCommand = new RedoCommand();
        undoCommand = new UndoCommand();
        redoCommand.setData(model, history, undoHistory);
        undoCommand.setData(model, history, undoHistory);
        this.taskList = TestUtil.generateSampleTaskData();
    }


    @Test
    public void executeRedoAddCommandSuccess()
            throws DuplicateTaskException, CommandException, ParseException, TaskNotFoundException {

        ReadOnlyTask taskToAdd = taskList.get(FIRST_INDEX);
        System.out.println(taskToAdd);

        //Adding Task to prepare model for undo command
        this.model.addTask(taskToAdd);
        this.history.addToAddHistory(taskToAdd);
        this.history.addToUserInputHistory(AddCommand.COMMAND_WORD);
        undoCommand.execute();

        Model expectedModel = new ModelManager(model.getTaskBook(), new UserPrefs());
        String expectedMessage = RedoCommand.MESSAGE_SUCCESS.concat(AddCommand.MESSAGE_SUCCESS);
        expectedModel.addTask(taskToAdd);

        CommandTestUtil.assertCommandSuccess(redoCommand, model,
                String.format(expectedMessage, taskToAdd), expectedModel);

    }

    @Test
    public void executeNoUndoPreviousCommandSuccess() throws CommandException {
        assertCommandResult(redoCommand, RedoCommand.MESSAGE_NO_HISTORY);
    }

    @Test
    public void executeRedoMarkCommandSuccess()
            throws CommandException, DuplicateTaskException, TaskNotFoundException, ParseException {

        //Mark Task to prepare model for undo command
        MarkCommand markCommand = new MarkCommand(INDEX_FIRST_TASK);
        markCommand.setData(model, history, undoHistory);
        markCommand.execute();
        this.history.addToUserInputHistory(MarkCommand.COMMAND_WORD);
        undoCommand.execute();

        Model expectedModel = new ModelManager(model.getTaskBook(), new UserPrefs());
        ReadOnlyTask taskToMark = expectedModel.getFilteredAndSortedTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        String expectedMessage = RedoCommand.MESSAGE_SUCCESS.concat(MarkCommand.MESSAGE_MARK_TASK_SUCCESS);
        expectedModel.markTask(taskToMark);

        CommandTestUtil.assertCommandSuccess(redoCommand, model,
                String.format(expectedMessage, taskToMark), expectedModel);

    }

    @Test
    public void executeRedoUnmarkCommandSuccess()
            throws CommandException, DuplicateTaskException, TaskNotFoundException, ParseException {

        //Unmark Task to prepare model for undo command
        UnmarkCommand unmarkCommand = new UnmarkCommand(INDEX_FIRST_TASK);
        unmarkCommand.setData(model, history, undoHistory);
        unmarkCommand.execute();
        this.history.addToUserInputHistory(UnmarkCommand.COMMAND_WORD);
        undoCommand.execute();

        Model expectedModel = new ModelManager(model.getTaskBook(), new UserPrefs());
        ReadOnlyTask taskToUnmark = expectedModel.getFilteredAndSortedTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        String expectedMessage = RedoCommand.MESSAGE_SUCCESS.concat(UnmarkCommand.MESSAGE_UNMARK_TASK_SUCCESS);
        expectedModel.unmarkTask(taskToUnmark);

        CommandTestUtil.assertCommandSuccess(redoCommand, model,
                String.format(expectedMessage, taskToUnmark), expectedModel);

    }

    @Test
    public void executeRedoClearCommandSuccess() throws CommandException, ParseException {

        ReadOnlyTaskBook taskBook = model.getTaskBook();

        //Clear Task to prepare model for undo command
        this.model.resetData(new TaskBook());
        this.history.addToClearHistory(taskBook);
        this.history.addToUserInputHistory(ClearCommand.COMMAND_WORD);
        undoCommand.execute();

        String expectedMessage = RedoCommand.MESSAGE_SUCCESS.concat(ClearCommand.MESSAGE_SUCCESS);
        Model expectedModel = new ModelManager(new TaskBook(), new UserPrefs());

        CommandTestUtil.assertCommandSuccess(redoCommand, model, expectedMessage, expectedModel);

    }


    @Test
    public void executeUndoDeleteCommandsuccess()
            throws DuplicateTaskException, CommandException, ParseException, TaskNotFoundException {

        ReadOnlyTask taskToDelete = model.getFilteredAndSortedTaskList().get(INDEX_FIRST_TASK.getZeroBased());

        //Delete Task to prepare model for undo command
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_TASK);
        deleteCommand.setData(model, history, undoHistory);
        deleteCommand.execute();
        this.history.addToUserInputHistory(DeleteCommand.COMMAND_WORD);
        undoCommand.execute();

        String expectedMessage = RedoCommand.MESSAGE_SUCCESS.concat(DeleteCommand.MESSAGE_DELETE_TASK_SUCCESS);
        Model expectedModel = new ModelManager(model.getTaskBook(), new UserPrefs());
        expectedModel.deleteTask(taskToDelete);

        CommandTestUtil.assertCommandSuccess(redoCommand, model,
                String.format(expectedMessage, taskToDelete), expectedModel);
    }

    @Test
    public void executeUndoEditCommandSuccess()
            throws CommandException, TaskNotFoundException, IllegalValueException {

        Index indexLastTask = Index.fromOneBased(model.getFilteredAndSortedTaskList().size());
        ReadOnlyTask lastTask = model.getFilteredAndSortedTaskList().get(indexLastTask.getZeroBased());

        //Delete Task to prepare model for undo command
        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withName(VALID_NAME_EVENT)
                .withStartAndEndDeadline(VALID_START_DATE, VALID_END_DATE).withTags(VALID_TAG_SPONGEBOB).build();
        EditCommand editCommand = new EditCommand(indexLastTask, descriptor);
        editCommand.setData(model, history, undoHistory);
        editCommand.execute();
        this.history.addToUserInputHistory(EditCommand.COMMAND_WORD);
        undoCommand.execute();

        //Building expected model and message
        TaskWithDeadlineBuilder taskInList = new TaskWithDeadlineBuilder(lastTask);
        Task editedTask = taskInList.withName(VALID_NAME_EVENT).withEventDeadline(VALID_START_DATE, VALID_END_DATE)
                .withTags(VALID_TAG_SPONGEBOB).build();
        Model expectedModel = new ModelManager(model.getTaskBook(), new UserPrefs());
        expectedModel.updateTask(lastTask, editedTask);
        String expectedMessage = RedoCommand.MESSAGE_SUCCESS.concat(EditCommand.MESSAGE_EDIT_TASK_SUCCESS);

        CommandTestUtil.assertCommandSuccess(redoCommand, model,
                String.format(expectedMessage, editedTask), expectedModel);
    }


    @Test
    public void excuteRedoInvalidCommandReturnInvalidMessage() throws ParseException, CommandException {
        HelpCommand helpCommand = new HelpCommand();
        helpCommand.setData(model, history, undoHistory);
        helpCommand.execute();
        this.history.addToUserInputHistory(HelpCommand.COMMAND_WORD);
        undoCommand.execute();
        CommandResult result = redoCommand.execute();

        String expectedMessage = RedoCommand.MESSAGE_INVALID_PREVIOUS_COMMAND.concat(HelpCommand.COMMAND_WORD);

        assertEquals(expectedMessage, result.feedbackToUser);
    }


    /**
     * Asserts that the result message from the execution of {@code historyCommand} equals to {@code expectedMessage}
     * @throws CommandException
     */
    private void assertCommandResult(RedoCommand redoCommand, String expectedMessage) throws CommandException {
        assertEquals(expectedMessage, redoCommand.execute().feedbackToUser);
    }

}
