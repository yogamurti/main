package teamthree.twodo.commons.events.alarm;

import java.util.ArrayList;
import java.util.List;

import teamthree.twodo.commons.events.BaseEvent;
import teamthree.twodo.model.task.ReadOnlyTask;

public class DeadlineNotificationTimeReachedEvent extends BaseEvent {

    public final List<ReadOnlyTask> tasksNearingDeadline = new ArrayList<ReadOnlyTask>();

    public DeadlineNotificationTimeReachedEvent(List<ReadOnlyTask> tasks) {
        tasksNearingDeadline.addAll(tasks);
    }

    @Override
    public String toString() {
        return "number of tasks " + tasksNearingDeadline.size() + ". First Task: "
                + tasksNearingDeadline.get(0).getAsText();
    }

}
