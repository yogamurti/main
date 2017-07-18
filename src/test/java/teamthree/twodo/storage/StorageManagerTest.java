package teamthree.twodo.storage;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import teamthree.twodo.commons.events.model.TaskBookChangedEvent;
import teamthree.twodo.commons.events.storage.DataSavingExceptionEvent;
import teamthree.twodo.commons.exceptions.DataConversionException;
import teamthree.twodo.model.ReadOnlyTaskBook;
import teamthree.twodo.model.TaskBook;
import teamthree.twodo.model.UserPrefs;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.testutil.EventsCollector;
import teamthree.twodo.testutil.TypicalTask;

public class StorageManagerTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private StorageManager storageManager;

    @Before
    public void setUp() {
        XmlTaskBookStorage addressBookStorage = new XmlTaskBookStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        storageManager = new StorageManager(addressBookStorage, userPrefsStorage);
    }


    private String getTempFilePath(String fileName) {
        return testFolder.getRoot().getPath() + fileName;
    }


    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Description: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(300, 600, 4, 6);
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void taskBookReadSave() throws Exception {
        /*
         * Description: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link XmlTaskBookStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link XmlTaskBookStorageTest} class.
         */
        TaskBook original = new TypicalTask().getTypicalTaskBook();
        storageManager.saveTaskBook(original);
        ReadOnlyTaskBook retrieved = storageManager.readTaskBook().get();
        assertEquals(original, new TaskBook(retrieved));
    }

    @Test
    public void getTaskBookFilePath() {
        assertNotNull(storageManager.getTaskBookFilePath());
    }

    //@@author A0162253M
    @Test
    public void setTaskBookFilePathSuccess() throws IOException {
        String expectedFilePath = storageManager.getTaskBookFilePath();
        //Create a StorageManager while injecting a stub that only allows the method setTaskBookFilePath() to be called
        Storage storage = new StorageManager(new XmlTaskBookStorageStub("dummy"),
                new JsonUserPrefsStorage("dummy"));
        storage.setTaskBookFilePath(expectedFilePath);
        assertEquals(storageManager.getTaskBookFilePath(), storage.getTaskBookFilePath());
    }


    @Test
    public void handleTaskBookChangedEvent_exceptionThrown_eventRaised() throws IOException {
        // Create a StorageManager while injecting a stub that  throws an exception when the save method is called
        Storage storage = new StorageManager(new XmlTaskBookStorageExceptionThrowingStub("dummy"),
                                             new JsonUserPrefsStorage("dummy"));
        EventsCollector eventCollector = new EventsCollector();
        storage.handleTaskBookChangedEvent(new TaskBookChangedEvent(new TaskBook()));
        assertTrue(eventCollector.get(0) instanceof DataSavingExceptionEvent);
    }


    /**
     * A Stub class to throw an exception when the save method is called
     */
    class XmlTaskBookStorageExceptionThrowingStub extends XmlTaskBookStorage {

        public XmlTaskBookStorageExceptionThrowingStub(String filePath) {
            super(filePath);
        }

        @Override
        public void saveTaskBook(ReadOnlyTaskBook addressBook, String filePath) throws IOException {
            throw new IOException("dummy exception");
        }
    }

    /**
     * A Stub class that only allows setTaskBookFilePath to be called
     * @author shuqi
     */
    //@@author A0162253M
    class XmlTaskBookStorageStub extends XmlTaskBookStorage {

        public XmlTaskBookStorageStub (String filePath) {
            super(filePath);
        }

        @Override
        public String getTaskBookFilePath() {
            return filePath;
        }

        @Override
        public void setTaskBookFilePath(String filePath) throws IOException {
            this.filePath = filePath;
        }

        @Override
        public Optional<ReadOnlyTaskBook> readTaskBook() throws DataConversionException, IOException {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public Optional<ReadOnlyTaskBook> readTaskBook(String filePath)
                throws DataConversionException, FileNotFoundException {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void saveTaskBook(ReadOnlyTaskBook addressBook) throws IOException {
            fail("This method should not be called.");
        }

        @Override
        public void saveTaskBook(ReadOnlyTaskBook addressBook, String filePath) throws IOException {
            fail("This method should not be called.");
        }

        @Override
        public void saveNotifiedTasks(HashSet<ReadOnlyTask> notified, String filePath) throws IOException {
            fail("This method should not be called.");
        }

    }


}
