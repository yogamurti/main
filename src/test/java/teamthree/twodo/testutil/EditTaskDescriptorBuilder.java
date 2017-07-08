package teamthree.twodo.testutil;

import java.util.Arrays;
import java.util.Optional;

import teamthree.twodo.commons.exceptions.IllegalValueException;
import teamthree.twodo.logic.commands.EditCommand.EditTaskDescriptor;
import teamthree.twodo.logic.parser.ParserUtil;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.TaskWithDeadline;

/**
 * A utility class to help with building EditTaskDescriptor objects.
 */
public class EditTaskDescriptorBuilder {

    private EditTaskDescriptor descriptor;

    public EditTaskDescriptorBuilder() {
        descriptor = new EditTaskDescriptor();
    }

    public EditTaskDescriptorBuilder(EditTaskDescriptor descriptor) {
        this.descriptor = new EditTaskDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditTaskDescriptor} with fields containing
     * {@code person}'s details
     */
    public EditTaskDescriptorBuilder(ReadOnlyTask person) throws IllegalValueException {
        descriptor = new EditTaskDescriptor();
        descriptor.setName(person.getName());
        descriptor.setDescription(person.getDescription());
        descriptor.setTags(person.getTags());
        if (person instanceof TaskWithDeadline) {
            descriptor.setDeadline(person.getDeadline().get());
        }
    }

    public EditTaskDescriptorBuilder withName(String name) throws IllegalValueException {
        ParserUtil.parseName(Optional.of(name)).ifPresent(descriptor::setName);
        return this;
    }

    public EditTaskDescriptorBuilder withStartDeadline(String start) throws IllegalValueException {
        ParserUtil.parseDeadlineForEdit(Optional.of(start), Optional.empty(), Optional.empty())
                .ifPresent(descriptor::setDeadline);
        return this;
    }

    public EditTaskDescriptorBuilder withStartAndEndDeadline(String start, String end) throws IllegalValueException {
        ParserUtil.parseDeadlineForEdit(Optional.of(start), Optional.of(end), Optional.empty())
                .ifPresent(descriptor::setDeadline);
        return this;
    }

    public EditTaskDescriptorBuilder withDescription(String description) throws IllegalValueException {
        ParserUtil.parseDescription(Optional.of(description)).ifPresent(descriptor::setDescription);
        return this;
    }

    public EditTaskDescriptorBuilder withTags(String... tags) throws IllegalValueException {
        descriptor.setTags(ParserUtil.parseTags(Arrays.asList(tags)));
        return this;
    }

    public EditTaskDescriptor build() {
        return descriptor;
    }
}
