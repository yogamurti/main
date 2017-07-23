package teamthree.twodo.ui;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import teamthree.twodo.commons.events.logic.NewUserInputEvent;
import teamthree.twodo.logic.Logic;

// Implements the commandbox user interface for the user to interact with the app
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";

    private ArrayList<String> previousUserInput;
    private int index;

    @FXML
    private TextField commandTextField;

    public CommandBox(Logic logic) {
        super(FXML);
        this.previousUserInput = logic.getCommandHistory().getHistory();
        index = previousUserInput.size();

    }

    //@@author A0162253M
    @FXML
    private void handleKeyPressed(KeyEvent e) {
        if (e.getCode().equals(KeyCode.UP)) {
            accessPreviousCommand();
        } else if (e.getCode().equals(KeyCode.DOWN)) {
            accessNextCommand();
        }
    }

    // Displays the previous command input on the command box if it is available
    private void accessPreviousCommand() {
        if (index > 0) {
            index--;
            commandTextField.clear();
            commandTextField.appendText(previousUserInput.get(index));
        }
    }

    private void accessNextCommand() {
        if (index < previousUserInput.size() - 1) {
            index++;
            commandTextField.clear();
            commandTextField.appendText(previousUserInput.get(index));
        }
    }
    public void setPreviousUserInput(ArrayList<String> newUserInputList) {
        previousUserInput = newUserInputList;
    }

    public void handleNewUserInputEvent(NewUserInputEvent e) {
        this.setPreviousUserInput(e.userInput);
        index = e.userInput.size();
    }

}
