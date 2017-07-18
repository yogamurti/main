package teamthree.twodo.commons.events.storage;

import teamthree.twodo.commons.events.BaseEvent;

//@@author A0162253M
// Indicates and updates when the storage of the task list has changed
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
