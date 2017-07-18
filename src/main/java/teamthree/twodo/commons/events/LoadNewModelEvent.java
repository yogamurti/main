package teamthree.twodo.commons.events;

//@@author A0162253M
// Indicates and updates when a new model is loaded
public class LoadNewModelEvent extends BaseEvent {

    public final String filePath;

    public LoadNewModelEvent(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "Loading new Model from " + filePath;
    }

}
