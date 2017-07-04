package teamthree.twodo.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import teamthree.twodo.logic.CommandHistory;
import teamthree.twodo.logic.commands.exceptions.CommandException;
import teamthree.twodo.model.Model;
import teamthree.twodo.model.ModelManager;
import teamthree.twodo.model.UserPrefs;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.testutil.PersonBuilder;
import teamthree.twodo.testutil.TypicalPersons;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(new TypicalPersons().getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newPerson_success() throws Exception {
        Task validPerson = new PersonBuilder().build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addTask(validPerson);

        CommandResult commandResult = prepareCommand(validPerson, model).execute();

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validPerson), commandResult.feedbackToUser);
        assertEquals(expectedModel, model);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() throws Exception {
        Task personInList = new Task(model.getAddressBook().getTaskList().get(0));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        try {
            prepareCommand(personInList, model).execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException ce) {
            assertEquals(AddCommand.MESSAGE_DUPLICATE_TASK, ce.getMessage());
            assertEquals(expectedModel, model);
        }
    }

    /**
     * Generates a new {@code AddCommand} which upon execution, adds {@code person} into the {@code model}.
     */
    private AddCommand prepareCommand(Task task, Model model) {
        AddCommand command = new AddCommand(task);
        command.setData(model, new CommandHistory());
        return command;
    }
}
