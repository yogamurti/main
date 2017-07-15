package teamthree.twodo.testutil;

import teamthree.twodo.commons.exceptions.IllegalValueException;
import teamthree.twodo.model.task.Deadline;

public class TypicalDeadline {

    private Deadline deadline;

    public TypicalDeadline () {
        try {
            deadline = new Deadline("yesterday 10am", "yesterday 10am",
                    Deadline.NULL_VALUE);
        } catch (IllegalValueException e) {
            deadline = null;
            e.printStackTrace();
        }
    }

    public Deadline getDeadline() {
        return deadline;
    }
}
