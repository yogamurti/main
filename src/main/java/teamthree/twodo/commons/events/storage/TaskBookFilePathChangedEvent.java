package teamthree.twodo.commons.events.storage;

import teamthree.twodo.commons.events.BaseEvent;
/**
 * Indicates that the file path of the taskBook has been changed
 */

//@@author A0162253M
public class TaskBookFilePathChangedEvent extends BaseEvent {

    public final String filePath;

    public TaskBookFilePathChangedEvent(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
