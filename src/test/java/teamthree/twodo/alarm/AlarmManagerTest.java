package teamthree.twodo.alarm;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.eventbus.Subscribe;

import teamthree.twodo.commons.core.EventsCenter;
import teamthree.twodo.commons.core.UnmodifiableObservableList;
import teamthree.twodo.commons.events.alarm.DeadlineNotificationTimeReachedEvent;
import teamthree.twodo.commons.exceptions.IllegalValueException;
import teamthree.twodo.logic.commands.ListCommand.AttributeInputted;
import teamthree.twodo.model.Model;
import teamthree.twodo.model.ReadOnlyTaskList;
import teamthree.twodo.model.TaskList;
import teamthree.twodo.model.tag.Tag;
import teamthree.twodo.model.task.Deadline;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.model.task.exceptions.DuplicateTaskException;
import teamthree.twodo.model.task.exceptions.TaskNotFoundException;
import teamthree.twodo.testutil.TaskWithDeadlineBuilder;
import teamthree.twodo.testutil.TestUtil;

public class AlarmManagerTest {
    /**
     * See https://github.com/junit-team/junit4/wiki/rules#temporaryfolder-rule
     */

    private Model model;
    private AlarmManager alarm;
    private boolean isEventCaught = false;

    @Subscribe
    public void handleDeadlineNotificationTimeReachedEvent(DeadlineNotificationTimeReachedEvent event) {
        isEventCaught = true;
    }

    @Before
    public void setUp() {
        model = new ModelManagerStub();
        EventsCenter.getInstance().registerHandler(this);

    }
    @After
    public void tearDown() {
        EventsCenter.clearSubscribers();
    }


    @Test
    public void raiseNotificationReminderSuccessfully() {
        // Should raise deadline reached event
        alarm = new AlarmManager(model);
        try {
            //Need to wait for event to be caught
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.toString();
        }
        assertTrue(isEventCaught);
    }

    private class ModelManagerStub implements Model {
        private TaskList tasklist;

        public ModelManagerStub() {
            try {
                setTaskList(new TaskList(generateSampleTaskList()));
            } catch (IllegalValueException e) {
                // should not reach here
                e.printStackTrace();
            }
        }

        private TaskList generateSampleTaskList() throws IllegalValueException {
            try {
                List<Task> samples = new ArrayList<Task>();
                samples.addAll(TestUtil.generateSampleTaskData());
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm");
                samples.add(new TaskWithDeadlineBuilder().withDeadline(dateFormat.format(new Date())).build());
                TaskList sampleTb = new TaskList();
                for (Task sampleTask : samples) {
                    sampleTb.addTask(sampleTask);
                }
                return sampleTb;
            } catch (DuplicateTaskException e) {
                throw new AssertionError("sample data cannot contain duplicate tasks", e);
            } catch (Exception e) {
                e.printStackTrace();
                throw new AssertionError("Got rekt", e);
            }
        }

        @Override
        public void resetData(ReadOnlyTaskList newData) {
            fail("This method should not be called.");
        }

        @Override
        public ReadOnlyTaskList getTaskList() {
            return tasklist;
        }

        public void setTaskList(TaskList taskList) {
            this.tasklist = taskList;
        }

        @Override
        public void setTaskList(ReadOnlyTaskList taskList) {
            fail("This method should not be called.");
        }

        @Override
        public void deleteTask(ReadOnlyTask target) throws TaskNotFoundException {
            fail("This method should not be called.");

        }

        @Override
        public void addTask(ReadOnlyTask task) throws DuplicateTaskException {
            fail("This method should not be called.");
        }

        @Override
        public void markTask(ReadOnlyTask task) throws TaskNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void unmarkTask(ReadOnlyTask task) throws TaskNotFoundException {
            fail("This method should not be called.");

        }

        @Override
        public void updateTask(ReadOnlyTask target, ReadOnlyTask editedTask)
                throws DuplicateTaskException, TaskNotFoundException {
            fail("This method should not be called.");

        }

        @Override
        public UnmodifiableObservableList<ReadOnlyTask> getFilteredAndSortedTaskList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void updateFilteredListToShowAllIncomplete(Set<Tag> tagList, boolean showFloating) {
            fail("This method should not be called.");
        }

        @Override
        public void updateFilteredTaskList(Set<String> keywords, boolean listIncompleted) {
            fail("This method should not be called.");
        }

        @Override
        public void updateFilteredTaskListToShowPeriod(Deadline deadline, AttributeInputted attInput,
                boolean listIncompleted, Set<Tag> tagList) {
            fail("This method should not be called.");
        }

        @Override
        public void saveTaskList() {
            fail("This method should not be called.");
        }

        @Override
        public void updateFilteredListToShowAllComplete(Set<Tag> tagList, boolean listFloating) {
            fail("This method should not be called.");
        }

        @Override
        public void sort() {
            fail("This method should not be called.");
        }

        @Override
        public void updateFilteredTaskListToEmpty() {
            fail("This method should not be called.");
        }

        @Override
        public void changeOptions() {
            fail("This method should not be called.");
        }

    }

}
