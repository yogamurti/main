package teamthree.twodo.model;

import java.util.Set;

import teamthree.twodo.commons.core.UnmodifiableObservableList;
import teamthree.twodo.logic.commands.ListCommand.AttributeInputted;
import teamthree.twodo.model.task.Deadline;
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
    void addTask(ReadOnlyTask task) throws DuplicateTaskException;

    /** Marks the given task as complete. */
    void markTask(ReadOnlyTask task) throws TaskNotFoundException;

    /** Marks the given task as incomplete. */
    void unmarkTask(ReadOnlyTask task) throws TaskNotFoundException;

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     *
     * @throws DuplicateTaskException if updating the task's details causes the task to be equivalent to
     *      another existing task in the list.
     * @throws TaskNotFoundException if {@code target} could not be found in the list.
     */
    void updateTask(ReadOnlyTask target, ReadOnlyTask editedTask)
            throws DuplicateTaskException, TaskNotFoundException;

    /** Returns the filtered task list as an {@code UnmodifiableObservableList<ReadOnlyTask>} */
    UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList();

    /** Updates the filter of the filtered task list to show all complete tasks */
    void updateFilteredListToShowAllComplete();

    /** Updates the filter of the filtered task list to show all incomplete tasks */
    void updateFilteredListToShowAllIncomplete();

    /** Updates the filter of the filtered task list to filter by the given keywords*/
    void updateFilteredTaskList(Set<String> keywords);

    /** Updates an extensive filter of the filtered task list to filter by the given keywords*/
    void updateFilteredTaskListExtensively(Set<String> keywords, boolean listIncomplete);

    /** Updates the filter of the filtered task list to show all tasks within the period */
    void updateFilteredListToShowPeriod(Deadline deadline, AttributeInputted attInput, boolean listIncomplete);

    /** Saves the taskBook*/
    void saveTaskBook();

}
