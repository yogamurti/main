package teamthree.twodo.commons.events.logic;

import teamthree.twodo.commons.events.BaseEvent;

public class LoadCommandExecutedEvent extends BaseEvent {

    public final String filePath;

    public LoadCommandExecutedEvent(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "Loading from filepath: " + filePath;
    }

}
