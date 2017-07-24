package teamthree.twodo.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import teamthree.twodo.commons.util.FileUtil;
//import teamthree.twodo.commons.exceptions.DataConversionException;
import teamthree.twodo.model.ReadOnlyTaskList;
import teamthree.twodo.model.TaskList;
import teamthree.twodo.model.task.Task;
//import teamthree.twodo.model.task.TaskWithDeadline;
import teamthree.twodo.testutil.TypicalTask;
import teamthree.twodo.testutil.TypicalTask.TaskType;

public class XmlTaskListStorageTest {
    private static final String TEST_DATA_FOLDER = FileUtil.getPath("./src/test/data/XmlTaskListStorageTest/");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readTaskListNullFilePathThrowsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readTaskList(null);
    }

    private java.util.Optional<ReadOnlyTaskList> readTaskList(String filePath) throws Exception {
        return new XmlTaskListStorage(filePath).readTaskList(addToTestDataPathIfNotNull(filePath));
    }

    private String addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER + prefsFileInTestDataFolder
                : null;
    }

    @Test
    public void readMissingFileEmptyResult() throws Exception {
        assertFalse(readTaskList("NonExistentFile.xml").isPresent());
    }

    @Test
    public void readAndSaveTaskListAllInOrderSuccess() throws Exception {
        String filePath = testFolder.getRoot().getPath() + "TempAddressBook.xml";
        TypicalTask td = new TypicalTask(TaskType.INCOMPLETE);
        TaskList original = td.getTypicalTaskList();
        XmlTaskListStorage xmlTaskListStorage = new XmlTaskListStorage(filePath);

        //Save in new file and read back
        xmlTaskListStorage.saveTaskList(original, filePath);
        ReadOnlyTaskList readBack = xmlTaskListStorage.readTaskList(filePath).get();
        assertEquals(original, new TaskList(readBack));

        //Modify data, overwrite exiting file, and read back
        original.addTask(new Task(td.supermarket));
        //original.removeTask(new TaskWithDeadline(td.alice));
        xmlTaskListStorage.saveTaskList(original, filePath);
        readBack = xmlTaskListStorage.readTaskList(filePath).get();
        assertEquals(original, new TaskList(readBack));

        //Save and read without specifying file path
        original.addTask(new Task(td.ida));
        xmlTaskListStorage.saveTaskList(original); //file path not specified
        readBack = xmlTaskListStorage.readTaskList().get(); //file path not specified
        assertEquals(original, new TaskList(readBack));

    }

    @Test
    public void saveTaskListNullTaskListThrowsNullPointerException() throws IOException {
        thrown.expect(NullPointerException.class);
        saveTaskList(null, "SomeFile.xml");
    }

    private void saveTaskList(ReadOnlyTaskList taskList, String filePath) throws IOException {
        new XmlTaskListStorage(filePath).saveTaskList(taskList, addToTestDataPathIfNotNull(filePath));
    }

    @Test
    public void saveTaskListNullFilePathThrowsNullPointerException() throws IOException {
        thrown.expect(NullPointerException.class);
        saveTaskList(new TaskList(), null);
    }


}
