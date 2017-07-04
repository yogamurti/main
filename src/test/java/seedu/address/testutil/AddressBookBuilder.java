package seedu.address.testutil;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.TaskBook;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Task;
import seedu.address.model.task.exceptions.DuplicateTaskException;

/**
 * A utility class to help with building Addressbook objects.
 * Example usage: <br>
 *     {@code TaskBook ab = new AddressBookBuilder().withPerson("John", "Doe").withTag("Friend").build();}
 */
public class AddressBookBuilder {

    private TaskBook taskBook;

    public AddressBookBuilder() {
        taskBook = new TaskBook();
    }

    public AddressBookBuilder(TaskBook taskBook) {
        this.taskBook = taskBook;
    }

    public AddressBookBuilder withPerson(Task task) throws DuplicateTaskException {
        taskBook.addPerson(task);
        return this;
    }

    public AddressBookBuilder withTag(String tagName) throws IllegalValueException {
        taskBook.addTag(new Tag(tagName));
        return this;
    }

    public TaskBook build() {
        return taskBook;
    }
}
