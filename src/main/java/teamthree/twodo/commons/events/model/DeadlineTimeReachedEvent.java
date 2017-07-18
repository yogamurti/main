package teamthree.twodo.commons.events.model;

import java.util.ArrayList;
import java.util.List;

import teamthree.twodo.commons.events.BaseEvent;
import teamthree.twodo.model.task.ReadOnlyTask;

//@@author A0139267W
// Sets up for auto-completion when a task's deadline has elapsed
public class DeadlineTimeReachedEvent extends BaseEvent {

    public final List<ReadOnlyTask> tasksNearingDeadline = new ArrayList<ReadOnlyTask>();

    public DeadlineTimeReachedEvent(List<ReadOnlyTask> tasks) {
        tasksNearingDeadline.addAll(tasks);
    }

    @Override
    public String toString() {
        return "number of tasks auto marked as completed " + tasksNearingDeadline.size() + ". First Task: "
                + tasksNearingDeadline.get(0).getAsText();
    }

}
