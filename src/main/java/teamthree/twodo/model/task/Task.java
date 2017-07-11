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
 * Represents a Task in the description book.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Task implements ReadOnlyTask {

    protected Name name;
    protected Description description;
    protected UniqueTagList tags;
    protected boolean completed;

    /**
     * Every field must be present and not null.
     * By default, the task is set as incompleted when it is created.
     */
    public Task(Name name, Description description, Set<Tag> tags) {
        requireAllNonNull(name, description, tags);
        this.name = name;
        this.description = description;
        this.tags = new UniqueTagList(tags); // protect internal tags from changes in the arg list
        completed = false;
    }
    /**
     * Constructor for floating Task
     * @param name
     */
    public Task(Name name) {
        this.name = name;
        try {
            this.description = new Description("");
            this.tags = new UniqueTagList();
            this.completed = false;
        } catch (IllegalValueException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a copy of the given ReadOnlyTask.
     */
    public Task(ReadOnlyTask source) {
        this(source.getName(), source.getDescription(), source.getTags());
    }

    public void setName(Name name) {
        this.name = requireNonNull(name);
    }

    @Override
    public Name getName() {
        return name;
    }

    public void setDescription(Description description) {
        this.description = requireNonNull(description);
    }

    @Override
    public Description getDescription() {
        return description;
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

    @Override
    public boolean isCompleted() {
        return completed;
    }

    // Marks the task as complete
    public void markCompleted() {
        completed = true;
    }

    // Marks the task as incomplete
    public void markIncompleted() {
        completed = false;
    }

    /**
     * Updates this person with the details of {@code replacement}.
     */
    public void resetData(ReadOnlyTask replacement) {
        requireNonNull(replacement);
        this.setName(replacement.getName());
        this.setDescription(replacement.getDescription());
        this.setTags(replacement.getTags());
        completed = replacement.isCompleted();
        if (completed) {
            this.markCompleted();
        } else {
            this.markIncompleted();
        }
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
        return Objects.hash(name, description, tags);
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
