package teamthree.twodo.testutil;

import static teamthree.twodo.model.util.SampleDataUtil.getTagSet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import guitests.guihandles.PersonCardHandle;
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

    public static final Task[] SAMPLE_PERSON_DATA = getSamplePersonData();

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

    private static Task[] getSamplePersonData() {
        try {
            //CHECKSTYLE.OFF: LineLength
            return new Task[] {
                new TaskWithDeadline(new Name("Ali Muster"), new Deadline("fri 10am", "sat 10pm", "1 day"), new Description("4th street"), getTagSet()),
                new TaskWithDeadline(new Name("Boris Mueller"), new Deadline("thu 11am", "sat 11pm", "1 day"), new Description("81th street"), getTagSet()),
                new TaskWithDeadline(new Name("Carl Kurz"), new Deadline("next fri 10am", "next sat 10pm", "1 day"), new Description("wall street"), getTagSet()),
                new TaskWithDeadline(new Name("Daniel Meier"), new Deadline("wed 10am", "sat 10pm", "2 days"), new Description("10th street"), getTagSet()),
                new TaskWithDeadline(new Name("Elle Meyer"), new Deadline("next wed 10am", "next wed 10pm", "3 days"), new Description("michegan ave"), getTagSet()),
                new TaskWithDeadline(new Name("Fiona Kunz"), new Deadline("fri 10am", "sat 10pm", "1 day"), new Description("little tokyo"), getTagSet()),
                new TaskWithDeadline(new Name("George Best"), new Deadline("10am", "10pm", "1 day"), new Description("4th street"), getTagSet()),
                new TaskWithDeadline(new Name("Hoon Meier"), new Deadline("23/12/17", "25/12/17", "1 day"), new Description("little india"), getTagSet()),
                new TaskWithDeadline(new Name("Ida Mueller"), new Deadline("12/12/17", "12/12/17", "1 day"), new Description("chicago ave"), getTagSet()) };
            //CHECKSTYLE.ON: LineLength
        } catch (IllegalValueException e) {
            assert false;
            // not possible
            return null;
        }
    }

    public static List<Task> generateSamplePersonData() {
        return Arrays.asList(SAMPLE_PERSON_DATA);
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
     * Removes a subset from the list of persons.
     *
     * @param persons
     *            The list of persons
     * @param personsToRemove
     *            The subset of persons.
     * @return The modified persons after removal of the subset from persons.
     */
    public static Task[] removePersonsFromList(final Task[] persons, Task... personsToRemove) {
        List<Task> listOfPersons = asList(persons);
        listOfPersons.removeAll(asList(personsToRemove));
        return listOfPersons.toArray(new Task[listOfPersons.size()]);
    }

    /**
     * Returns a copy of the list with the person at specified index removed.
     *
     * @param list
     *            original list to copy from
     */
    public static Task[] removePersonFromList(final Task[] list, Index index) {
        return removePersonsFromList(list, list[index.getZeroBased()]);
    }

    /**
     * Appends persons to the array of persons.
     *
     * @param persons
     *            A array of persons.
     * @param personsToAdd
     *            The persons that are to be appended behind the original array.
     * @return The modified array of persons.
     */
    public static Task[] addPersonsToList(final Task[] persons, Task... personsToAdd) {
        List<Task> listOfPersons = asList(persons);
        listOfPersons.addAll(asList(personsToAdd));
        return listOfPersons.toArray(new Task[listOfPersons.size()]);
    }

    private static <T> List<T> asList(T[] objs) {
        List<T> list = new ArrayList<>();
        for (T obj : objs) {
            list.add(obj);
        }
        return list;
    }

    public static boolean compareCardAndPerson(PersonCardHandle card, ReadOnlyTask person) {
        return card.isSamePerson(person);
    }

}
