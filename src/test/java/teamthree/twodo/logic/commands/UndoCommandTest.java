package teamthree.twodo.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static teamthree.twodo.testutil.EditCommandTestUtil.VALID_END_DATE;
import static teamthree.twodo.testutil.EditCommandTestUtil.VALID_NAME_EVENT;
import static teamthree.twodo.testutil.EditCommandTestUtil.VALID_START_DATE;
import static teamthree.twodo.testutil.EditCommandTestUtil.VALID_TAG_SPONGEBOB;
import static teamthree.twodo.testutil.TypicalTask.INDEX_FIRST_TASK;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import teamthree.twodo.automark.AutoMarkManager;
import teamthree.twodo.automark.AutoMarkManagerStud;
import teamthree.twodo.commons.core.Config;
import teamthree.twodo.commons.core.ConfigStud;
import teamthree.twodo.commons.core.index.Index;
import teamthree.twodo.commons.core.options.Alarm;
import teamthree.twodo.commons.core.options.AutoMark;
import teamthree.twodo.commons.core.options.Options;
import teamthree.twodo.commons.exceptions.IllegalValueException;
import teamthree.twodo.logic.CommandHistory;
import teamthree.twodo.logic.UndoCommandHistory;
import teamthree.twodo.logic.commands.EditCommand.EditTaskDescriptor;
import teamthree.twodo.logic.commands.exceptions.CommandException;
import teamthree.twodo.logic.parser.ParserUtil;
import teamthree.twodo.logic.parser.exceptions.ParseException;
import teamthree.twodo.model.Model;
import teamthree.twodo.model.ModelManager;
import teamthree.twodo.model.ReadOnlyTaskList;
import teamthree.twodo.model.TaskList;
import teamthree.twodo.model.UserPrefs;
import teamthree.twodo.model.category.Category;
import teamthree.twodo.model.category.CategoryManager;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.model.task.TaskWithDeadline;
import teamthree.twodo.model.task.exceptions.DuplicateTaskException;
import teamthree.twodo.model.task.exceptions.TaskNotFoundException;
import teamthree.twodo.testutil.EditTaskDescriptorBuilder;
import teamthree.twodo.testutil.TaskWithDeadlineBuilder;
import teamthree.twodo.testutil.TestUtil;
import teamthree.twodo.testutil.TypicalTask;
import teamthree.twodo.testutil.TypicalTask.TaskType;

//@@author A0162253M
/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * UndoCommand
 */
public class UndoCommandTest {

    private static final int FIRST_INDEX = 1;

    private static final String DEFAULT_NOTIFICATION_PERIOD_STRING = "1 day";
    private static final String VALID_ALARM_INPUT = "2 days";
    private static final boolean VALID_AUTOMARK_INPUT = true;
    private static final Options SAME_AS_DEFAULT = new Options(
            new Alarm(Config.defaultNotificationPeriodToString()), new AutoMark(AutoMarkManager.getSetToRun()));

    private UndoCommand undoCommand;
    private CommandHistory history;
    private UndoCommandHistory undoHistory;
    private Model model;
    private List<Task> taskList;
    private CategoryManager catMan;

    @Before
    public void setUp() {
        model = new ModelManager(new TypicalTask(TaskType.INCOMPLETE).getTypicalTaskList(), new UserPrefs());
        history = new CommandHistory();
        undoHistory = new UndoCommandHistory();
        undoCommand = new UndoCommand();
        catMan = new CategoryManager(model);
        undoCommand.setData(model, history, undoHistory, catMan);
        this.taskList = TestUtil.generateSampleTaskData();
    }

    @Test
    public void executeUndoAddCommandSuccess()
            throws DuplicateTaskException, CommandException, ParseException, TaskNotFoundException {

        ReadOnlyTask taskToAdd = taskList.get(FIRST_INDEX);

        //Adding Task to prepare model for undo command
        this.model.addTask(taskToAdd);
        this.history.addToAddHistory(taskToAdd);
        this.history.addToUserInputHistory(AddCommand.COMMAND_WORD);

        Model expectedModel = new ModelManager(model.getTaskList(), new UserPrefs());
        String expectedMessage = UndoCommand.MESSAGE_SUCCESS.concat(DeleteCommand.MESSAGE_DELETE_TASK_SUCCESS);
        expectedModel.deleteTask(taskToAdd);

        CommandTestUtil.assertCommandSuccess(undoCommand, model, String.format(expectedMessage, taskToAdd),
                expectedModel);

    }

    @Test
    public void executeNoPreviousCommandSuccess() throws CommandException {
        assertCommandResult(undoCommand, UndoCommand.MESSAGE_NO_HISTORY);
    }

    @Test
    public void executeUndoMarkCommandSuccess()
            throws CommandException, DuplicateTaskException, TaskNotFoundException, ParseException {
        //Mark Task to prepare model for undo command
        MarkCommand markCommand = new MarkCommand(INDEX_FIRST_TASK);
        model.updateFilteredTaskListToShowAll(null, false, true);
        Task taskToMark = model.getFilteredAndSortedTaskList()
                .get(INDEX_FIRST_TASK.getZeroBased()) instanceof TaskWithDeadline
                        ? new TaskWithDeadline(
                                model.getFilteredAndSortedTaskList().get(INDEX_FIRST_TASK.getZeroBased()))
                        : new Task(model.getFilteredAndSortedTaskList().get(INDEX_FIRST_TASK.getZeroBased()));
        ReadOnlyTask task2Mark = model.getFilteredAndSortedTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        markCommand.setData(model, history, undoHistory);
        markCommand.execute();
        this.history.addToUserInputHistory(MarkCommand.COMMAND_WORD);

        Model expectedModel = new ModelManager(model.getTaskList(), new UserPrefs());
        expectedModel.updateFilteredTaskListToShowAll(null, false, false);
        //The recently marked task should be the only marked task in the model
        assertTrue(expectedModel.getFilteredAndSortedTaskList().size() == 1);

        expectedModel.updateFilteredTaskListToShowAll(null, false, true);
        String expectedMessage = UndoCommand.MESSAGE_SUCCESS.concat(UnmarkCommand.MESSAGE_UNMARK_TASK_SUCCESS);
        expectedModel.unmarkTask(task2Mark);
        taskToMark.markIncompleted();
        CommandTestUtil.assertCommandSuccess(undoCommand, model, String.format(expectedMessage, taskToMark),
                expectedModel);

    }

    @Test
    public void executeUndoUnmarkCommandSuccess()
            throws CommandException, DuplicateTaskException, TaskNotFoundException, ParseException {
        //Unmark Task to prepare model for undo command
        // Marks the indexed first task from the task book
        model.getTaskList().getTaskList().forEach((task) -> {
            try {
                model.unmarkTask(task);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        ReadOnlyTask taskToUndo = model.getFilteredAndSortedTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        MarkCommand markCommand = new MarkCommand(INDEX_FIRST_TASK);
        markCommand.setData(model, history, undoHistory);
        Model expectedModel = new ModelManager(new TaskList(model.getTaskList()), new UserPrefs());
        expectedModel.markTask(taskToUndo);
        String expectedMessage = UndoCommand.MESSAGE_SUCCESS.concat(getExpectedMessage(expectedModel, taskToUndo));
        markCommand.execute();
        /**
         * Unmarks the marked task The recently marked task should be the only
         * marked task in the model
         */
        expectedModel.updateFilteredTaskListToShowAll(null, false, false);

        UnmarkCommand unmarkCommand = new UnmarkCommand(INDEX_FIRST_TASK);
        unmarkCommand.setData(model, history, undoHistory);
        model.updateFilteredTaskListToShowAll(null, false, false);

        unmarkCommand.execute();
        this.history.addToUserInputHistory(UnmarkCommand.COMMAND_WORD);

        CommandTestUtil.assertCommandSuccess(undoCommand, model, String.format(expectedMessage, taskToUndo),
                expectedModel);
    }

    @Test
    public void executeUndoClearCommandSuccess() throws CommandException, ParseException {

        ReadOnlyTaskList taskBook = model.getTaskList();

        //Clear Task to prepare model for undo command
        this.model.resetData(new TaskList());
        this.history.addToClearHistory(taskBook);
        this.history.addToUserInputHistory(ClearCommand.COMMAND_WORD);

        String expectedMessage = UndoCommand.MESSAGE_SUCCESS.concat("Restored TaskList");
        Model expectedModel = new ModelManager(model.getTaskList(), new UserPrefs());

        CommandTestUtil.assertCommandSuccess(undoCommand, model, expectedMessage, expectedModel);

    }

    @Test
    public void executeUndoDeleteCommandSuccess()
            throws DuplicateTaskException, CommandException, ParseException, TaskNotFoundException {

        ReadOnlyTask taskToDelete = model.getFilteredAndSortedTaskList().get(INDEX_FIRST_TASK.getZeroBased());

        //Delete Task to prepare model for undo command
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_TASK, false);
        deleteCommand.setData(model, history, undoHistory);
        deleteCommand.execute();
        this.history.addToUserInputHistory(DeleteCommand.COMMAND_WORD);

        String expectedMessage = UndoCommand.MESSAGE_SUCCESS.concat(AddCommand.MESSAGE_SUCCESS);
        Model expectedModel = new ModelManager(model.getTaskList(), new UserPrefs());
        expectedModel.addTask(taskToDelete);

        CommandTestUtil.assertCommandSuccess(undoCommand, model, String.format(expectedMessage, taskToDelete),
                expectedModel);
    }

    @Test
    public void executeUndoDeleteTagCommandSuccess() throws IllegalValueException, CommandException {
        Index index = ParserUtil.parseIndex("6");
        Category catToBeDeleted = catMan.getCategoryList().get(5);
        String tagName = catToBeDeleted.getName();

        //Delete Tag to prepare model for undo command
        DeleteCommand deleteCommand = new DeleteCommand(index, true);
        deleteCommand.setData(model, history, undoHistory, catMan);
        deleteCommand.execute();
        this.history.addToUserInputHistory("tagDeleted");

        Model expectedModel = new ModelManager(new TypicalTask(TaskType.INCOMPLETE)
                .getTypicalTaskList(), new UserPrefs());
        String expectedMessage = UndoCommand.MESSAGE_SUCCESS.concat(
                        String.format(AddCommand.MESSAGE_SUCCESS_TAG, tagName));

        CommandTestUtil.assertCommandSuccess(undoCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void executeUndoAddTagCommandSuccess() throws IllegalValueException, CommandException {
        String tagName = "important";
        ArrayList<Index> indexList = new ArrayList<Index>();
        Index secondIndex = ParserUtil.parseIndex("2");
        Index firstIndex = ParserUtil.parseIndex("1");
        indexList.add(secondIndex);
        indexList.add(firstIndex);

        //Delete Tag to prepare model for undo command
        AddCommand addCommand = new AddCommand(tagName, indexList);
        addCommand.setData(model, history, undoHistory, catMan);
        addCommand.execute();
        this.history.addToUserInputHistory("tagAdded");

        Model expectedModel = new ModelManager(new TypicalTask(TaskType.INCOMPLETE)
                .getTypicalTaskList(), new UserPrefs());
        String expectedMessage = UndoCommand.MESSAGE_SUCCESS.concat(
                        String.format(DeleteCommand.MESSAGE_DELETE_TAG_SUCCESS, tagName));

        CommandTestUtil.assertCommandSuccess(undoCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void executeUndoEditCommandSuccess() throws CommandException, TaskNotFoundException, IllegalValueException {
        Index indexFirstTask = Index.fromOneBased(1);
        ReadOnlyTask firstTask = model.getFilteredAndSortedTaskList().get(indexFirstTask.getZeroBased());
        TaskWithDeadline initialTask = new TaskWithDeadline(firstTask);

        //Delete Task to prepare model for undo command
        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withName(VALID_NAME_EVENT)
                .withStartAndEndDeadline(VALID_START_DATE, VALID_END_DATE).withTags(VALID_TAG_SPONGEBOB).build();
        EditCommand editCommand = new EditCommand(indexFirstTask, descriptor);
        editCommand.setData(model, history, undoHistory);
        editCommand.execute();
        this.history.addToUserInputHistory(EditCommand.COMMAND_WORD);

        //Building expected model and message
        TaskWithDeadlineBuilder taskInList = new TaskWithDeadlineBuilder(firstTask);
        Task editedTask = taskInList.withName(VALID_NAME_EVENT).withEventDeadline(VALID_START_DATE, VALID_END_DATE)
                .withTags(VALID_TAG_SPONGEBOB).build();
        Model expectedModel = new ModelManager(model.getTaskList(), new UserPrefs());
        expectedModel.updateTask(editedTask, initialTask);
        String expectedMessage = UndoCommand.MESSAGE_SUCCESS.concat(EditCommand.MESSAGE_EDIT_TASK_SUCCESS);

        CommandTestUtil.assertCommandSuccess(undoCommand, model, String.format(expectedMessage, editedTask),
                expectedModel);
    }

    @Test
    public void executeUndoOptionsCommandSuccess() throws CommandException, ParseException {
        Config.changeDefaultNotificationPeriod(DEFAULT_NOTIFICATION_PERIOD_STRING);
        ConfigStud.changeDefaultNotificationPeriod(DEFAULT_NOTIFICATION_PERIOD_STRING);
        AutoMarkManager.setToRun(false);
        AutoMarkManagerStud.setToRun(false);

        Options changedOptions = new Options(new Alarm(VALID_ALARM_INPUT), new AutoMark(VALID_AUTOMARK_INPUT));
        OptionsCommand optionsCommand = new OptionsCommand(changedOptions);
        optionsCommand.setData(model, history, undoHistory);
        optionsCommand.execute();
        history.addToUserInputHistory(OptionsCommand.COMMAND_WORD);
        CommandResult result = undoCommand.execute();

        String expectedMessage = UndoCommand.MESSAGE_SUCCESS.concat(
                String.format(OptionsCommand.MESSAGE_UPDATE_OPTIONS_SUCCESS, SAME_AS_DEFAULT));

        assertEquals(expectedMessage, result.feedbackToUser);
        assertEquals(Config.getDefaultNotificationPeriod(), ConfigStud.getDefaultNotificationPeriod());
        assertEquals(Config.defaultNotificationPeriodToString(), ConfigStud.defaultNotificationPeriodToString());
        assertEquals(AutoMarkManager.getSetToRun(), AutoMarkManagerStud.getSetToRun());

        //ensure that Options is set to default stage
        Config.changeDefaultNotificationPeriod(DEFAULT_NOTIFICATION_PERIOD_STRING);
        AutoMarkManager.setToRun(false);
    }

    @Test
    public void excuteUndoInvalidCommandReturnInvalidMessage() throws ParseException, CommandException {
        HistoryCommand historyCommand = new HistoryCommand();
        historyCommand.setData(model, history, undoHistory);
        historyCommand.execute();
        this.history.addToUserInputHistory(HistoryCommand.COMMAND_WORD);
        CommandResult result = undoCommand.execute();

        String expectedMessage = UndoCommand.MESSAGE_INVALID_PREVIOUS_COMMAND.concat(HistoryCommand.COMMAND_WORD);

        assertEquals(result.feedbackToUser, expectedMessage);
    }

    /**
     * Asserts that the result message from the execution of
     * {@code historyCommand} equals to {@code expectedMessage}
     *
     * @throws CommandException
     */
    private void assertCommandResult(UndoCommand undoCommand, String expectedMessage) throws CommandException {
        assertEquals(expectedMessage, undoCommand.execute().feedbackToUser);
    }

    //@@author A0139267W
    // Obtains the appropriate expected message obtained after a successful MarkCommand
    private String getExpectedMessage(Model expectedModel, ReadOnlyTask taskToMark) {
        // Finds the updated task
        final String[] splitName = taskToMark.getName().fullName.split("\\s+");
        expectedModel.updateFilteredTaskListByKeywords(new HashSet<>(Arrays.asList(splitName)), false);
        assertTrue(expectedModel.getFilteredAndSortedTaskList().size() == 1);

        ReadOnlyTask markedTask = expectedModel.getFilteredAndSortedTaskList().get(INDEX_FIRST_TASK.getZeroBased());

        /**
         * Resets task list to its initial state Initial state is assumed to be
         * the task list that lists all incomplete tasks
         */
        expectedModel.updateFilteredTaskListToShowAll(null, false, true);

        return String.format(MarkCommand.MESSAGE_MARK_TASK_SUCCESS, markedTask);
    }

}
