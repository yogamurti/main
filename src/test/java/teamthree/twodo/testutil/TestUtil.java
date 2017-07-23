package teamthree.twodo.testutil;

import static teamthree.twodo.model.util.SampleDataUtil.getTagSet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import guitests.guihandles.TaskCardHandle;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import junit.framework.AssertionFailedError;
import teamthree.twodo.commons.core.index.Index;
import teamthree.twodo.commons.exceptions.IllegalValueException;
import teamthree.twodo.commons.util.FileUtil;
import teamthree.twodo.commons.util.XmlUtil;
import teamthree.twodo.model.task.Deadline;
import teamthree.twodo.model.task.Description;
import teamthree.twodo.model.task.Name;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.model.task.TaskWithDeadline;

/**
 * A utility class for test cases.
 */
public class TestUtil {

    /**
     * Folder used for temp files created during testing. Ignored by Git.
     */
    public static final String SANDBOX_FOLDER = FileUtil.getPath("./src/test/data/sandbox/");

    public static final Task[] SAMPLE_TASK_DATA = getSampleTaskData();

    public static void assertThrows(Class<? extends Throwable> expected, Runnable executable) {
        try {
            executable.run();
        } catch (Throwable actualException) {
            if (actualException.getClass().isAssignableFrom(expected)) {
                return;
            }
            String message = String.format("Expected thrown: %s, actual: %s", expected.getName(),
                    actualException.getClass().getName());
            throw new AssertionFailedError(message);
        }
        throw new AssertionFailedError(
                String.format("Expected %s to be thrown, but nothing was thrown.", expected.getName()));
    }

    private static Task[] getSampleTaskData() {
        try {
            //CHECKSTYLE.OFF: LineLength
            return new Task[] {
                new TaskWithDeadline(new Name("Class"), new Deadline("fri 10am", "sat 10pm", Deadline.NULL_VALUE),
                        new Description("Homework"), getTagSet(), false),
                new TaskWithDeadline(new Name("Tutorial"), new Deadline("thu 11am", "sat 11pm", Deadline.NULL_VALUE),
                        new Description("Chapter 4"), getTagSet(), false),
                new TaskWithDeadline(new Name("Lecture"), new Deadline("next fri 10am", "next sat 10pm", Deadline.NULL_VALUE),
                        new Description("Chapter 5"), getTagSet(), false),
                new TaskWithDeadline(new Name("Labs"), new Deadline("wed 10am", "sat 10pm", Deadline.NULL_VALUE),
                        new Description("Sit-in Lab"), getTagSet(), false),
                new TaskWithDeadline(new Name("CCA"),
                        new Deadline("next wed 10am", "next wed 10pm", Deadline.NULL_VALUE), new Description("Proposal"),
                        getTagSet(), false),
                new TaskWithDeadline(new Name("Party"), new Deadline("fri 10am", "sat 10pm", Deadline.NULL_VALUE),
                        new Description("little tokyo"), getTagSet(), false),
                new TaskWithDeadline(new Name("Clothes Shopping"), new Deadline("10am", "10pm", Deadline.NULL_VALUE),
                        new Description("4th street"), getTagSet(), false),
                new TaskWithDeadline(new Name("Meeting Friends"), new Deadline("23/12/17 10pm", "25/12/17 3am", Deadline.NULL_VALUE),
                        new Description("little india"), getTagSet(), false),
                new TaskWithDeadline(new Name("Family lunch"), new Deadline("12/12/17 2pm", "12/12/17 4pm", Deadline.NULL_VALUE),
                        new Description("chicago ave"), getTagSet(), false) };
            //CHECKSTYLE.ON: LineLength
        } catch (IllegalValueException e) {
            assert false;
            // not possible
            return null;
        }
    }

    public static List<Task> generateSampleTaskData() {
        return Arrays.asList(SAMPLE_TASK_DATA);
    }

    /**
     * Appends the file name to the sandbox folder path. Creates the sandbox
     * folder if it doesn't exist.
     *
     * @param fileName
     * @return
     */
    public static String getFilePathInSandboxFolder(String fileName) {
        try {
            FileUtil.createDirs(new File(SANDBOX_FOLDER));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return SANDBOX_FOLDER + fileName;
    }

    public static <T> void createDataFileWithData(T data, String filePath) {
        try {
            File saveFileForTesting = new File(filePath);
            FileUtil.createIfMissing(saveFileForTesting);
            XmlUtil.saveDataToFile(saveFileForTesting, data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets mid point of a node relative to the screen.
     *
     * @param node
     * @return
     */
    public static Point2D getScreenMidPoint(Node node) {
        double x = getScreenPos(node).getMinX() + node.getLayoutBounds().getWidth() / 2;
        double y = getScreenPos(node).getMinY() + node.getLayoutBounds().getHeight() / 2;
        return new Point2D(x, y);
    }

    public static Bounds getScreenPos(Node node) {
        return node.localToScreen(node.getBoundsInLocal());
    }

    /**
     * Removes a subset from the list of tasks.
     *
     * @param tasks
     *            The list of tasks
     * @param tasksToRemove
     *            The subset of tasks.
     * @return The modified tasks after removal of the subset from tasks.
     */
    public static Task[] removeTasksFromList(final Task[] tasks, Task... tasksToRemove) {
        List<Task> listOfTasks = asList(tasks);
        listOfTasks.removeAll(asList(tasksToRemove));
        return listOfTasks.toArray(new Task[listOfTasks.size()]);
    }

    /**
     * Returns a copy of the list with the task at specified index removed.
     *
     * @param list
     *            original list to copy from
     */
    public static Task[] removeTaskFromList(final Task[] list, Index index) {
        return removeTasksFromList(list, list[index.getZeroBased()]);
    }
    /**
     * Returns a copy of the list with the person at specified index removed.
     *
     * @param list
     *            original list to copy from
     */
    public static List<ReadOnlyTask> removeTaskFromList(final List<ReadOnlyTask> list, Index index) {
        list.remove(index.getZeroBased());
        return list;
    }

    /**
     * Appends tasks to the array of tasks.
     *
     * @param tasks
     *            A array of tasks.
     * @param tasksToAdd
     *            The tasks that are to be appended behind the original array.
     * @return The modified array of tasks.
     */
    public static Task[] addTasksToList(final Task[] tasks, Task... tasksToAdd) {
        List<Task> listOfTasks = asList(tasks);
        listOfTasks.addAll(asList(tasksToAdd));
        return listOfTasks.toArray(new Task[listOfTasks.size()]);
    }

    private static <T> List<T> asList(T[] objs) {
        List<T> list = new ArrayList<>();
        for (T obj : objs) {
            list.add(obj);
        }
        return list;
    }

    public static boolean compareCardAndTask(TaskCardHandle card, ReadOnlyTask person) {
        return card.isSamePerson(person);
    }

}
