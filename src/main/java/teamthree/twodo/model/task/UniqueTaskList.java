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
 * A list of persons that enforces uniqueness between its elements and does not
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
     * Adds a person to the list.
     *
     * @throws DuplicateTaskException
     *             if the person to add is a duplicate of an existing person in
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
     * Replaces the person {@code target} in the list with {@code editedPerson}.
     *
     * @throws DuplicateTaskException
     *             if updating the person's details causes the person to be
     *             equivalent to another existing person in the list.
     * @throws TaskNotFoundException
     *             if {@code target} could not be found in the list.
     */
    public void updatePerson(ReadOnlyTask target, ReadOnlyTask editedPerson)
            throws DuplicateTaskException, TaskNotFoundException {
        requireNonNull(editedPerson);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new TaskNotFoundException();
        }

        Task personToUpdate = internalList.get(index);
        if (!personToUpdate.equals(editedPerson) && internalList.contains(editedPerson)) {
            throw new DuplicateTaskException();
        }
        //For the scenario where user wants to update floating task to a task with deadline
        if (!(personToUpdate instanceof TaskWithDeadline) && editedPerson instanceof TaskWithDeadline) {
            personToUpdate = new TaskWithDeadline(editedPerson);
        } else {
            personToUpdate.resetData(editedPerson);
        }
        // TODO: The code below is just a workaround to notify observers of the updated person.
        // The right way is to implement observable properties in the Task class.
        // Then, PersonCard should then bind its text labels to those observable properties.
        internalList.set(index, personToUpdate);
    }

    /**
     * Removes the equivalent person from the list.
     *
     * @throws TaskNotFoundException
     *             if no such person could be found in the list.
     */
    public boolean remove(ReadOnlyTask toRemove) throws TaskNotFoundException {
        requireNonNull(toRemove);
        final boolean personFoundAndDeleted = internalList.remove(toRemove);
        if (!personFoundAndDeleted) {
            throw new TaskNotFoundException();
        }
        return personFoundAndDeleted;
    }

    public void setPersons(UniqueTaskList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setPersons(List<? extends ReadOnlyTask> persons) throws DuplicateTaskException {
        final UniqueTaskList replacement = new UniqueTaskList();
        for (final ReadOnlyTask person : persons) {
            if (person instanceof TaskWithDeadline) {
                replacement.add(new TaskWithDeadline(person));
            } else {
                replacement.add(new Task(person));
            }
        }
        setPersons(replacement);
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
