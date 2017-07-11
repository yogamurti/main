package teamthree.twodo.model.task;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.Set;

import teamthree.twodo.model.tag.Tag;

public class TaskWithDeadline extends Task implements ReadOnlyTask {

    private Deadline deadline;
    public TaskWithDeadline(Name name, Deadline deadline, Description description, Set<Tag> tags) {
        super(name, description, tags);
        this.deadline = deadline;
    }

    public TaskWithDeadline(Name name) {
        super(name);
        // TODO Auto-generated constructor stub
    }

    public TaskWithDeadline(ReadOnlyTask source) {
        this(source.getName(), source.getDeadline().get(), source.getDescription(), source.getTags());
    }

    public void setDeadline(Deadline deadline) {
        this.deadline = deadline;
    }

    @Override
    public Optional<Deadline> getDeadline() {
        return Optional.of(deadline);
    }
    /**
     * Formats the person as text, showing all contact details.
     */
    @Override
    public String getAsText() {
        assert(deadline != null);
        final StringBuilder builder = new StringBuilder();
        builder.append(getName() + "\n")
                .append(getDeadline().get())
                .append("Description: ")
                .append(getDescription())
                .append("Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }
    /**
     * Updates this person with the details of {@code replacement}.
     */
    @Override
    public void resetData(ReadOnlyTask replacement) {
        requireNonNull(replacement);

        this.setName(replacement.getName());
        this.setDeadline(replacement.getDeadline().get());
        this.setDescription(replacement.getDescription());
        this.setTags(replacement.getTags());
    }

}
