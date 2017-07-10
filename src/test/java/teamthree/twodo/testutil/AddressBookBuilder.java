package teamthree.twodo.testutil;

import teamthree.twodo.commons.exceptions.IllegalValueException;
import teamthree.twodo.model.TaskBook;
import teamthree.twodo.model.tag.Tag;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.model.task.exceptions.DuplicateTaskException;

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
        taskBook.addTask(task);
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
