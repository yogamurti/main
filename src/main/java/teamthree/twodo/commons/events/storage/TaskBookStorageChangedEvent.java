package teamthree.twodo.commons.events.storage;

import teamthree.twodo.commons.events.BaseEvent;

/**
 * Indicate to UI that the task book storage location has changed
 * @author shuqi
 *
 */

//@@author A0162253M
public class TaskBookStorageChangedEvent extends BaseEvent {

    public final String filePath;

    public TaskBookStorageChangedEvent(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
