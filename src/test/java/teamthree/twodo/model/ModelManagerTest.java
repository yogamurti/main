package teamthree.twodo.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import teamthree.twodo.logic.commands.ListCommand.AttributeInputted;
import teamthree.twodo.model.task.Deadline;
import teamthree.twodo.testutil.TaskBookBuilder;
import teamthree.twodo.testutil.TypicalTask;

//@@author A0107433N
public class ModelManagerTest {

    private TypicalTask typicalTask = new TypicalTask();

    @Test
    public void equals() throws Exception {
        TaskBook taskBook = new TaskBookBuilder().withTask(typicalTask.partyCompleted)
                .withTask(typicalTask.cs2103).withTask(typicalTask.cs1020).build();
        TaskBook differentTaskBook = new TaskBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        ModelManager modelManager = new ModelManager(taskBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(taskBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentTaskBook, userPrefs)));

        // different filteredList (key words) -> returns false
        modelManager.updateFilteredTaskListByKeywords(new HashSet<>(
                Arrays.asList(typicalTask.cs2103.getName().fullName.split(" "))), true);
        assertFalse(modelManager.equals(new ModelManager(taskBook, userPrefs)));
        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredTaskListToShowAll(null, false, true);

        // different filteredList (completed) -> return false
        modelManager.updateFilteredTaskListToShowAll(null, false, false);
        assertFalse(modelManager.equals(new ModelManager(taskBook, userPrefs)));
        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredTaskListToShowAll(null, false, true);

        // different filteredList (period) -> return false
        modelManager.updateFilteredTaskListToShowPeriod(new Deadline("yesterday 10am", "yesterday 10am",
                Deadline.NULL_VALUE), AttributeInputted.START, true, null);
        assertFalse(modelManager.equals(new ModelManager(taskBook, userPrefs)));
        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredTaskListToShowAll(null, false, true);

        // different sortedList -> returns true
        modelManager.sort();
        assertTrue(modelManager.equals(new ModelManager(taskBook, userPrefs)));
        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredTaskListToShowAll(null, false, true);

        // different userPrefs -> returns true
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setTaskBookName("differentName");
        assertTrue(modelManager.equals(new ModelManager(taskBook, differentUserPrefs)));
    }
}
