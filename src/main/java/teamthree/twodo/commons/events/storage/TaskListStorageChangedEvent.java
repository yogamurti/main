package teamthree.twodo.commons.events.storage;

import teamthree.twodo.commons.events.BaseEvent;

/**
 * Indicate to UI that the task book storage location has changed
 * @author shuqi
 *
 */

//@@author A0162253M
// Indicates and updates when the storage of the task list has changed
public class TaskListStorageChangedEvent extends BaseEvent {

    public final String filePath;

    public TaskListStorageChangedEvent(String filepath) {
        this.filePath = filepath;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
