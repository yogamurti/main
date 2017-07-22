package teamthree.twodo.storage;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import teamthree.twodo.commons.core.ComponentManager;
import teamthree.twodo.commons.core.Config;
import teamthree.twodo.commons.core.LogsCenter;
import teamthree.twodo.commons.core.Messages;
import teamthree.twodo.commons.events.LoadNewModelEvent;
import teamthree.twodo.commons.events.logic.LoadCommandExecutedEvent;
import teamthree.twodo.commons.events.model.TaskListChangedEvent;
import teamthree.twodo.commons.events.storage.DataSavingExceptionEvent;
import teamthree.twodo.commons.events.storage.TaskListFilePathChangedEvent;
import teamthree.twodo.commons.events.storage.TaskListStorageChangedEvent;
import teamthree.twodo.commons.exceptions.DataConversionException;
import teamthree.twodo.commons.util.ConfigUtil;
import teamthree.twodo.logic.commands.SaveCommand;
import teamthree.twodo.logic.commands.exceptions.CommandException;
import teamthree.twodo.model.ReadOnlyTaskList;
import teamthree.twodo.model.UserPrefs;
import teamthree.twodo.model.task.ReadOnlyTask;

// Manages storage of TaskList data in local storage
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private TaskListStorage taskBookStorage;
    private UserPrefsStorage userPrefsStorage;
    private Config config;

    public StorageManager(TaskListStorage taskBookStorage, UserPrefsStorage userPrefsStorage) {
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
    public String getTaskListFilePath() {
        return taskBookStorage.getTaskListFilePath();
    }

    @Override
    //@@author A0162253M
    public void setTaskListFilePath(String filePath) throws IOException {
        taskBookStorage.setTaskListFilePath(filePath);
        config.setTaskListFilePath(filePath);
        ConfigUtil.saveConfig(config, Config.getDefaultConfigFile());
        raise(new TaskListStorageChangedEvent(filePath));
    }

    @Override
    public Optional<ReadOnlyTaskList> readTaskList() throws DataConversionException, IOException {
        return readTaskList(taskBookStorage.getTaskListFilePath());
    }

    @Override
    public Optional<ReadOnlyTaskList> readTaskList(String filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return taskBookStorage.readTaskList(filePath);
    }

    @Override
    public void saveTaskList(ReadOnlyTaskList taskList) throws IOException {
        saveTaskList(taskList, taskBookStorage.getTaskListFilePath());
    }

    @Override
    public void saveTaskList(ReadOnlyTaskList taskList, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        taskBookStorage.saveTaskList(taskList, filePath);
    }

    public void saveNotifiedTasks(HashSet<ReadOnlyTask> notified, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
    }

    @Override
    @Subscribe
    public void handleTaskListChangedEvent(TaskListChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveTaskList(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

    @Subscribe
    private void handleTaskListFilePathChangedEvent(TaskListFilePathChangedEvent event) throws CommandException {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local filePath changed, saving to config"));
        try {
            setTaskListFilePath(event.filePath);
        } catch (IOException e) {
            throw new CommandException(String.format(SaveCommand.MESSAGE_INVALID_PATH, event.filePath));
        }
    }

    @Subscribe
    public void handleLoadCommandExecutedEvent(LoadCommandExecutedEvent event) throws CommandException {
        logger.info(
                LogsCenter.getEventHandlingLogMessage(event, "Load command executed, saving new filepath to config"));
        try {
            Optional<ReadOnlyTaskList> loadedTaskBook;
            if ((loadedTaskBook = readTaskList(event.filePath)).isPresent()) {
                setTaskListFilePath(event.filePath);
                raise(new LoadNewModelEvent(loadedTaskBook.get()));
            }
        } catch (IOException e) {
            throw new CommandException(String.format(SaveCommand.MESSAGE_INVALID_PATH, event.filePath));
        } catch (DataConversionException e) {
            throw new CommandException(Messages.MESSAGE_LOAD_FAILED);
        }
    }
}
