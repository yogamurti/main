package teamthree.twodo.model;

import java.util.Set;

import teamthree.twodo.commons.core.UnmodifiableObservableList;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.exceptions.DuplicateTaskException;
import teamthree.twodo.model.task.exceptions.TaskNotFoundException;

/**
 * The API of the Model component.
 */
public interface Model {
    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyTaskBook newData);

    /** Returns the TaskBook */
    ReadOnlyTaskBook getTaskBook();

    /** Deletes the given task. */
    void deleteTask(ReadOnlyTask target) throws TaskNotFoundException;

    /** Adds the given task. */
    void addTask(ReadOnlyTask person) throws DuplicateTaskException;

    /** Marks the given task as complete. */
    void markTask(ReadOnlyTask person) throws TaskNotFoundException;

    /** Marks the given task as incomplete. */
    void unmarkTask(ReadOnlyTask person) throws TaskNotFoundException;

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     *
     * @throws DuplicateTaskException if updating the person's details causes the person to be equivalent to
     *      another existing person in the list.
     * @throws TaskNotFoundException if {@code target} could not be found in the list.
     */
    void updateTask(ReadOnlyTask target, ReadOnlyTask editedPerson)
            throws DuplicateTaskException, TaskNotFoundException;

    /** Returns the filtered person list as an {@code UnmodifiableObservableList<ReadOnlyTask>} */
    UnmodifiableObservableList<ReadOnlyTask> getFilteredPersonList();

    /** Updates the filter of the filtered person list to show all persons */
    void updateFilteredListToShowAll();

    /** Updates the filter of the filtered person list to filter by the given keywords*/
    void updateFilteredTaskList(Set<String> keywords);

    void updateFilteredTaskList(ReadOnlyTask task);

    /** Saves the taskBook*/
    void saveTaskBook();

}
