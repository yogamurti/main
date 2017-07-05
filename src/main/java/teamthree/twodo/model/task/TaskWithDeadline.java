package teamthree.twodo.model.task;

import java.util.Set;

import teamthree.twodo.model.tag.Tag;

public class TaskWithDeadline extends Task implements ReadOnlyTask {

    private Deadline deadline;
    public TaskWithDeadline(Name name, Deadline deadline, Note note, Set<Tag> tags) {
        super(name, note, tags);
        this.deadline = deadline;
    }

    public TaskWithDeadline(Name name) {
        super(name);
        // TODO Auto-generated constructor stub
    }

    public TaskWithDeadline(ReadOnlyTask source) {
        super(source);
        // TODO Auto-generated constructor stub
    }

}
