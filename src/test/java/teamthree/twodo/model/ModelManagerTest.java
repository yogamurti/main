package teamthree.twodo.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import teamthree.twodo.testutil.AddressBookBuilder;
import teamthree.twodo.testutil.TypicalPersons;

public class ModelManagerTest {

    private TypicalPersons typicalPersons = new TypicalPersons();

    @Test
    public void equals() throws Exception {
        TaskBook taskBook = new AddressBookBuilder().withPerson(typicalPersons.alice)
                .withPerson(typicalPersons.benson).build();
        TaskBook differentAddressBook = new TaskBook();
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
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, userPrefs)));

        // different filteredList -> returns false
        modelManager.updateFilteredTaskList(new HashSet<>(
                Arrays.asList(typicalPersons.alice.getName().fullName.split(" "))));
        assertFalse(modelManager.equals(new ModelManager(taskBook, userPrefs)));
        modelManager.updateFilteredListToShowAllIncomplete(); // resets modelManager to initial state for upcoming tests

        // different userPrefs -> returns true
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setTaskBookName("differentName");
        assertTrue(modelManager.equals(new ModelManager(taskBook, differentUserPrefs)));
    }
}
