package teamthree.twodo.commons.events.storage;

import teamthree.twodo.commons.events.BaseEvent;

public class ReadTaskBookEvent extends BaseEvent {

    public final String filePath;

    public ReadTaskBookEvent(String filePath) {
        this.filePath = filePath;
    }
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
