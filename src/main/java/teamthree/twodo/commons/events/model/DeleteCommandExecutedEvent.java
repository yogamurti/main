package teamthree.twodo.commons.events.model;

import teamthree.twodo.commons.events.BaseEvent;
import teamthree.twodo.model.task.ReadOnlyTask;

/**
 * Event indicating the successful execution of delete command.
 */
public class DeleteCommandExecutedEvent extends BaseEvent {

    public final ReadOnlyTask taskDeleted;

    public DeleteCommandExecutedEvent(ReadOnlyTask taskDeleted) {
        this.taskDeleted = taskDeleted;
    }
    //Returns true if taskDeleted had tags
    public boolean hasTags() {
        return !taskDeleted.getTags().isEmpty();
    }

    @Override
    public String toString() {
        return "Deleted:\n" + taskDeleted.getAsText();
    }

}
