package teamthree.twodo.commons.events.logic;

import teamthree.twodo.commons.events.BaseEvent;

/**
 * NewUserInputEvent to indicate that the user has typed in a new command.
 *
 */
public class NewUserInputEvent extends BaseEvent {
    public final String userInput;

    public NewUserInputEvent(String userInput) {
        this.userInput = userInput;
    }

    @Override
    public String toString() {
        return "NewUserInputEvent posted with " + userInput;
    }

}
