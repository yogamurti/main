package teamthree.twodo.storage;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.logging.Logger;

import teamthree.twodo.commons.core.LogsCenter;
import teamthree.twodo.commons.exceptions.DataConversionException;
import teamthree.twodo.commons.util.FileUtil;
import teamthree.twodo.model.ReadOnlyTaskList;
import teamthree.twodo.model.task.ReadOnlyTask;

/**
 * A class to access TaskBook data stored as an xml file on the hard disk.
 */
public class XmlTaskListStorage implements TaskListStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlTaskListStorage.class);

    protected String filePath;

    public XmlTaskListStorage(String filePath) {
        this.filePath = filePath;
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
        return readTaskList(filePath);
    }

    /**
     * Similar to {@link #readTaskList()}
     *
     * @param filePath
     *            location of the data. Cannot be null
     * @throws DataConversionException
     *             if the file is not in the correct format.
     */

    @Override
    public Optional<ReadOnlyTaskList> readTaskList(String filePath)
            throws DataConversionException, FileNotFoundException {
        requireNonNull(filePath);

        File taskListFile = new File(filePath);

        if (!taskListFile.exists()) {
            logger.info("TaskList file " + taskListFile + " not found");
            return Optional.empty();
        }

        ReadOnlyTaskList taskListOptional = XmlFileStorage.loadDataFromSaveFile(new File(filePath));

        return Optional.of(taskListOptional);
    }

    @Override
    public void saveTaskList(ReadOnlyTaskList taskList) throws IOException {
        saveTaskList(taskList, filePath);
    }

    /**
     * Similar to {@link #saveTaskList(ReadOnlyTaskList)}
     *
     * @param filePath
     *            location of the data. Cannot be null
     */
    @Override
    public void saveTaskList(ReadOnlyTaskList taskList, String filePath) throws IOException {
        requireNonNull(taskList);
        requireNonNull(filePath);

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        XmlFileStorage.saveDataToFile(file, new XmlSerializableTaskList(taskList));
    }

    /**
     * The following method saves notified tasks
     *
     * @param notified
     * @param filePath
     * @throws IOException
     */
    public void saveNotifiedTasks(HashSet<ReadOnlyTask> notified, String filePath) throws IOException {
        requireNonNull(notified);
        requireNonNull(filePath);

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        XmlFileStorage.saveNotificationToFile(file, XmlSerializableTaskList.getXmlSerializableTaskList(notified));
    }

}
