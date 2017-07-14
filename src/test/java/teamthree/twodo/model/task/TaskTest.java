package teamthree.twodo.model.task;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import teamthree.twodo.commons.exceptions.IllegalValueException;
import teamthree.twodo.testutil.FloatingTaskBuilder;
import teamthree.twodo.testutil.TaskWithDeadlineBuilder;

public class TaskTest {

    @Test
    public void resetTaskSuccessfullyForFloating() {
        try {
            Task original = new FloatingTaskBuilder().build();
            Task replacement = new FloatingTaskBuilder().withName("Replacement").build();
            original.resetData(replacement);
            assertTrue(original.equals(replacement));
        } catch (IllegalValueException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void markCompletedSetsTaskAsComplete() {
        try {
            Task task = new FloatingTaskBuilder().build();
            task.markCompleted();
            assertTrue(task.isCompleted());
        } catch (IllegalValueException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void resetTaskSuccessfullyForDeadlinesAndCompletedTask() {
        try {
            Task original = new TaskWithDeadlineBuilder().build();
            Task replacement = new TaskWithDeadlineBuilder().withName("Replacement").build();
            replacement.markCompleted();
            original.resetData(replacement);
            assertTrue(original.equals(replacement));
        } catch (IllegalValueException e) {
            e.printStackTrace();
        }
    }
}
