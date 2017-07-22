package teamthree.twodo.commons.events.storage;

import teamthree.twodo.commons.events.BaseEvent;
/**
 * Indicates that the file path of the filePath has been changed
 */

//@@author A0162253M
// Indicates and updates when the file path of the task list has changed
public class TaskListFilePathChangedEvent extends BaseEvent {

    public final String filePath;

    public TaskListFilePathChangedEvent(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
