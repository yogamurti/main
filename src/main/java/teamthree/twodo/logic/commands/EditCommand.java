package teamthree.twodo.logic.commands;

import static java.util.Objects.requireNonNull;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_DEADLINE_END;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_DEADLINE_START;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_NAME;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_NOTE;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import teamthree.twodo.commons.core.Messages;
import teamthree.twodo.commons.core.index.Index;
import teamthree.twodo.commons.util.CollectionUtil;
import teamthree.twodo.logic.commands.exceptions.CommandException;
import teamthree.twodo.model.tag.Tag;
import teamthree.twodo.model.task.Deadline;
import teamthree.twodo.model.task.Email;
import teamthree.twodo.model.task.Name;
import teamthree.twodo.model.task.Note;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.model.task.TaskWithDeadline;
import teamthree.twodo.model.task.exceptions.DuplicateTaskException;
import teamthree.twodo.model.task.exceptions.TaskNotFoundException;

/**
 * Edits the details of an existing person in the note book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the index number used in the last person listing. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) " + "[" + PREFIX_NAME + "NAME] " + "["
            + PREFIX_DEADLINE_START + "PHONE] " + "[" + PREFIX_DEADLINE_END + "EMAIL] " + "[" + PREFIX_NOTE
            + "ADDRESS] " + "[" + PREFIX_TAG + "TAG]...\n" + "Example: " + COMMAND_WORD + " 1 " + PREFIX_DEADLINE_START
            + "91234567 ";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Task: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the note book.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index
     *            of the person in the filtered person list to edit
     * @param editPersonDescriptor
     *            details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute() throws CommandException {
        List<ReadOnlyTask> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        ReadOnlyTask personToEdit = lastShownList.get(index.getZeroBased());
        Task editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        try {
            model.updateTask(personToEdit, editedPerson);
        } catch (DuplicateTaskException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        } catch (TaskNotFoundException pnfe) {
            throw new AssertionError("The target person cannot be missing");
        }
        model.updateFilteredListToShowAll();
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, personToEdit));
    }

    /**
     * Creates and returns a {@code Task} with the details of
     * {@code personToEdit} edited with {@code editPersonDescriptor}.
     *
     * If edit adds a deadline to floating task, a TaskWithDeadline object is returned.
     */
    private static Task createEditedPerson(ReadOnlyTask personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;
        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Note updatedAddress = editPersonDescriptor.getAddress().orElse(personToEdit.getAddress());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());
        if (!editPersonDescriptor.getDeadline().isPresent() && !(personToEdit instanceof TaskWithDeadline)) {
            return new Task(updatedName, updatedAddress, updatedTags);
        } else {
            Deadline updatedDeadline = editPersonDescriptor.getDeadline().orElse(personToEdit.getDeadline().get());
            return new TaskWithDeadline(updatedName, updatedDeadline, updatedAddress, updatedTags);
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return index.equals(e.index) && editPersonDescriptor.equals(e.editPersonDescriptor);
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value
     * will replace the corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Deadline deadline;
        private Email email;
        private Note note;
        private Set<Tag> tags;

        public EditPersonDescriptor() {
        }

        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            this.name = toCopy.name;
            this.deadline = toCopy.deadline;
            this.email = toCopy.email;
            this.note = toCopy.note;
            this.tags = toCopy.tags;
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(this.name, this.deadline, this.email, this.note, this.tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Deadline deadline) {
            this.deadline = deadline;
        }

        public Optional<Deadline> getDeadline() {
            return Optional.ofNullable(deadline);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Note note) {
            this.note = note;
        }

        public Optional<Note> getAddress() {
            return Optional.ofNullable(note);
        }

        public void setTags(Set<Tag> tags) {
            this.tags = tags;
        }

        public Optional<Set<Tag>> getTags() {
            return Optional.ofNullable(tags);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            // state check
            EditPersonDescriptor e = (EditPersonDescriptor) other;

            return getName().equals(e.getName()) && getDeadline().equals(e.getDeadline())
                    && getEmail().equals(e.getEmail()) && getAddress().equals(e.getAddress())
                    && getTags().equals(e.getTags());
        }
    }
}
