package teamthree.twodo.ui;

import java.util.List;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import teamthree.twodo.MainApp;
import teamthree.twodo.commons.core.ComponentManager;
import teamthree.twodo.commons.core.Config;
import teamthree.twodo.commons.core.LogsCenter;
import teamthree.twodo.commons.events.alarm.DeadlineNotificationTimeReachedEvent;
import teamthree.twodo.commons.events.model.AddOrEditCommandExecutedEvent;
import teamthree.twodo.commons.events.storage.DataSavingExceptionEvent;
import teamthree.twodo.commons.events.ui.JumpToListRequestEvent;
import teamthree.twodo.commons.events.ui.ShowHelpRequestEvent;
import teamthree.twodo.commons.events.ui.TaskPanelSelectionChangedEvent;
import teamthree.twodo.commons.util.StringUtil;
import teamthree.twodo.logic.Logic;
import teamthree.twodo.model.UserPrefs;
import teamthree.twodo.model.task.ReadOnlyTask;

/**
 * The manager of the UI component.
 */
public class UiManager extends ComponentManager implements Ui {

    public static final String ALERT_DIALOG_PANE_FIELD_ID = "alertDialogPane";

    private static final Logger logger = LogsCenter.getLogger(UiManager.class);
    private static final String ICON_APPLICATION = "/images/address_book_32.png";

    private Logic logic;
    private Config config;
    private UserPrefs prefs;
    private MainWindow mainWindow;

    public UiManager(Logic logic, Config config, UserPrefs prefs) {
        super();
        this.logic = logic;
        this.config = config;
        this.prefs = prefs;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting UI...");
        primaryStage.setTitle(config.getAppTitle());

        //Set the application icon.
        primaryStage.getIcons().add(getImage(ICON_APPLICATION));

        try {
            mainWindow = new MainWindow(primaryStage, config, prefs, logic);
            mainWindow.show(); //This should be called before creating other UI parts
            mainWindow.fillInnerParts();

        } catch (Throwable e) {
            logger.severe(StringUtil.getDetails(e));
            showFatalErrorDialogAndShutdown("Fatal error during initializing", e);
        }
    }

    @Override
    public void stop() {
        prefs.updateLastUsedGuiSetting(mainWindow.getCurrentGuiSetting());
        mainWindow.hide();
        mainWindow.releaseResources();
    }

    private void showFileOperationAlertAndWait(String description, String details, Throwable cause) {
        final String content = details + ":\n" + cause.toString();
        showAlertDialogAndWait(AlertType.ERROR, "File Op Error", description, content);
    }

    private void showReminderAlertAndWait(List<ReadOnlyTask> tasksNearing) {
        final StringBuilder content = new StringBuilder("");
        tasksNearing.forEach((t) -> {
            content.append(t.getName() + "\n");
        });
        /**
         * The following prevents an illegal state exception from being thrown.
         * Need to be on JavaFX thread to update UI. When this method is called,
         * program will still be on Timer thread in the AlarmManager which
         * raised the deadline reached event. RunLater allows the UI call inside
         * to wait until the program is back in the JavaFX thread.
         */
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                showAlertDialogAndWait(AlertType.INFORMATION, "Deadline Reaching",
                        "The Following tasks are nearing their deadlines\n", content.toString());
            }
        });
    }

    private Image getImage(String imagePath) {
        return new Image(MainApp.class.getResourceAsStream(imagePath));
    }

    void showAlertDialogAndWait(Alert.AlertType type, String title, String headerText, String contentText) {
        showAlertDialogAndWait(mainWindow.getPrimaryStage(), type, title, headerText, contentText);
    }

    private static void showAlertDialogAndWait(Stage owner, AlertType type, String title, String headerText,
            String contentText) {
        final Alert alert = new Alert(type);
        alert.getDialogPane().getStylesheets().add("view/DarkTheme.css");
        alert.initOwner(owner);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.getDialogPane().setId(ALERT_DIALOG_PANE_FIELD_ID);
        alert.showAndWait();
    }

    private void showFatalErrorDialogAndShutdown(String title, Throwable e) {
        logger.severe(title + " " + e.getMessage() + StringUtil.getDetails(e));
        showAlertDialogAndWait(Alert.AlertType.ERROR, title, e.getMessage(), e.toString());
        Platform.exit();
        System.exit(1);
    }

    //==================== Event Handling Code ===============================================================

    @Subscribe
    private void handleDataSavingExceptionEvent(DataSavingExceptionEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        showFileOperationAlertAndWait("Could not save data", "Could not save data to file", event.exception);
    }

    @Subscribe
    private void handleShowHelpEvent(ShowHelpRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        mainWindow.handleHelp();
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        mainWindow.getTaskListPanel().scrollTo(event.targetIndex);
    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(TaskPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        mainWindow.loadPersonPage(event.getNewSelection());
    }

    @Subscribe
    private void handleDeadlineNotificationTimeReachedEvent(DeadlineNotificationTimeReachedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        showReminderAlertAndWait(event.tasksNearingDeadline);
    }

    @Subscribe
    private void handleAddOrEditCommandExecutedEvent(AddOrEditCommandExecutedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        //if (event.targetIndex == AddOrEditCommandExecutedEvent.ADD_EVENT) {
        mainWindow.getTaskListPanel()
                .scrollTo(mainWindow.getTaskListPanel().getRoot().getChildrenUnmodifiable().indexOf(event.task));
        /*
         * } else { mainWindow.getTaskListPanel().scrollTo(event.targetIndex); }
         */
    }

}
