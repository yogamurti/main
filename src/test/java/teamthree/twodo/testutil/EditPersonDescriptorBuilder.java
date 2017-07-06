package teamthree.twodo.testutil;

import java.util.Arrays;
import java.util.Optional;

import teamthree.twodo.commons.exceptions.IllegalValueException;
import teamthree.twodo.logic.commands.EditCommand.EditTaskDescriptor;
import teamthree.twodo.logic.parser.ParserUtil;
import teamthree.twodo.model.task.ReadOnlyTask;

/**
 * A utility class to help with building EditTaskDescriptor objects.
 */
public class EditPersonDescriptorBuilder {

    private EditTaskDescriptor descriptor;

    public EditPersonDescriptorBuilder() {
        descriptor = new EditTaskDescriptor();
    }

    public EditPersonDescriptorBuilder(EditTaskDescriptor descriptor) {
        this.descriptor = new EditTaskDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditTaskDescriptor} with fields containing {@code person}'s details
     */
    public EditPersonDescriptorBuilder(ReadOnlyTask person) throws IllegalValueException {
        descriptor = new EditTaskDescriptor();
        descriptor.setName(person.getName());
        descriptor.setDeadline(person.getDeadline());
        descriptor.setEmail(person.getEmail());
        descriptor.setDescription(person.getDescription());
        descriptor.setTags(person.getTags());
    }

    public EditPersonDescriptorBuilder withName(String name) throws IllegalValueException {
        ParserUtil.parseName(Optional.of(name)).ifPresent(descriptor::setName);
        return this;
    }

    public EditPersonDescriptorBuilder withPhone(String phone) throws IllegalValueException {
        ParserUtil.parsePhone(Optional.of(phone)).ifPresent(descriptor::setDeadline);
        return this;
    }

    public EditPersonDescriptorBuilder withEmail(String email) throws IllegalValueException {
        ParserUtil.parseEmail(Optional.of(email)).ifPresent(descriptor::setEmail);
        return this;
    }

    public EditPersonDescriptorBuilder withAddress(String address) throws IllegalValueException {
        ParserUtil.parseDescription(Optional.of(address)).ifPresent(descriptor::setDescription);
        return this;
    }

    public EditPersonDescriptorBuilder withTags(String... tags) throws IllegalValueException {
        descriptor.setTags(ParserUtil.parseTags(Arrays.asList(tags)));
        return this;
    }

    public EditTaskDescriptor build() {
        return descriptor;
    }
}
