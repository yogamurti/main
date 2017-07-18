package teamthree.twodo.commons.events.ui;

import teamthree.twodo.commons.events.BaseEvent;
import teamthree.twodo.logic.Logic;

public class LoadNewUiEvent extends BaseEvent {

    public final Logic logic;

    public LoadNewUiEvent(Logic logic) {
        this.logic = logic;
    }

    @Override
    public String toString() {
        return "Setting new Logic for User Interface";
    }
}
