package teamthree.twodo.commons.events;

import teamthree.twodo.model.ReadOnlyTaskBook;

//@@author A0162253M
// Indicates and updates when a new model is to be loaded
public class LoadNewModelEvent extends BaseEvent {

    public final ReadOnlyTaskBook taskBook;

    public LoadNewModelEvent(ReadOnlyTaskBook taskBook) {
        this.taskBook = taskBook;
    }
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
