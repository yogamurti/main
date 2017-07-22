package teamthree.twodo.model.task;

import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import teamthree.twodo.commons.core.UnmodifiableObservableList;
import teamthree.twodo.commons.util.CollectionUtil;
import teamthree.twodo.model.task.exceptions.DuplicateTaskException;
import teamthree.twodo.model.task.exceptions.TaskNotFoundException;

/**
 * A list of tasks that enforces uniqueness between its elements and does not
 * allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Task#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueTaskList implements Iterable<Task> {

    private final ObservableList<Task> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent person as the given
     * argument.
     */
    public boolean contains(ReadOnlyTask toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a task to the list.
     *
     * @throws DuplicateTaskException
     *             if the task to add is a duplicate of an existing task in
     *             the list.
     */
    public void add(ReadOnlyTask toAdd) throws DuplicateTaskException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateTaskException();
        }
        if (toAdd instanceof TaskWithDeadline) {
            internalList.add(new TaskWithDeadline(toAdd));
        } else {
            internalList.add(new Task(toAdd));
        }
    }

    /**
     * Replaces the task {@code target} in the list with {@code editedTask}.
     *
     * @throws DuplicateTaskException
     *             if updating the rask's details causes the task to be
     *             equivalent to another existing person in the list.
     * @throws TaskNotFoundException
     *             if {@code target} could not be found in the list.
     */
    public void updateTask(ReadOnlyTask target, ReadOnlyTask editedTask)
            throws DuplicateTaskException, TaskNotFoundException {
        requireNonNull(editedTask);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new TaskNotFoundException();
        }

        Task taskToUpdate = internalList.get(index);
        if (!taskToUpdate.equals(editedTask) && internalList.contains(editedTask)) {
            throw new DuplicateTaskException();
        }
        //For the scenario where user wants to update floating task to a task with deadline
        if (!(taskToUpdate instanceof TaskWithDeadline) && editedTask instanceof TaskWithDeadline) {
            taskToUpdate = new TaskWithDeadline(editedTask);
        } else {
            taskToUpdate.resetData(editedTask);
        }
        internalList.set(index, taskToUpdate);
    }

    /**
     * Removes the equivalent task from the list.
     *
     * @throws TaskNotFoundException
     *             if no such task could be found in the list.
     */
    public boolean remove(ReadOnlyTask toRemove) throws TaskNotFoundException {
        requireNonNull(toRemove);
        final boolean taskFoundAndDeleted = internalList.remove(toRemove);
        if (!taskFoundAndDeleted) {
            throw new TaskNotFoundException();
        }
        return taskFoundAndDeleted;
    }

    /**
     * Marks the equivalent task from the list as complete.
     *
     * @throws TaskNotFoundException
     *             if no such task could be found in the list.
     */

    public boolean mark(ReadOnlyTask toMark) throws TaskNotFoundException {
        requireNonNull(toMark);
        boolean taskFoundAndMarked = false;
        int index = internalList.indexOf(toMark);
        if (index == -1) {
            throw new TaskNotFoundException();
        } else {
            Task taskToUpdate = internalList.get(index);
            taskToUpdate.markCompleted();
            internalList.set(index, taskToUpdate);
            taskFoundAndMarked = true;
        }
        return taskFoundAndMarked;
    }

    /**
     * Marks the equivalent task from the list as incomplete.
     *
     * @throws TaskNotFoundException
     *             if no such task could be found in the list.
     */

    public boolean unmark(ReadOnlyTask toUnmark) throws TaskNotFoundException {
        requireNonNull(toUnmark);
        boolean taskFoundAndUnmarked = false;
        int index = internalList.indexOf(toUnmark);
        if (index == -1) {
            throw new TaskNotFoundException();
        } else {
            Task taskToUpdate = internalList.get(index);
            taskToUpdate.markIncompleted();
            internalList.set(index, taskToUpdate);
            taskFoundAndUnmarked = true;
        }
        return taskFoundAndUnmarked;
    }

    public void setTasks(UniqueTaskList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setTasks(List<? extends ReadOnlyTask> tasks) throws DuplicateTaskException {
        final UniqueTaskList replacement = new UniqueTaskList();
        for (final ReadOnlyTask task : tasks) {
            if (task instanceof TaskWithDeadline) {
                replacement.add(new TaskWithDeadline(task));
            } else {
                replacement.add(new Task(task));
            }
        }
        setTasks(replacement);
    }

    public UnmodifiableObservableList<Task> asObservableList() {
        return new UnmodifiableObservableList<>(internalList);
    }

    @Override
    public Iterator<Task> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueTaskList // instanceof handles nulls
                        && this.internalList.equals(((UniqueTaskList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
