package teamthree.twodo.storage;

import java.io.IOException;
import java.util.Optional;

import teamthree.twodo.commons.exceptions.DataConversionException;
import teamthree.twodo.model.ReadOnlyTaskList;

/**
 * Represents a storage for {@link teamthree.twodo.model.TaskList}.
 */
public interface TaskBookStorage {

    /**
     * Returns the file path of the data file.
     */
    String getTaskBookFilePath();

    /**
     * Returns TaskList data as a {@link ReadOnlyTaskList}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyTaskList> readTaskBook() throws DataConversionException, IOException;

    /**
     * @see #getTaskBookFilePath()
     */
    Optional<ReadOnlyTaskList> readTaskBook(String filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyTaskList} to the storage.
     * @param addressBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveTaskBook(ReadOnlyTaskList addressBook) throws IOException;

    /**
     * @see #saveTaskBook(ReadOnlyTaskList)
     */
    void saveTaskBook(ReadOnlyTaskList addressBook, String filePath) throws IOException;

    /**
     * Changes the filePath at which task book will be saved at
     * @param filePath cannot be invalid or null
     * @throws IOException if there is any problem in the filePath
     */
    void setTaskBookFilePath(String filePath) throws IOException;

}
