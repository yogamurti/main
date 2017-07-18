package teamthree.twodo.commons.events.logic;

import java.util.ArrayList;

import teamthree.twodo.commons.events.BaseEvent;

//@@author A0162253M
//NewUserInputEvent to indicate that the user has typed in a new command
public class NewUserInputEvent extends BaseEvent {

    public final ArrayList<String> userInput;

    public NewUserInputEvent(ArrayList<String> userInput) {
        this.userInput = userInput;
    }

    @Override
    public String toString() {
        return "Updated List of previous user inputs for CommandBox";
    }

}
