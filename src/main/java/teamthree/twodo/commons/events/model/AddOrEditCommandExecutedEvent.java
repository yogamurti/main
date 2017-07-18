package teamthree.twodo.commons.events.model;

import teamthree.twodo.commons.events.BaseEvent;
import teamthree.twodo.model.task.ReadOnlyTask;

//@@author A0124399W
// Indicates that an Add/Edit Command has been executed
public class AddOrEditCommandExecutedEvent extends BaseEvent {

    public final ReadOnlyTask task;
    public AddOrEditCommandExecutedEvent(ReadOnlyTask task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return "New/Edited Task: " + task.getAsText();
    }

}
