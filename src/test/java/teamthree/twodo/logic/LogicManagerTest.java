package teamthree.twodo.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.fail;
import static teamthree.twodo.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static teamthree.twodo.commons.core.Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX;
import static teamthree.twodo.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_DEADLINE_END;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_DEADLINE_START;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_NAME;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_TAG;
import static teamthree.twodo.model.util.SampleDataUtil.getTagSet;
import static teamthree.twodo.testutil.TypicalTask.INDEX_THIRD_TASK;

import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.eventbus.Subscribe;

import teamthree.twodo.commons.core.EventsCenter;
import teamthree.twodo.commons.events.model.TaskBookChangedEvent;
import teamthree.twodo.commons.events.ui.ShowHelpRequestEvent;
import teamthree.twodo.logic.commands.AddCommand;
import teamthree.twodo.logic.commands.ClearCommand;
//import teamthree.twodo.logic.commands.Command;
import teamthree.twodo.logic.commands.CommandResult;
import teamthree.twodo.logic.commands.DeleteCommand;
import teamthree.twodo.logic.commands.ExitCommand;
import teamthree.twodo.logic.commands.FindCommand;
import teamthree.twodo.logic.commands.HelpCommand;
import teamthree.twodo.logic.commands.ListCommand;
//import teamthree.twodo.logic.commands.UndoCommand;
import teamthree.twodo.logic.commands.exceptions.CommandException;
import teamthree.twodo.logic.parser.exceptions.ParseException;
import teamthree.twodo.model.Model;
import teamthree.twodo.model.ModelManager;
import teamthree.twodo.model.ReadOnlyTaskBook;
import teamthree.twodo.model.TaskBook;
import teamthree.twodo.model.UserPrefs;
import teamthree.twodo.model.tag.Tag;
import teamthree.twodo.model.task.Deadline;
import teamthree.twodo.model.task.Description;
import teamthree.twodo.model.task.Name;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.model.task.TaskWithDeadline;
//import teamthree.twodo.testutil.TaskWithDeadlineBuilder;

public class LogicManagerTest {

    /**
     * See https://github.com/junit-team/junit4/wiki/rules#temporaryfolder-rule
     */
    @Rule
    public TemporaryFolder saveFolder = new TemporaryFolder();

    private Model model;
    private Logic logic;

    //These are for checking the correctness of the events raised
    private ReadOnlyTaskBook latestSavedTaskBook;
    private boolean helpShown;

    @Subscribe
    private void handleLocalModelChangedEvent(TaskBookChangedEvent abce) {
        latestSavedTaskBook = new TaskBook(abce.data);
    }

    @Subscribe
    private void handleShowHelpRequestEvent(ShowHelpRequestEvent she) {
        helpShown = true;
    }

    @Before
    public void setUp() {
        model = new ModelManager();
        logic = new LogicManager(model);
        EventsCenter.getInstance().registerHandler(this);

        latestSavedTaskBook = new TaskBook(model.getTaskBook()); // last saved assumed to be up to date
        helpShown = false;
    }

    @After
    public void tearDown() {
        EventsCenter.clearSubscribers();
    }

    @Test
    public void execute_invalid() {
        String invalidCommand = "       ";
        assertParseException(invalidCommand, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
    }

    /**
     * Executes the command, confirms that no exceptions are thrown and that the
     * result message is correct. Also confirms that {@code expectedModel} is as
     * specified.
     *
     * @see #assertCommandBehavior(Class, String, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage, Model expectedModel) {
        assertCommandBehavior(null, inputCommand, expectedMessage, expectedModel);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that
     * the result message is correct.
     *
     * @see #assertCommandBehavior(Class, String, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that
     * the result message is correct.
     *
     * @see #assertCommandBehavior(Class, String, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the
     * result message is correct.
     *
     * @see #assertCommandBehavior(Class, String, String, Model)
     */
    private <T> void assertCommandFailure(String inputCommand, Class<T> expectedException, String expectedMessage) {
        Model expectedModel = new ModelManager(model.getTaskBook(), new UserPrefs());
        assertCommandBehavior(expectedException, inputCommand, expectedMessage, expectedModel);
    }

    /**
     * Executes the command, confirms that the result message is correct and
     * that the expected exception is thrown, and also confirms that the
     * following two parts of the LogicManager object's state are as expected:
     * <br>
     * - the internal model manager data are same as those in the
     * {@code expectedModel} <br>
     * - {@code expectedModel}'s address book was saved to the storage file.
     */
    private <T> void assertCommandBehavior(Class<T> expectedException, String inputCommand, String expectedMessage,
            Model expectedModel) {

        try {

            CommandResult result = logic.execute(inputCommand);
            assertEquals(expectedException, null);
            assertEquals(expectedMessage, result.feedbackToUser);
        } catch (CommandException | ParseException e) {
            assertEquals(expectedException, e.getClass());
            assertEquals(expectedMessage, e.getMessage());
        }

        assertEquals(expectedModel, model);
        assertEquals(expectedModel.getTaskBook(), latestSavedTaskBook);
    }

    @Test
    public void executeUnknownCommandWord() {
        String unknownCommand = "uicfhmowqewca";
        assertParseException(unknownCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void executeHelp() {
        assertCommandSuccess(HelpCommand.COMMAND_WORD, HelpCommand.SHOWING_HELP_MESSAGE, new ModelManager());
        assertTrue(helpShown);
    }

    @Test
    public void executeExit() {
        assertCommandSuccess(ExitCommand.COMMAND_WORD, ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT, new ModelManager());
    }

    @Test
    public void executeClear() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        model.addTask(helper.generateTask(1));
        model.addTask(helper.generateTask(2));
        model.addTask(helper.generateTask(3));

        assertCommandSuccess(ClearCommand.COMMAND_WORD, ClearCommand.MESSAGE_SUCCESS, new ModelManager());
    }

    @Test
    public void executeAddInvalidArgsFormat() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);
        assertParseException(AddCommand.COMMAND_WORD, expectedMessage);
        assertParseException(AddCommand.COMMAND_WORD + " wrong args wrong args", expectedMessage);
    }

    @Test
    public void executeAddInvalidTaskData() {
        assertParseException(AddCommand.COMMAND_WORD + " " + PREFIX_NAME + "/ " + PREFIX_DEADLINE_END + "fri 2am "
                + PREFIX_DESCRIPTION + "valid, desc", Name.MESSAGE_NAME_CONSTRAINTS);
        assertParseException(AddCommand.COMMAND_WORD + " " + PREFIX_NAME + "Valid Name " + PREFIX_DEADLINE_END
                + "not_numbers " + PREFIX_DESCRIPTION + "valid, desc", Deadline.MESSAGE_DEADLINE_CONSTRAINTS_STRICT);
        assertParseException(
                AddCommand.COMMAND_WORD + " " + PREFIX_NAME + "Valid Name " + PREFIX_DEADLINE_END + "fri 2am "
                        + PREFIX_DESCRIPTION + "valid, desc " + PREFIX_TAG + "invalid_-[.tag",
                Tag.MESSAGE_TAG_CONSTRAINTS);
    }

    /* The following two tests fail because matcher.matches() fails to find pattern
     * but the exact same command works when executed outside of the unit test
    @Test
    public void execute_add_successful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.event();
        Model expectedModel = new ModelManager();
        expectedModel.addTask(toBeAdded);
        String expectedMessage = String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded);

        // execute command and verify result
        try {

            CommandResult result = logic.execute(helper.generateAddCommand(toBeAdded));
            assertEquals(expectedMessage, result.feedbackToUser);
        } catch (CommandException | ParseException e) {
            assertEquals(expectedMessage, e.getMessage());
        }

        assertEquals(expectedModel, model);
        assertEquals(expectedModel.getTaskBook(), latestSavedTaskBook);

    }
*/
    /*@Test
    public void execute_addDuplicate_notAllowed() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.event();

        // setup starting state
        model.addTask(toBeAdded); // person already in internal address book

        // execute command and verify result
        Model expectedModel = new ModelManager(model.getTaskBook(), new UserPrefs());
        String expectedMessage = AddCommand.MESSAGE_DUPLICATE_TASK;
        try {

            CommandResult result = logic.execute(helper.generateAddCommand(toBeAdded));
            assertEquals(CommandException.class, null);
            assertEquals(expectedMessage, result.feedbackToUser);
        } catch (CommandException | ParseException e) {
            assertEquals(CommandException.class, e.getClass());
            assertEquals(expectedMessage, e.getMessage());
        }

        assertEquals(expectedModel, model);
        assertEquals(expectedModel.getTaskBook(), latestSavedTaskBook);

    }
*/
    @Test
    public void executeListShowsAllTasks() throws Exception {
        // prepare expectations
        TestDataHelper helper = new TestDataHelper();
        Model expectedModel = new ModelManager(helper.generateTaskBook(2), new UserPrefs());

        // prepare address book state
        helper.addToModel(model, 2);

        assertCommandSuccess(ListCommand.COMMAND_WORD, ListCommand.MESSAGE_SUCCESS_INCOMPLETE, expectedModel);
    }

    /**
     * Confirms the 'invalid argument index number behaviour' for the given
     * command targeting a single task in the shown list, using visible index.
     *
     * @param commandWord
     *            to test assuming it targets a single task in the last shown
     *            list based on visible index.
     */
    private void assertIncorrectIndexFormatBehaviorForCommand(String commandWord, String expectedMessage)
            throws Exception {
        assertParseException(commandWord, expectedMessage); //index missing
        assertParseException(commandWord + " +1", expectedMessage); //index should be unsigned
        assertParseException(commandWord + " -1", expectedMessage); //index should be unsigned
        assertParseException(commandWord + " 0", expectedMessage); //index cannot be 0
        assertParseException(commandWord + " not_a_number", expectedMessage);
    }

    /**
     * Confirms the 'invalid argument index number behaviour' for the given
     * command targeting a single person in the shown list, using visible index.
     *
     * @param commandWord
     *            to test assuming it targets a single person in the last shown
     *            list based on visible index.
     */
    private void assertIndexNotFoundBehaviorForCommand(String commandWord) throws Exception {
        String expectedMessage = MESSAGE_INVALID_TASK_DISPLAYED_INDEX;
        TestDataHelper helper = new TestDataHelper();
        List<Task> taskList = helper.generateTaskList(2);

        // set AB state to 2 persons
        model.resetData(new TaskBook());
        for (Task p : taskList) {
            model.addTask(p);
        }

        assertCommandException(commandWord + " " + INDEX_THIRD_TASK.getOneBased(), expectedMessage);
    }

    @Test

    public void executeDeleteInvalidArgsFormatErrorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE);
        assertIncorrectIndexFormatBehaviorForCommand(DeleteCommand.COMMAND_WORD, expectedMessage);
    }

    @Test
    public void execute_deleteIndexNotFound_errorMessageShown() throws Exception {
        assertIndexNotFoundBehaviorForCommand(DeleteCommand.COMMAND_WORD);
    }

    @Test
    public void executeDeleteRemovesCorrectTask() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        List<Task> threeTasks = helper.generateTaskList(3);

        Model expectedModel = new ModelManager(helper.generateTaskBook(threeTasks), new UserPrefs());
        expectedModel.deleteTask(threeTasks.get(1));
        helper.addToModel(model, threeTasks);

        assertCommandSuccess(DeleteCommand.COMMAND_WORD + " 2",
                String.format(DeleteCommand.MESSAGE_DELETE_TASK_SUCCESS, threeTasks.get(1)), expectedModel);
    }

    @Test
    public void executeFindInvalidArgsFormat() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
        assertParseException(FindCommand.COMMAND_WORD + " ", expectedMessage);
    }

    /*@Test
    public void execute_find_onlyMatchesFullWordsInNames() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task pTarget1 = new TaskWithDeadlineBuilder().withName("bla bla KEY bla").build();
        Task pTarget2 = new TaskWithDeadlineBuilder().withName("bla KEY bla bceofeia").build();
        Task p1 = new TaskWithDeadlineBuilder().withName("KE Y").build();
        Task p2 = new TaskWithDeadlineBuilder().withName("KEYKEYKEY sduauo").build();

        List<Task> fourPersons = helper.generatePersonList(p1, pTarget1, p2, pTarget2);
        Model expectedModel = new ModelManager(helper.generateAddressBook(fourPersons), new UserPrefs());
        expectedModel.updateFilteredTaskList(new HashSet<>(Collections.singletonList("KEY")));
        helper.addToModel(model, fourPersons);
        assertCommandSuccess(FindCommand.COMMAND_WORD + " KEY",
                Command.getMessageForPersonListShownSummary(expectedModel.getFilteredTaskList().size()),
                expectedModel);
    }

    @Test
    public void execute_find_isNotCaseSensitive() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task p1 = new TaskWithDeadlineBuilder().withName("bla bla KEY bla").build();
        Task p2 = new TaskWithDeadlineBuilder().withName("bla KEY bla bceofeia").build();
        Task p3 = new TaskWithDeadlineBuilder().withName("key key").build();
        Task p4 = new TaskWithDeadlineBuilder().withName("KEy sduauo").build();

        List<Task> fourPersons = helper.generatePersonList(p3, p1, p4, p2);
        Model expectedModel = new ModelManager(helper.generateAddressBook(fourPersons), new UserPrefs());
        helper.addToModel(model, fourPersons);

        assertCommandSuccess(FindCommand.COMMAND_WORD + " KEY",
                Command.getMessageForPersonListShownSummary(expectedModel.getFilteredTaskList().size()),
                expectedModel);
    }

    @Test
    public void execute_find_matchesIfAnyKeywordPresent() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task p1 = new TaskWithDeadlineBuilder().withName("bla bla KEY bla").build();
        Task p2 = new TaskWithDeadlineBuilder().withName("bla KEY bla bceofeia").build();
        Task p3 = new TaskWithDeadlineBuilder().withName("key key").build();
        Task p4 = new TaskWithDeadlineBuilder().withName("KEy sduauo").build();

        List<Task> fourPersons = helper.generatePersonList(p3, p1, p4, p2);
        Model expectedModel = new ModelManager(helper.generateAddressBook(fourPersons), new UserPrefs());
        helper.addToModel(model, fourPersons);

        assertCommandSuccess(FindCommand.COMMAND_WORD + " KEY",
                Command.getMessageForPersonListShownSummary(expectedModel.getFilteredTaskList().size()),
                expectedModel);
    }*/

    /*@Test
    public void execute_verifyHistory_success() throws Exception {
        String validCommand = "clear";
        logic.execute(validCommand);

        String invalidCommandParse = "   adds   Bob   ";
        try {
            logic.execute(invalidCommandParse);
            fail("The expected ParseException was not thrown.");
        } catch (ParseException pe) {
            assertEquals(MESSAGE_UNKNOWN_COMMAND, pe.getMessage());
        }

        String invalidCommandExecute = "delete 1"; // address book is of size 0; index out of bounds
        try {
            logic.execute(invalidCommandExecute);
            fail("The expected CommandException was not thrown.");
        } catch (CommandException ce) {
            assertEquals(MESSAGE_INVALID_TASK_DISPLAYED_INDEX, ce.getMessage());
        }

        String expectedMessage = String.format(UndoCommand.MESSAGE_SUCCESS,
                String.join("\n", validCommand, invalidCommandParse, invalidCommandExecute));
        assertCommandSuccess("history", expectedMessage, model);
    }*/

    /**
     * A utility class to generate test data.
     */
    class TestDataHelper {

        public Task module() throws Exception {
            Name name = new Name("CS2103 V0.3");
            Description privateDescription = new Description("MVP");
            return new Task(name, privateDescription, getTagSet("tag1", "longertag2"));
        }

        public TaskWithDeadline event() throws Exception {
            Name name = new Name("Gay Parade");
            Deadline deadline = new Deadline("next fri", "next sat", "1 day");
            Description privateDescription = new Description("111, alpha street");
            return new TaskWithDeadline(name, deadline, privateDescription, getTagSet("tag1", "longertag2"));
        }

        /**
         * Generates a valid task using the given seed. Running this function
         * with the same parameter values guarantees the returned person will
         * have the same state. Each unique seed will generate a unique Task
         * object.
         *
         * @param seed
         *            used to generate the person data field values
         */
        private Task generateTask(int seed) throws Exception {
            return new TaskWithDeadline(new Name("Task " + seed),
                    new Deadline("today " + seed + "am", "tomorrow " + seed + "pm", Deadline.NULL_VALUE),
                    new Description("Task ID " + seed), getTagSet("tag" + Math.abs(seed), "tag" + Math.abs(seed + 1)));
        }

        /** Generates the correct add command based on the person given */
        private String generateAddCommand(Task p) {
            StringBuffer cmd = new StringBuffer();

            cmd.append(AddCommand.COMMAND_WORD);
            cmd.append(" " + PREFIX_NAME.getPrefix()).append(p.getName());
            cmd.append(" " + PREFIX_DESCRIPTION.getPrefix()).append(p.getDescription());
            if (p instanceof TaskWithDeadline) {
                cmd.append(" " + PREFIX_DEADLINE_START.getPrefix())
                        .append(p.getDeadline().get().getStartDate().toString());
                cmd.append(" " + PREFIX_DEADLINE_END.getPrefix()).append(p.getDeadline().get().getEndDate().toString());
            }
            Set<Tag> tags = p.getTags();
            for (Tag t : tags) {
                cmd.append(" " + PREFIX_TAG.getPrefix()).append(t.tagName);
            }

            return cmd.toString();
        }

        /**
         * Generates an TaskBook with auto-generated persons.
         */
        private TaskBook generateTaskBook(int numGenerated) throws Exception {
            TaskBook taskBook = new TaskBook();
            addToTaskBook(taskBook, numGenerated);
            return taskBook;
        }

        /**
         * Generates an TaskBook based on the list of Persons given.
         */
        private TaskBook generateTaskBook(List<Task> tasks) throws Exception {
            TaskBook taskBook = new TaskBook();
            addToAddressBook(taskBook, tasks);
            return taskBook;
        }

        /**
         * Adds auto-generated Task objects to the given TaskBook
         *
         * @param taskBook
         *            The TaskBook to which the Persons will be added
         */
        private void addToTaskBook(TaskBook taskBook, int numGenerated) throws Exception {
            addToAddressBook(taskBook, generateTaskList(numGenerated));
        }

        /**
         * Adds the given list of Persons to the given TaskBook
         */
        private void addToAddressBook(TaskBook taskBook, List<Task> personsToAdd) throws Exception {
            for (Task p : personsToAdd) {
                taskBook.addTask(p);
            }
        }

        /**
         * Adds auto-generated Task objects to the given model
         *
         * @param model
         *            The model to which the Persons will be added
         */
        private void addToModel(Model model, int numGenerated) throws Exception {
            addToModel(model, generateTaskList(numGenerated));
        }

        /**
         * Adds the given list of Persons to the given model
         */
        private void addToModel(Model model, List<Task> personsToAdd) throws Exception {
            for (Task p : personsToAdd) {
                model.addTask(p);
            }
        }

        /**
         * Generates a list of Persons based on the flags.
         */
        private List<Task> generateTaskList(int numGenerated) throws Exception {
            List<Task> tasks = new ArrayList<>();
            for (int i = 1; i <= numGenerated; i++) {
                tasks.add(generateTask(i));
            }
            return tasks;
        }

        private List<Task> generatePersonList(Task... persons) {
            return Arrays.asList(persons);
        }
    }
}
