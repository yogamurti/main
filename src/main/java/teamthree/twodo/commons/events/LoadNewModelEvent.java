package teamthree.twodo.commons.events;

import teamthree.twodo.model.ReadOnlyTaskList;

//@@author A0162253M
// Indicates and updates when a new model is to be loaded
public class LoadNewModelEvent extends BaseEvent {

    public final ReadOnlyTaskList taskList;

    public LoadNewModelEvent(ReadOnlyTaskList taskBook) {
        this.taskList = taskBook;
    }
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
