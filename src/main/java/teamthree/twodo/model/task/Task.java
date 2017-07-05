package teamthree.twodo.model.task;

import static java.util.Objects.requireNonNull;
import static teamthree.twodo.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import teamthree.twodo.commons.exceptions.IllegalValueException;
import teamthree.twodo.model.tag.Tag;
import teamthree.twodo.model.tag.UniqueTagList;

/**
 * Represents a Task in the note book.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Task implements ReadOnlyTask {

    private Name name;
    private Note note;
    private UniqueTagList tags;

    /**
     * Every field must be present and not null.
     */
    public Task(Name name, Note note, Set<Tag> tags) {
        requireAllNonNull(name, note, tags);
        this.name = name;
        this.note = note;
        this.tags = new UniqueTagList(tags); // protect internal tags from changes in the arg list
    }
    /**
     * Constructor for floating Task
     * @param name
     */
    public Task(Name name) {
        this.name = name;
        try {
            this.note = new Note("");
            this.tags = new UniqueTagList();
        } catch (IllegalValueException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a copy of the given ReadOnlyTask.
     */
    public Task(ReadOnlyTask source) {
        this(source.getName(), source.getAddress(), source.getTags());
    }

    public void setName(Name name) {
        this.name = requireNonNull(name);
    }

    @Override
    public Name getName() {
        return name;
    }

    public void setAddress(Note note) {
        this.note = requireNonNull(note);
    }

    @Override
    public Note getAddress() {
        return note;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    @Override
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags.toSet());
    }

    /**
     * Replaces this person's tags with the tags in the argument tag set.
     */
    public void setTags(Set<Tag> replacement) {
        tags.setTags(new UniqueTagList(replacement));
    }

    /**
     * Updates this person with the details of {@code replacement}.
     */
    public void resetData(ReadOnlyTask replacement) {
        requireNonNull(replacement);

        this.setName(replacement.getName());
        this.setAddress(replacement.getAddress());
        this.setTags(replacement.getTags());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReadOnlyTask // instanceof handles nulls
                && this.isSameStateAs((ReadOnlyTask) other));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, note, tags);
    }

    @Override
    public String toString() {
        return getAsText();
    }
    @Override
    public Optional<Deadline> getDeadline() {
        return Optional.empty();
    }

}
