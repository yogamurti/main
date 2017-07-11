//@@author A0162253M
package teamthree.twodo.commons.events.storage;

import teamthree.twodo.commons.events.BaseEvent;

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
