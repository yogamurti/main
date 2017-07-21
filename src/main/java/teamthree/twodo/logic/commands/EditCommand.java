package teamthree.twodo.logic.commands;

import static java.util.Objects.requireNonNull;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_DEADLINE_END;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_DEADLINE_START;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_NAME;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import teamthree.twodo.commons.core.EventsCenter;
import teamthree.twodo.commons.core.Messages;
import teamthree.twodo.commons.core.index.Index;
import teamthree.twodo.commons.events.model.AddOrEditCommandExecutedEvent;
import teamthree.twodo.commons.util.CollectionUtil;
import teamthree.twodo.logic.commands.exceptions.CommandException;
import teamthree.twodo.model.tag.Tag;
import teamthree.twodo.model.task.Deadline;
import teamthree.twodo.model.task.Description;
import teamthree.twodo.model.task.Name;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.model.task.TaskWithDeadline;
import teamthree.twodo.model.task.exceptions.DuplicateTaskException;
import teamthree.twodo.model.task.exceptions.TaskNotFoundException;

//@@author A0124399W
// Edits the details of an existing task in the Tasklist.
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";
    public static final String COMMAND_WORD_FAST = "e";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the task identified "
            + "by the index number used in the last task listing. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) " + "[" + PREFIX_NAME + "NAME] " + "["
            + PREFIX_DEADLINE_START + "START DATE&TIME] " + "[" + PREFIX_DEADLINE_END + "END DATE&TIME] " + "["
            + PREFIX_DESCRIPTION + "NOTES] " + "[" + PREFIX_TAG + "TAG]...\n" + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_DEADLINE_START + "fri 3pm";

    public static final String MESSAGE_EDIT_TASK_SUCCESS = "Edited Task: %1$s\n";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in your 2Do list";

    private final Index index;
    private final EditTaskDescriptor editTaskDescriptor;

    /**
     * @param index
     *            of the person in the filtered person list to edit
     * @param editTaskDescriptor
     *            details to edit the person with
     */
    public EditCommand(Index index, EditTaskDescriptor editTaskDescriptor) {
        requireNonNull(index);
        requireNonNull(editTaskDescriptor);

        this.index = index;
        this.editTaskDescriptor = new EditTaskDescriptor(editTaskDescriptor);
    }

    @Override
    public CommandResult execute() throws CommandException {
        List<ReadOnlyTask> lastShownList = model.getFilteredAndSortedTaskList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        ReadOnlyTask taskToEdit = lastShownList.get(index.getZeroBased());
        Task editedTask = createEditedTask(taskToEdit, editTaskDescriptor);

        try {
            model.updateTask(taskToEdit, editedTask);
            history.addToBeforeEditHistory(taskToEdit);
            history.addToAfterEditHistory(editedTask);

        } catch (DuplicateTaskException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_TASK);
        } catch (TaskNotFoundException pnfe) {
            throw new AssertionError("The target task cannot be missing");
        }
        if (editedTask instanceof TaskWithDeadline) {
            model.updateFilteredListToShowAllIncomplete(null, false);
        } else {
            model.updateFilteredListToShowAllIncomplete(null, true);
        }
        EventsCenter.getInstance().post(new AddOrEditCommandExecutedEvent(editedTask));
        return new CommandResult(String.format(MESSAGE_EDIT_TASK_SUCCESS, editedTask));
    }

    /**
     * Creates and returns a {@code Task} with the details of {@code taskToEdit}
     * edited with {@code editTaskDescriptor}.
     *
     * If edit adds a deadline to floating task, a TaskWithDeadline object is
     * returned.
     */
    private static Task createEditedTask(ReadOnlyTask taskToEdit, EditTaskDescriptor editTaskDescriptor) {
        assert taskToEdit != null;
        Name updatedName = editTaskDescriptor.getName().orElse(taskToEdit.getName());
        Description updatedDescription = editTaskDescriptor.getDescription().orElse(taskToEdit.getDescription());
        Set<Tag> updatedTags = editTaskDescriptor.getTags().orElse(taskToEdit.getTags());

        if (editTaskDescriptor.getDeadline().isPresent() || taskToEdit instanceof TaskWithDeadline) {
            Deadline updatedDeadline = getUpdatedDeadline(taskToEdit, editTaskDescriptor);
            return new TaskWithDeadline(updatedName, updatedDeadline, updatedDescription, updatedTags);
        }
        return new Task(updatedName, updatedDescription, updatedTags);
    }

    /**
     * Returns the final deadline with all the updates integrated.
     *
     * @param taskToEdit
     *            the original task
     * @param editTaskDescriptor
     *            the taskdescriptor with updates
     * @return final deadline with all updates
     */
    private static Deadline getUpdatedDeadline(ReadOnlyTask taskToEdit, EditTaskDescriptor editTaskDescriptor) {
        boolean isDeadlineUnchanged = !editTaskDescriptor.getDeadline().isPresent()
                && taskToEdit instanceof TaskWithDeadline;
        if (isDeadlineUnchanged) {
            return taskToEdit.getDeadline().get();
        }
        Deadline updates = editTaskDescriptor.getDeadline().get();
        if (!(taskToEdit instanceof TaskWithDeadline)) {
            //if original task had no deadline, the new deadline will be fully from task descriptor
            return correctStartEndDiscrepancy(updates, updates);
        }
        Deadline old = taskToEdit.getDeadline().get();

        Date start = isDefaultDate(updates.getStartDate()) ? old.getStartDate() : updates.getStartDate();
        Date end = isDefaultDate(updates.getEndDate()) ? old.getEndDate() : updates.getEndDate();

        Long notification = updateNotificationPeriod(old, updates);
        return correctStartEndDiscrepancy(new Deadline(start, end, notification), updates);
    }

    /**
     * Checks if the notification period is being updated and returns the
     * updated version if true
     *
     */
    private static Long updateNotificationPeriod(Deadline old, Deadline updates) {
        Long notification = updates.getNotificationPeriod().equals(old.getNotificationPeriod())
                ? old.getNotificationPeriod() : updates.getNotificationPeriod();
        return notification;
    }

    /**
     * Checks and corrects for start and end date discrepancy (i.e. start date
     * after end date). If a date is default, it means that it is not being
     * updated
     *
     * @param updatedDate
     *            the final deadline with all updates integrated
     * @param updates
     *            the updates in this edit
     * @return final deadline with all start and end date discrepancies cleared
     */

    private static Deadline correctStartEndDiscrepancy(Deadline updatedDate, Deadline updates) {
        Date start = updatedDate.getStartDate();
        Date end = updatedDate.getEndDate();
        if ((start.after(end) || isDefaultDate(end)) && isDefaultDate(updates.getEndDate())) {
            end = start;
        } else if ((end.before(start) || isDefaultDate(start)) && isDefaultDate(updates.getStartDate())) {
            start = end;
        }
        return new Deadline(start, end, updatedDate.getNotificationPeriod());
    }

    /**
     * Returns true if the given date is the default date
     */
    private static boolean isDefaultDate(Date updates) {
        return updates.equals(Deadline.DEFAULT_DATE);
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
        return index.equals(e.index) && editTaskDescriptor.equals(e.editTaskDescriptor);
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value
     * will replace the corresponding field value of the person.
     */
    public static class EditTaskDescriptor {
        private Name name;
        private Deadline deadline;
        private Description description;
        private Set<Tag> tags;

        public EditTaskDescriptor() {
        }

        public EditTaskDescriptor(EditTaskDescriptor toCopy) {
            this.name = toCopy.name;
            this.deadline = toCopy.deadline;
            this.description = toCopy.description;
            this.tags = toCopy.tags;
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(this.name, this.deadline, this.description, this.tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setDeadline(Deadline deadline) {
            this.deadline = deadline;
        }

        public Optional<Deadline> getDeadline() {
            return Optional.ofNullable(deadline);
        }

        public void setDescription(Description description) {
            this.description = description;
        }

        public Optional<Description> getDescription() {
            return Optional.ofNullable(description);
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
            if (!(other instanceof EditTaskDescriptor)) {
                return false;
            }

            // state check
            EditTaskDescriptor e = (EditTaskDescriptor) other;

            return getName().equals(e.getName()) && getDeadline().equals(e.getDeadline())
                    && getDescription().equals(e.getDescription()) && getTags().equals(e.getTags());
        }
    }
}
