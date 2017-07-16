package teamthree.twodo.alarm;

import static org.junit.Assert.assertTrue;

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
import teamthree.twodo.model.ReadOnlyTaskBook;
import teamthree.twodo.model.TaskBook;
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
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.toString();
        }
        assertTrue(isEventCaught);
    }

    private class ModelManagerStub implements Model {
        private TaskBook taskbook;

        public ModelManagerStub() {
            try {
                setTaskbook(new TaskBook(generateSampleTaskBook()));
            } catch (IllegalValueException e) {
                // should not reach here
                e.printStackTrace();
            }
        }

        private TaskBook generateSampleTaskBook() throws IllegalValueException {
            try {
                List<Task> samples = new ArrayList<Task>();
                samples.addAll(TestUtil.generateSampleTaskData());
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm");
                samples.add(new TaskWithDeadlineBuilder().withDeadline(dateFormat.format(new Date())).build());
                TaskBook sampleTb = new TaskBook();
                for (Task samplePerson : samples) {
                    sampleTb.addTask(samplePerson);
                }
                return sampleTb;
            } catch (DuplicateTaskException e) {
                throw new AssertionError("sample data cannot contain duplicate persons", e);
            } catch (Exception e) {
                e.printStackTrace();
                throw new AssertionError("Got rekt", e);
            }
        }

        @Override
        public void resetData(ReadOnlyTaskBook newData) {
            // TODO Auto-generated method stub

        }

        @Override
        public ReadOnlyTaskBook getTaskBook() {
            return taskbook;
        }

        @Override
        public void deleteTask(ReadOnlyTask target) throws TaskNotFoundException {
            // TODO Auto-generated method stub

        }

        @Override
        public void addTask(ReadOnlyTask task) throws DuplicateTaskException {
            // TODO Auto-generated method stub

        }

        @Override
        public void markTask(ReadOnlyTask task) throws TaskNotFoundException {
            // TODO Auto-generated method stub

        }

        @Override
        public void unmarkTask(ReadOnlyTask task) throws TaskNotFoundException {
            // TODO Auto-generated method stub

        }

        @Override
        public void updateTask(ReadOnlyTask target, ReadOnlyTask editedTask)
                throws DuplicateTaskException, TaskNotFoundException {
            // TODO Auto-generated method stub

        }

        @Override
        public UnmodifiableObservableList<ReadOnlyTask> getFilteredAndSortedTaskList() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void updateFilteredListToShowAllIncomplete(Set<Tag> tagList, boolean listFloating) {
            // TODO Auto-generated method stub

        }

        @Override
        public void updateFilteredTaskList(Set<String> keywords, boolean listIncompleted) {
            // TODO Auto-generated method stub

        }

        @Override
        public void updateFilteredListToShowPeriod(Deadline deadline, AttributeInputted attInput,
                boolean listIncompleted, Set<Tag> tagList) {
            // TODO Auto-generated method stub

        }

        @Override
        public void saveTaskBook() {
            // TODO Auto-generated method stub

        }

        public TaskBook getTaskbook() {
            return taskbook;
        }

        public void setTaskbook(TaskBook taskbook) {
            this.taskbook = taskbook;
        }

        @Override
        public void updateFilteredListToShowAllComplete(Set<Tag> tagList, boolean listFloating) {
            // TODO Auto-generated method stub

        }

        @Override
        public void sort() {
            // TODO Auto-generated method stub
        }

    }

}
