package teamthree.twodo.testutil;

import teamthree.twodo.commons.exceptions.IllegalValueException;
import teamthree.twodo.model.TaskList;
import teamthree.twodo.model.tag.Tag;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.model.task.exceptions.DuplicateTaskException;

/**
 * A utility class to help with building Taskbook objects.
 * Example usage: <br>
 *     {@code TaskList tb = new TaskBookBuilder().withPerson("John", "Doe").withTag("Friend").build();}
 */
public class TaskBookBuilder {

    private TaskList taskList;

    public TaskBookBuilder() {
        taskList = new TaskList();
    }

    public TaskBookBuilder(TaskList taskList) {
        this.taskList = taskList;
    }

    public TaskBookBuilder withTask(Task task) throws DuplicateTaskException {
        taskList.addTask(task);
        return this;
    }

    public TaskBookBuilder withTag(String tagName) throws IllegalValueException {
        taskList.addTag(new Tag(tagName));
        return this;
    }

    public TaskList build() {
        return taskList;
    }
}
