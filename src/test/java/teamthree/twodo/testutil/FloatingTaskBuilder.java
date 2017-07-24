package teamthree.twodo.testutil;

import java.util.Set;

import teamthree.twodo.commons.exceptions.IllegalValueException;
import teamthree.twodo.model.tag.Tag;
import teamthree.twodo.model.task.Description;
import teamthree.twodo.model.task.Name;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.model.util.SampleDataUtil;

public class FloatingTaskBuilder {

    public static final String DEFAULT_NAME = "eat,sleep,rest,repeat";
    public static final String DEFAULT_DESCRIPTION = "zen";
    public static final String DEFAULT_TAGS = "life";

    private Task task;

    public FloatingTaskBuilder() throws IllegalValueException {
        Name defaultName = new Name(DEFAULT_NAME);
        Description defaultDescription = new Description(DEFAULT_DESCRIPTION);
        Set<Tag> defaultTags = SampleDataUtil.getTagSet(DEFAULT_TAGS);
        this.task = new Task(defaultName, defaultDescription, defaultTags, false);
    }

    /**
     * Initializes the FloatingTaskBuilder with the data of {@code personToCopy}
     * .
     */
    public FloatingTaskBuilder(ReadOnlyTask taskToCopy) {
        this.task = new Task(taskToCopy);
    }

    public FloatingTaskBuilder withName(String name) throws IllegalValueException {
        this.task.setName(new Name(name));
        return this;
    }

    public FloatingTaskBuilder withTags(String... tags) throws IllegalValueException {
        this.task.setTags(SampleDataUtil.getTagSet(tags));
        return this;
    }

    public FloatingTaskBuilder withDescription(String description) throws IllegalValueException {
        this.task.setDescription(new Description(description));
        return this;
    }

    public Task build() {
        return this.task;
    }

    public FloatingTaskBuilder isCompleted() {
        this.task.markCompleted();
        return this;
    }

}
