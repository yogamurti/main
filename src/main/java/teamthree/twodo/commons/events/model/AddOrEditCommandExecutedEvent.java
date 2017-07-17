package teamthree.twodo.commons.events.model;

import teamthree.twodo.commons.events.BaseEvent;
import teamthree.twodo.model.task.ReadOnlyTask;
/**
 * Indicates that an Add/Edit Command has been executed.
 *
 */
public class AddOrEditCommandExecutedEvent extends BaseEvent {

    /*public static final int ADD_EVENT = -1;
    public final int targetIndex;
    */
    public final ReadOnlyTask task;

    public AddOrEditCommandExecutedEvent(ReadOnlyTask task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return "New/Edited Task: " + task.getAsText();
    }

}
