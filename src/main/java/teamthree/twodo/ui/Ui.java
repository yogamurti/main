package teamthree.twodo.ui;

import javafx.stage.Stage;

/**
 * API of UI component
 */
public interface Ui {

    /** Starts the UI (and 2Do).  */
    void start(Stage primaryStage);

    /** Stops the UI. */
    void stop();

}
