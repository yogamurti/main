package teamthree.twodo.commons.events.model;

import teamthree.twodo.commons.events.BaseEvent;
import teamthree.twodo.model.ReadOnlyTaskBook;

/** Indicates the TaskBook in the model has changed*/
public class AddressBookChangedEvent extends BaseEvent {

    public final ReadOnlyTaskBook data;

    public AddressBookChangedEvent(ReadOnlyTaskBook data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "number of persons " + data.getTaskList().size() + ", number of tags " + data.getTagList().size();
    }
}
