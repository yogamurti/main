package teamthree.twodo.model;

import javafx.collections.ObservableList;
import teamthree.twodo.model.tag.Tag;
import teamthree.twodo.model.task.ReadOnlyTask;


/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyTaskBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<ReadOnlyTask> getTaskList();

    /**
     * Returns an unmodifiable view of the tags list.
     * This list will not contain any duplicate tags.
     */
    ObservableList<Tag> getTagList();

}
