package teamthree.twodo.testutil;

import java.util.Arrays;
import java.util.Optional;

import teamthree.twodo.commons.exceptions.IllegalValueException;
import teamthree.twodo.logic.commands.EditCommand.EditPersonDescriptor;
import teamthree.twodo.logic.parser.ParserUtil;
import teamthree.twodo.model.task.ReadOnlyTask;

/**
 * A utility class to help with building EditPersonDescriptor objects.
 */
public class EditPersonDescriptorBuilder {

    private EditPersonDescriptor descriptor;

    public EditPersonDescriptorBuilder() {
        descriptor = new EditPersonDescriptor();
    }

    public EditPersonDescriptorBuilder(EditPersonDescriptor descriptor) {
        this.descriptor = new EditPersonDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditPersonDescriptor} with fields containing {@code person}'s details
     */
    public EditPersonDescriptorBuilder(ReadOnlyTask person) throws IllegalValueException {
        descriptor = new EditPersonDescriptor();
        descriptor.setName(person.getName());
        descriptor.setPhone(person.getPhone());
        descriptor.setEmail(person.getEmail());
        descriptor.setAddress(person.getAddress());
        descriptor.setTags(person.getTags());
    }

    public EditPersonDescriptorBuilder withName(String name) throws IllegalValueException {
        ParserUtil.parseName(Optional.of(name)).ifPresent(descriptor::setName);
        return this;
    }

    public EditPersonDescriptorBuilder withPhone(String phone) throws IllegalValueException {
        ParserUtil.parsePhone(Optional.of(phone)).ifPresent(descriptor::setPhone);
        return this;
    }

    public EditPersonDescriptorBuilder withEmail(String email) throws IllegalValueException {
        ParserUtil.parseEmail(Optional.of(email)).ifPresent(descriptor::setEmail);
        return this;
    }

    public EditPersonDescriptorBuilder withAddress(String address) throws IllegalValueException {
        ParserUtil.parseAddress(Optional.of(address)).ifPresent(descriptor::setAddress);
        return this;
    }

    public EditPersonDescriptorBuilder withTags(String... tags) throws IllegalValueException {
        descriptor.setTags(ParserUtil.parseTags(Arrays.asList(tags)));
        return this;
    }

    public EditPersonDescriptor build() {
        return descriptor;
    }
}
