package teamthree.twodo.testutil;

import java.util.Set;

import teamthree.twodo.commons.exceptions.IllegalValueException;
import teamthree.twodo.model.tag.Tag;
import teamthree.twodo.model.task.Description;
import teamthree.twodo.model.task.Deadline;
import teamthree.twodo.model.task.Email;
import teamthree.twodo.model.task.Name;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.model.util.SampleDataUtil;

/**
 * A utility class to help with building Task objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Alice Pauline";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "alice@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_TAGS = "friends";

    private Task task;

    public PersonBuilder() throws IllegalValueException {
        Name defaultName = new Name(DEFAULT_NAME);
        Deadline defaultPhone = new Deadline(DEFAULT_PHONE);
        Email defaultEmail = new Email(DEFAULT_EMAIL);
        Description defaultAddress = new Description(DEFAULT_ADDRESS);
        Set<Tag> defaultTags = SampleDataUtil.getTagSet(DEFAULT_TAGS);
        this.task = new Task(defaultName, defaultPhone, defaultEmail, defaultAddress, defaultTags);
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(ReadOnlyTask personToCopy) {
        this.task = new Task(personToCopy);
    }

    public PersonBuilder withName(String name) throws IllegalValueException {
        this.task.setName(new Name(name));
        return this;
    }

    public PersonBuilder withTags(String ... tags) throws IllegalValueException {
        this.task.setTags(SampleDataUtil.getTagSet(tags));
        return this;
    }

    public PersonBuilder withAddress(String address) throws IllegalValueException {
        this.task.setDescription(new Description(address));
        return this;
    }

    public PersonBuilder withPhone(String phone) throws IllegalValueException {
        this.task.setDeadline(new Deadline(phone));
        return this;
    }

    public PersonBuilder withEmail(String email) throws IllegalValueException {
        this.task.setEmail(new Email(email));
        return this;
    }

    public Task build() {
        return this.task;
    }

}
