package teamthree.twodo.testutil;

import java.util.Set;

import teamthree.twodo.commons.exceptions.IllegalValueException;
import teamthree.twodo.model.tag.Tag;
import teamthree.twodo.model.task.Deadline;
import teamthree.twodo.model.task.Description;
import teamthree.twodo.model.task.Name;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.model.task.TaskWithDeadline;
import teamthree.twodo.model.util.SampleDataUtil;

/**
 * A utility class to help with building Task objects.
 */
public class TaskWithDeadlineBuilder {

    public static final String DEFAULT_NAME = "eat,sleep,rest,repeat";
    public static final String DEFAULT_DEADLINE = "thu 10pm";
    public static final String DEFAULT_DESCRIPTION = "zen";
    public static final String DEFAULT_TAGS = "life";

    private TaskWithDeadline task;

    public TaskWithDeadlineBuilder() throws IllegalValueException {
        Name defaultName = new Name(DEFAULT_NAME);
        Deadline defaultDeadline = new Deadline(DEFAULT_DEADLINE, DEFAULT_DEADLINE, Deadline.NULL_VALUE);
        Description defaultAddress = new Description(DEFAULT_DESCRIPTION);
        Set<Tag> defaultTags = SampleDataUtil.getTagSet(DEFAULT_TAGS);
        this.task = new TaskWithDeadline(defaultName, defaultDeadline, defaultAddress, defaultTags);
    }

    /**
     * Initializes the TaskWithDeadlineBuilder with the data of
     * {@code personToCopy}.
     */
    public TaskWithDeadlineBuilder(ReadOnlyTask taskToCopy) {
        if (taskToCopy instanceof TaskWithDeadline) {
            this.task = new TaskWithDeadline(taskToCopy);
        }
    }

    public TaskWithDeadlineBuilder withName(String name) throws IllegalValueException {
        this.task.setName(new Name(name));
        return this;
    }

    public TaskWithDeadlineBuilder withTags(String... tags) throws IllegalValueException {
        this.task.setTags(SampleDataUtil.getTagSet(tags));
        return this;
    }

    public TaskWithDeadlineBuilder withDescription(String description) throws IllegalValueException {
        this.task.setDescription(new Description(description));
        return this;
    }

    public TaskWithDeadlineBuilder withDeadline(String startTime) throws IllegalValueException {
        this.task.setDeadline(new Deadline(startTime, startTime, Deadline.NULL_VALUE));
        return this;
    }

    public TaskWithDeadlineBuilder withEventDeadline(String startTime, String endTime) throws IllegalValueException {
        this.task.setDeadline(new Deadline(startTime, endTime, Deadline.NULL_VALUE));
        return this;
    }

    public Task build() {
        return this.task;
    }

}
