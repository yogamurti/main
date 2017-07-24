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

import teamthree.twodo.commons.events.model.TaskListChangedEvent;
import teamthree.twodo.commons.events.storage.DataSavingExceptionEvent;
import teamthree.twodo.commons.exceptions.DataConversionException;
import teamthree.twodo.model.ReadOnlyTaskList;
import teamthree.twodo.model.TaskList;
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
        XmlTaskListStorage taskListStorage = new XmlTaskListStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        storageManager = new StorageManager(taskListStorage, userPrefsStorage);
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
    public void taskListReadSave() throws Exception {
        /*
         * Description: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link XmlTaskListStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link XmlTaskListStorageTest} class.
         */
        TaskList original = new TypicalTask().getTypicalTaskList();
        storageManager.saveTaskList(original);
        ReadOnlyTaskList retrieved = storageManager.readTaskList().get();
        assertEquals(original, new TaskList(retrieved));
    }

    @Test
    public void getTaskListFilePath() {
        assertNotNull(storageManager.getTaskListFilePath());
    }

    //@@author A0162253M
    @Test
    public void setTaskListFilePathSuccess() throws IOException {
        String expectedFilePath = storageManager.getTaskListFilePath();
        //Create a StorageManager while injecting a stub that only allows the method setTaskBookFilePath() to be called
        Storage storage = new StorageManager(new XmlTaskListStorageStub("dummy"),
                new JsonUserPrefsStorage("dummy"));
        storage.setTaskListFilePath(expectedFilePath);
        assertEquals(storageManager.getTaskListFilePath(), storage.getTaskListFilePath());
    }


    @Test
    public void handleTaskListChangedEventFailure() throws IOException {
        // Create a StorageManager while injecting a stub that  throws an exception when the save method is called
        Storage storage = new StorageManager(new XmlTaskListStorageExceptionThrowingStub("dummy"),
                                             new JsonUserPrefsStorage("dummy"));
        EventsCollector eventCollector = new EventsCollector();
        storage.handleTaskListChangedEvent(new TaskListChangedEvent(new TaskList()));
        assertTrue(eventCollector.get(0) instanceof DataSavingExceptionEvent);
    }


    /**
     * A Stub class to throw an exception when the save method is called
     */
    class XmlTaskListStorageExceptionThrowingStub extends XmlTaskListStorage {

        public XmlTaskListStorageExceptionThrowingStub(String filePath) {
            super(filePath);
        }

        @Override
        public void saveTaskList(ReadOnlyTaskList addressBook, String filePath) throws IOException {
            throw new IOException("dummy exception");
        }
    }

    /**
     * A Stub class that only allows setTaskListFilePath to be called
     * @author shuqi
     */
    //@@author A0162253M
    class XmlTaskListStorageStub extends XmlTaskListStorage {

        public XmlTaskListStorageStub (String filePath) {
            super(filePath);
        }

        @Override
        public String getTaskListFilePath() {
            return filePath;
        }

        @Override
        public void setTaskListFilePath(String filePath) throws IOException {
            this.filePath = filePath;
        }

        @Override
        public Optional<ReadOnlyTaskList> readTaskList() throws DataConversionException, IOException {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public Optional<ReadOnlyTaskList> readTaskList(String filePath)
                throws DataConversionException, FileNotFoundException {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void saveTaskList(ReadOnlyTaskList taskListk) throws IOException {
            fail("This method should not be called.");
        }

        @Override
        public void saveTaskList(ReadOnlyTaskList taskList, String filePath) throws IOException {
            fail("This method should not be called.");
        }

        @Override
        public void saveNotifiedTasks(HashSet<ReadOnlyTask> notified, String filePath) throws IOException {
            fail("This method should not be called.");
        }

    }


}
