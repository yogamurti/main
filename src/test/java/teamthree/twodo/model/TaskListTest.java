package teamthree.twodo.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import teamthree.twodo.model.tag.Tag;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.testutil.TypicalTask;

public class TaskListTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final TaskList taskList = new TaskList();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), taskList.getTaskList());
        assertEquals(Collections.emptyList(), taskList.getTagList());
    }

    @Test
    public void resetDataNullThrowsNullPointerException() {
        thrown.expect(NullPointerException.class);
        taskList.resetData(null);
    }

    @Test
    public void resetDataWithValidReadOnlyTaskListReplacesData() {
        TaskList newData = new TypicalTask().getTypicalTaskList();
        taskList.resetData(newData);
        assertEquals(newData, taskList);
    }

    @Test
    public void resetData_withDuplicateTasks_throwsAssertionError() {
        TypicalTask td = new TypicalTask();
        // Repeat td.alice twice
        List<Task> newTasks = Arrays.asList(new Task(td.cs2103), new Task(td.cs2103));
        List<Tag> newTags = new ArrayList<>(td.cs2103.getTags());
        TaskListStub newData = new TaskListStub(newTasks, newTags);

        thrown.expect(AssertionError.class);
        taskList.resetData(newData);
    }

    @Test
    public void resetData_withDuplicateTags_throwsAssertionError() {
        TaskList typicalTaskList = new TypicalTask().getTypicalTaskList();
        List<ReadOnlyTask> newTasks = typicalTaskList.getTaskList();
        List<Tag> newTags = new ArrayList<>(typicalTaskList.getTagList());
        // Repeat the first tag twice
        newTags.add(newTags.get(0));
        TaskListStub newData = new TaskListStub(newTasks, newTags);

        thrown.expect(AssertionError.class);
        taskList.resetData(newData);
    }

    /**
     * A stub ReadOnlyTaskList whose tasks and tags lists can violate interface constraints.
     */
    private static class TaskListStub implements ReadOnlyTaskList {
        private final ObservableList<ReadOnlyTask> tasks = FXCollections.observableArrayList();
        private final ObservableList<Tag> tags = FXCollections.observableArrayList();

        TaskListStub(Collection<? extends ReadOnlyTask> tasks, Collection<? extends Tag> tags) {
            this.tasks.setAll(tasks);
            this.tags.setAll(tags);
        }

        @Override
        public ObservableList<ReadOnlyTask> getTaskList() {
            return tasks;
        }

        @Override
        public ObservableList<Tag> getTagList() {
            return tags;
        }
    }

}
