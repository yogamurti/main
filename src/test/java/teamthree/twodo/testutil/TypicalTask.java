package teamthree.twodo.testutil;

import teamthree.twodo.commons.core.index.Index;
import teamthree.twodo.commons.exceptions.IllegalValueException;
import teamthree.twodo.model.TaskBook;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.model.task.TaskWithDeadline;
import teamthree.twodo.model.task.exceptions.DuplicateTaskException;

/**
 *
 */
public class TypicalTask {

    public static final Index INDEX_FIRST_TASK = Index.fromOneBased(1);
    public static final Index INDEX_SECOND_TASK = Index.fromOneBased(2);
    public static final Index INDEX_THIRD_TASK = Index.fromOneBased(3);

    public final Task cs2103, cs1020, cs2010, project, dinner, training, cca, supermarket, ida;

    public TypicalTask() {
        try {
            cs2103 = new TaskWithDeadlineBuilder().withName("CS2013")
                    .withDescription("Project V0.5").withDeadline("17 april 2018")
                    .withTags("school", "project").build();
            cs1020 = new TaskWithDeadlineBuilder().withName("CS1020")
                    .withDescription("labs").withTags("school").build();
            cs2010 = new TaskWithDeadlineBuilder().withName("cs2010").withDeadline("fri10am")
                    .withDescription("tutorial").build();
            project = new TaskWithDeadlineBuilder().withName("project").withDeadline("sat10pm")
                    .withDescription("project").build();
            dinner = new TaskWithDeadlineBuilder().withName("dinner").withDeadline("next week")
                    .withDescription("Thai Food").build();
            training = new TaskWithDeadlineBuilder().withName("training").withDeadline("next mon")
                    .withDescription("cca training").build();
            cca = new TaskWithDeadlineBuilder().withName("cca").withDeadline("july 2018")
                    .withDescription("end of term").build();

            // Manually added
            supermarket = new TaskWithDeadlineBuilder().withName("buy lotion").withDeadline("march 2020")
                    .withDescription("little india").build();
            ida = new TaskWithDeadlineBuilder().withName("Ida Mueller").withDeadline("tomorrow")
                    .withDescription("meet at chicago ave").build();
        } catch (IllegalValueException e) {
            throw new AssertionError("Sample data cannot be invalid", e);
        }
    }

    public static void loadTaskBookWithSampleData(TaskBook ab) {
        for (Task task : new TypicalTask().getTypicalTasks()) {
            try {
                if (task instanceof TaskWithDeadline) {
                    ab.addTask(new TaskWithDeadline(task));
                } else {
                    ab.addTask(new Task(task));
                }
            } catch (DuplicateTaskException e) {
                assert false : "not possible";
            }
        }
    }

    public Task[] getTypicalTasks() {
        return new Task[] { cs2103, cs1020, cs2010, project, dinner, training, cca };
    }

    public TaskBook getTypicalTaskBook() {
        TaskBook ab = new TaskBook();
        loadTaskBookWithSampleData(ab);
        return ab;
    }
}
