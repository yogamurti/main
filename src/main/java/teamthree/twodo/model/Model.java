package teamthree.twodo.model;

import java.util.Set;

import teamthree.twodo.commons.core.UnmodifiableObservableList;
import teamthree.twodo.logic.commands.ListCommand.AttributeInputted;
import teamthree.twodo.model.tag.Tag;
import teamthree.twodo.model.task.Deadline;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.exceptions.DuplicateTaskException;
import teamthree.twodo.model.task.exceptions.TaskNotFoundException;

/**
 * The API of the Model component.
 */
public interface Model {
    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyTaskList newData);

    /** Returns the TaskList */
    ReadOnlyTaskList getTaskList();

    /** Changes the TaskList */
    void setTaskList(ReadOnlyTaskList taskList);

    /** Deletes the given task. */
    void deleteTask(ReadOnlyTask target) throws TaskNotFoundException;

    /** Adds the given task. */
    void addTask(ReadOnlyTask task) throws DuplicateTaskException;

    /** Marks the given task as complete. */
    void markTask(ReadOnlyTask task) throws TaskNotFoundException;

    /** Marks the given task as incomplete. */
    void unmarkTask(ReadOnlyTask task) throws TaskNotFoundException;

    /** Indicates a change in the default options of 2Do. */
    void changeOptions();

    /**
     * Replaces the given task {@code target} with {@code editedTask}.
     *
     * @throws DuplicateTaskException if updating the task's details causes the task to be equivalent to
     *      another existing task in the list.
     * @throws TaskNotFoundException if {@code target} could not be found in the list.
     */
    void updateTask(ReadOnlyTask target, ReadOnlyTask editedTask)
            throws DuplicateTaskException, TaskNotFoundException;

    /** Returns the filtered task list as an {@code UnmodifiableObservableList<ReadOnlyTask>} */
    UnmodifiableObservableList<ReadOnlyTask> getFilteredAndSortedTaskList();

    /** Updates the filter of the filtered task list to show all incomplete tasks */
    void updateFilteredTaskListToShowAll(Set<Tag> tagList, boolean listFloating, boolean listIncomplete);

    /** Updates an extensive filter of the filtered task list to filter by the given keywords*/
    void updateFilteredTaskListByKeywords(Set<String> keywords, boolean listIncomplete);

    /** Updates the filter of the filtered task list to show all tasks within the period */
    void updateFilteredTaskListToShowPeriod(Deadline deadline, AttributeInputted attInput,
            boolean listIncomplete, Set<Tag> tagList);

    /** Saves the filePath*/
    void saveTaskList();

    /** Sorts the filePath*/
    void sort();

    /** Updates the filter to show no tasks*/
    void updateFilteredTaskListToEmpty();

}
