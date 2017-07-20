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
 * A class to access TaskList data stored as an xml file on the hard disk.
 */
public class XmlTaskBookStorage implements TaskBookStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlTaskBookStorage.class);

    protected String filePath;

    public XmlTaskBookStorage(String filePath) {
        this.filePath = filePath;
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
    public Optional<ReadOnlyTaskList> readTaskBook() throws DataConversionException, IOException {
        return readTaskBook(filePath);
    }

    /**
     * Similar to {@link #readTaskBook()}
     *
     * @param filePath
     *            location of the data. Cannot be null
     * @throws DataConversionException
     *             if the file is not in the correct format.
     */

    @Override
    public Optional<ReadOnlyTaskList> readTaskBook(String filePath)
            throws DataConversionException, FileNotFoundException {
        requireNonNull(filePath);

        File taskBookFile = new File(filePath);

        if (!taskBookFile.exists()) {
            logger.info("TaskList file " + taskBookFile + " not found");
            return Optional.empty();
        }

        ReadOnlyTaskList taskBookOptional = XmlFileStorage.loadDataFromSaveFile(new File(filePath));

        return Optional.of(taskBookOptional);
    }

    @Override
    public void saveTaskBook(ReadOnlyTaskList taskBook) throws IOException {
        saveTaskBook(taskBook, filePath);
    }

    /**
     * Similar to {@link #saveTaskBook(ReadOnlyTaskList)}
     *
     * @param filePath
     *            location of the data. Cannot be null
     */
    @Override
    public void saveTaskBook(ReadOnlyTaskList taskBook, String filePath) throws IOException {
        requireNonNull(taskBook);
        requireNonNull(filePath);

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        XmlFileStorage.saveDataToFile(file, new XmlSerializableTaskBook(taskBook));
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
        XmlFileStorage.saveNotificationToFile(file, XmlSerializableTaskBook.getXmlSerializableTaskList(notified));
    }

}
