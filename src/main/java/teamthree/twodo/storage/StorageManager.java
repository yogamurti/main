package teamthree.twodo.storage;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import teamthree.twodo.commons.core.ComponentManager;
import teamthree.twodo.commons.core.Config;
import teamthree.twodo.commons.core.LogsCenter;
import teamthree.twodo.commons.events.alarm.DeadlineNotificationTimeReachedEvent;
import teamthree.twodo.commons.events.model.TaskBookChangedEvent;
import teamthree.twodo.commons.events.storage.DataSavingExceptionEvent;
import teamthree.twodo.commons.events.storage.TaskBookFilePathChangedEvent;
import teamthree.twodo.commons.events.storage.TaskBookStorageChangedEvent;
import teamthree.twodo.commons.exceptions.DataConversionException;
import teamthree.twodo.commons.util.ConfigUtil;
import teamthree.twodo.logic.commands.SaveCommand;
import teamthree.twodo.logic.commands.exceptions.CommandException;
import teamthree.twodo.model.ReadOnlyTaskBook;
import teamthree.twodo.model.UserPrefs;
import teamthree.twodo.model.task.ReadOnlyTask;

/**
 * Manages storage of TaskBook data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private TaskBookStorage taskBookStorage;
    private UserPrefsStorage userPrefsStorage;
    private Config config;

    public StorageManager(TaskBookStorage taskBookStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.taskBookStorage = taskBookStorage;
        this.userPrefsStorage = userPrefsStorage;
        this.config = new Config();
    }

    // ================ UserPrefs methods ==============================

    @Override
    public String getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }

    // ================ TaskBook methods ==============================

    @Override
    public String getTaskBookFilePath() {
        return taskBookStorage.getTaskBookFilePath();
    }

    @Override
    //@@author A0162253M
    public void setTaskBookFilePath(String filePath) throws IOException {
        taskBookStorage.setTaskBookFilePath(filePath);
        config.setTaskBookFilePath(filePath);
        ConfigUtil.saveConfig(config, filePath);
        raise(new TaskBookStorageChangedEvent(filePath));
    }

    @Override
    public Optional<ReadOnlyTaskBook> readTaskBook() throws DataConversionException, IOException {
        return readTaskBook(taskBookStorage.getTaskBookFilePath());
    }

    @Override
    public Optional<ReadOnlyTaskBook> readTaskBook(String filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return taskBookStorage.readTaskBook(filePath);
    }

    @Override
    public void saveTaskBook(ReadOnlyTaskBook taskBook) throws IOException {
        saveTaskBook(taskBook, taskBookStorage.getTaskBookFilePath());
    }

    @Override
    public void saveTaskBook(ReadOnlyTaskBook taskBook, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        taskBookStorage.saveTaskBook(taskBook, filePath);
    }

    public void saveNotifiedTasks(HashSet<ReadOnlyTask> notified, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
    }

    @Override
    @Subscribe
    public void handleTaskBookChangedEvent(TaskBookChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveTaskBook(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

    @Subscribe
    private void handleDeadlineNotificationTimeReachedEvent(DeadlineNotificationTimeReachedEvent event) {

    }

    @Subscribe
    private void handleTaskBookFilePathChangedEvent(TaskBookFilePathChangedEvent event) throws CommandException {
      logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local filePath changed, saving to config"));
      try {
          setTaskBookFilePath(event.filePath);
      } catch (IOException e) {
        throw new CommandException(String.format(SaveCommand.MESSAGE_FAILURE, event.filePath));
      }
  }
}
