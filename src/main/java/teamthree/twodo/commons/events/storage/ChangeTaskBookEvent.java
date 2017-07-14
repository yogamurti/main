//@@AUTHOR A0162253M
package teamthree.twodo.commons.events.storage;

import teamthree.twodo.commons.events.BaseEvent;
import teamthree.twodo.model.ReadOnlyTaskBook;

public class ChangeTaskBookEvent extends BaseEvent {

    public final ReadOnlyTaskBook taskBook;

    public ChangeTaskBookEvent(ReadOnlyTaskBook taskBook) {
        this.taskBook = taskBook;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
