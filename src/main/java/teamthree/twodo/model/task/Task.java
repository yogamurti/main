package teamthree.twodo.model.task;

import static java.util.Objects.requireNonNull;
import static teamthree.twodo.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import teamthree.twodo.commons.exceptions.IllegalValueException;
import teamthree.twodo.model.tag.Tag;
import teamthree.twodo.model.tag.UniqueTagList;

/**
 * Represents a Task in the address book.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Task implements ReadOnlyTask {

    private Name name;
    private Deadline deadline;
    private Email email;
    private Address address;

    private UniqueTagList tags;

    /**
     * Every field must be present and not null.
     */
    public Task(Name name, Deadline deadline, Email email, Address address, Set<Tag> tags) {
        requireAllNonNull(name, deadline, email, address, tags);
        this.name = name;
        this.deadline = deadline;
        this.email = email;
        this.address = address;
        this.tags = new UniqueTagList(tags); // protect internal tags from changes in the arg list
    }
    /**
     * Constructor for floating Task
     * @param name
     */
    public Task(Name name) {
        this.name = name;
        try {
            this.deadline = new Deadline("");
            this.email = new Email("");
            this.address = new Address("");
            this.tags = new UniqueTagList();
        } catch (IllegalValueException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a copy of the given ReadOnlyTask.
     */
    public Task(ReadOnlyTask source) {
        this(source.getName(), source.getPhone(), source.getEmail(), source.getAddress(),
                source.getTags());
    }

    public void setName(Name name) {
        this.name = requireNonNull(name);
    }

    @Override
    public Name getName() {
        return name;
    }

    public void setPhone(Deadline deadline) {
        this.deadline = requireNonNull(deadline);
    }

    @Override
    public Deadline getPhone() {
        return deadline;
    }

    public void setEmail(Email email) {
        this.email = requireNonNull(email);
    }

    @Override
    public Email getEmail() {
        return email;
    }

    public void setAddress(Address address) {
        this.address = requireNonNull(address);
    }

    @Override
    public Address getAddress() {
        return address;
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
        this.setPhone(replacement.getPhone());
        this.setEmail(replacement.getEmail());
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
        return Objects.hash(name, deadline, email, address, tags);
    }

    @Override
    public String toString() {
        return getAsText();
    }

}
