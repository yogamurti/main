package teamthree.twodo.storage;

import java.io.IOException;
import java.util.Optional;

import teamthree.twodo.commons.exceptions.DataConversionException;
import teamthree.twodo.model.ReadOnlyTaskList;

/**
 * Represents a storage for {@link teamthree.twodo.model.TaskBook}.
 */
public interface TaskListStorage {

    /**
     * Returns the file path of the data file.
     */
    String getTaskListFilePath();

    /**
     * Returns TaskBook data as a {@link ReadOnlyTaskBook}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyTaskList> readTaskList() throws DataConversionException, IOException;

    /**
     * @see #getTaskBookFilePath()
     */
    Optional<ReadOnlyTaskList> readTaskList(String filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyTaskBook} to the storage.
     * @param addressBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveTaskList(ReadOnlyTaskList taskList) throws IOException;

    /**
     * @see #saveTaskList(ReadOnlyTaskBook)
     */
    void saveTaskList(ReadOnlyTaskList taskList, String filePath) throws IOException;

    /**
     * Changes the filePath at which task book will be saved at
     * @param filePath cannot be invalid or null
     * @throws IOException if there is any problem in the filePath
     */
    void setTaskListFilePath(String filePath) throws IOException;

}
