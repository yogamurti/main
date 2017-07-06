package teamthree.twodo.model;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javafx.collections.ObservableList;
import teamthree.twodo.commons.core.UnmodifiableObservableList;
import teamthree.twodo.model.tag.Tag;
import teamthree.twodo.model.tag.UniqueTagList;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.model.task.TaskWithDeadline;
import teamthree.twodo.model.task.UniqueTaskList;
import teamthree.twodo.model.task.exceptions.DuplicateTaskException;
import teamthree.twodo.model.task.exceptions.TaskNotFoundException;

/**
 * Wraps all data at the address-book level Duplicates are not allowed (by
 * .equals comparison)
 */
public class TaskBook implements ReadOnlyTaskBook {

    private final UniqueTaskList persons;
    private final UniqueTagList tags;

    /*
     * The 'unusual' code block below is an non-static initialization block,
     * sometimes used to avoid duplication between constructors. See
     * https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Description that non-static init blocks are not recommended to use. There
     * are other ways to avoid duplication among constructors.
     */
    {
        persons = new UniqueTaskList();
        tags = new UniqueTagList();
    }

    public TaskBook() {
    }

    /**
     * Creates an TaskBook using the Persons and Tags in the {@code toBeCopied}
     */
    public TaskBook(ReadOnlyTaskBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    public void setPersons(List<? extends ReadOnlyTask> persons) throws DuplicateTaskException {
        this.persons.setPersons(persons);
    }

    public void setTags(Collection<Tag> tags) throws UniqueTagList.DuplicateTagException {
        this.tags.setTags(tags);
    }

    public void resetData(ReadOnlyTaskBook newData) {
        requireNonNull(newData);
        try {
            setPersons(newData.getTaskList());
        } catch (DuplicateTaskException e) {
            assert false : "AddressBooks should not have duplicate persons";
        }
        try {
            setTags(newData.getTagList());
        } catch (UniqueTagList.DuplicateTagException e) {
            assert false : "AddressBooks should not have duplicate tags";
        }
        syncMasterTagListWith(persons);
    }

    //// person-level operations

    /**
     * Adds a person to the address book. Also checks the new person's tags and
     * updates {@link #tags} with any new tags found, and updates the Tag
     * objects in the person to point to those in {@link #tags}.
     *
     * @throws DuplicateTaskException
     *             if an equivalent person already exists.
     */
    public void addPerson(ReadOnlyTask p) throws DuplicateTaskException {
        Task newPerson;
        if (p instanceof TaskWithDeadline) {
            newPerson = new TaskWithDeadline(p);
        } else {
            newPerson = new Task(p);
        }
        syncMasterTagListWith(newPerson);
        persons.add(newPerson);
    }

    /**
     * Replaces the given person {@code target} in the list with
     * {@code editedReadOnlyPerson}. {@code TaskBook}'s tag list will be updated
     * with the tags of {@code editedReadOnlyPerson}.
     *
     * @throws DuplicateTaskException
     *             if updating the person's details causes the person to be
     *             equivalent to another existing person in the list.
     * @throws TaskNotFoundException
     *             if {@code target} could not be found in the list.
     *
     * @see #syncMasterTagListWith(Task)
     */
    public void updatePerson(ReadOnlyTask target, ReadOnlyTask editedReadOnlyPerson)
            throws DuplicateTaskException, TaskNotFoundException {
        requireNonNull(editedReadOnlyPerson);

        Task editedPerson;
        if (editedReadOnlyPerson instanceof TaskWithDeadline) {
            editedPerson = new TaskWithDeadline(editedReadOnlyPerson);
        } else {
            editedPerson = new Task(editedReadOnlyPerson);
        }
        syncMasterTagListWith(editedPerson);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any person
        // in the person list.
        persons.updatePerson(target, editedPerson);
    }

    /**
     * Ensures that every tag in this person: - exists in the master list
     * {@link #tags} - points to a Tag object in the master list
     */
    private void syncMasterTagListWith(Task task) {
        final UniqueTagList personTags = new UniqueTagList(task.getTags());
        tags.mergeFrom(personTags);

        // Create map with values = tag object references in the master list
        // used for checking person tag references
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        tags.forEach(tag -> masterTagObjects.put(tag, tag));

        // Rebuild the list of person tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        personTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));
        task.setTags(correctTagReferences);
    }

    /**
     * Ensures that every tag in these persons: - exists in the master list
     * {@link #tags} - points to a Tag object in the master list
     *
     * @see #syncMasterTagListWith(Task)
     */
    private void syncMasterTagListWith(UniqueTaskList persons) {
        persons.forEach(this::syncMasterTagListWith);
    }

    public boolean removePerson(ReadOnlyTask key) throws TaskNotFoundException {
        if (persons.remove(key)) {
            return true;
        } else {
            throw new TaskNotFoundException();
        }
    }

    //// tag-level operations

    public void addTag(Tag t) throws UniqueTagList.DuplicateTagException {
        tags.add(t);
    }

    //// util methods

    @Override
    public String toString() {
        return persons.asObservableList().size() + " persons, " + tags.asObservableList().size() + " tags";
        // TODO: refine later
    }

    @Override
    public ObservableList<ReadOnlyTask> getTaskList() {
        return new UnmodifiableObservableList<>(persons.asObservableList());
    }

    @Override
    public ObservableList<Tag> getTagList() {
        return new UnmodifiableObservableList<>(tags.asObservableList());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskBook // instanceof handles nulls
                        && this.persons.equals(((TaskBook) other).persons)
                        && this.tags.equalsOrderInsensitive(((TaskBook) other).tags));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(persons, tags);
    }
}
