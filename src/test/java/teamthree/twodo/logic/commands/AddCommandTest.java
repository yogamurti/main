package teamthree.twodo.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import teamthree.twodo.commons.core.UnmodifiableObservableList;
import teamthree.twodo.commons.exceptions.IllegalValueException;
import teamthree.twodo.logic.CommandHistory;
import teamthree.twodo.logic.commands.exceptions.CommandException;
import teamthree.twodo.model.Model;
import teamthree.twodo.model.ReadOnlyTaskBook;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.model.task.exceptions.DuplicateTaskException;
import teamthree.twodo.model.task.exceptions.TaskNotFoundException;
import teamthree.twodo.testutil.PersonBuilder;

public class AddCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullPerson_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        new AddCommand(null);
    }

    @Test
    public void execute_personAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Task validPerson = new PersonBuilder().build();

        CommandResult commandResult = getAddCommandForPerson(validPerson, modelStub).execute();

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validPerson), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validPerson), modelStub.personsAdded);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() throws Exception {
        ModelStub modelStub = new ModelStubThrowingDuplicatePersonException();
        Task validPerson = new PersonBuilder().build();

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_TASK);

        getAddCommandForPerson(validPerson, modelStub).execute();
    }

    /**
     * Generates a new AddCommand with the details of the given person.
     */
    private AddCommand getAddCommandForPerson(Task task, Model model) throws IllegalValueException {
        AddCommand command = new AddCommand(task);
        command.setData(model, new CommandHistory());
        return command;
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void addTask(ReadOnlyTask person) throws DuplicateTaskException {
            fail("This method should not be called.");
        }

        @Override
        public void resetData(ReadOnlyTaskBook newData) {
            fail("This method should not be called.");
        }

        @Override
        public ReadOnlyTaskBook getAddressBook() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void deleteTask(ReadOnlyTask target) throws TaskNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void updateTask(ReadOnlyTask target, ReadOnlyTask editedPerson)
                throws DuplicateTaskException {
            fail("This method should not be called.");
        }

        @Override
        public UnmodifiableObservableList<ReadOnlyTask> getFilteredPersonList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void updateFilteredListToShowAll() {
            fail("This method should not be called.");
        }

        @Override
        public void updateFilteredTaskList(Set<String> keywords) {
            fail("This method should not be called.");
        }
    }

    /**
     * A Model stub that always throw a DuplicateTaskException when trying to add a person.
     */
    private class ModelStubThrowingDuplicatePersonException extends ModelStub {
        @Override
        public void addTask(ReadOnlyTask person) throws DuplicateTaskException {
            throw new DuplicateTaskException();
        }
    }

    /**
     * A Model stub that always accept the person being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Task> personsAdded = new ArrayList<>();

        @Override
        public void addTask(ReadOnlyTask person) throws DuplicateTaskException {
            personsAdded.add(new Task(person));
        }
    }

}
