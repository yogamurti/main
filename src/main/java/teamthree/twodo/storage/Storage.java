package teamthree.twodo.storage;

import java.io.IOException;
import java.util.Optional;

import teamthree.twodo.commons.events.model.TaskBookChangedEvent;
import teamthree.twodo.commons.events.storage.DataSavingExceptionEvent;
import teamthree.twodo.commons.exceptions.DataConversionException;
import teamthree.twodo.model.ReadOnlyTaskBook;
import teamthree.twodo.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends TaskBookStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(UserPrefs userPrefs) throws IOException;

    @Override
    String getTaskBookFilePath();

    @Override
    Optional<ReadOnlyTaskBook> readTaskBook() throws DataConversionException, IOException;

    @Override
    void saveTaskBook(ReadOnlyTaskBook addressBook) throws IOException;

    /**
     * Saves the current version of the Note Book to the hard disk.
     *   Creates the data file if it is missing.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    void handleTaskBookChangedEvent(TaskBookChangedEvent abce);
}
