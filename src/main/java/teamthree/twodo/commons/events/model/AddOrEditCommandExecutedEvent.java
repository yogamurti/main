package teamthree.twodo.commons.events.model;

import teamthree.twodo.commons.events.BaseEvent;
/**
 * Indicates that an Add/Edit Command has been executed.
 *
 */
public class AddOrEditCommandExecutedEvent extends BaseEvent {

    public static final int ADD_EVENT = -1;
    public final int targetIndex;

    public AddOrEditCommandExecutedEvent(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public String toString() {
        return "Index of New/Edited Task: " + targetIndex;
    }

}
