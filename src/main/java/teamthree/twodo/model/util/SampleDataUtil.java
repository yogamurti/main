package teamthree.twodo.model.util;

import java.util.HashSet;
import java.util.Set;

import teamthree.twodo.commons.exceptions.IllegalValueException;
import teamthree.twodo.model.ReadOnlyTaskBook;
import teamthree.twodo.model.TaskBook;
import teamthree.twodo.model.tag.Tag;
import teamthree.twodo.model.task.Description;
import teamthree.twodo.model.task.Name;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.model.task.exceptions.DuplicateTaskException;

public class SampleDataUtil {
    public static Task[] getSamplePersons() {
        try {
            return new Task[] { new Task(new Name("CS2103 Project"), new Description("V0.2 on Mon"), getTagSet("NUS")),
                new Task(new Name("CS2103 Tutorial"), new Description("Complete by 4pm"), getTagSet("NUS")),
                new Task(new Name("Dinner"), new Description("7pm @ Ang Mo Kio"),
                          getTagSet("Friends", "coursemates")),
                new Task(new Name("Shopping"), new Description("New Clothes"), getTagSet("family", "friends")),
                new Task(new Name("cca Meeting"), new Description("NUS Tues 3pm"), getTagSet("NUS", "cca")),
                new Task(new Name("Buy lotion"), new Description("Lotion for hands"), getTagSet("myself")) };
        } catch (IllegalValueException e) {
            throw new AssertionError("sample data cannot be invalid", e);
        }
    }

    public static ReadOnlyTaskBook getSampleAddressBook() {
        try {
            TaskBook sampleAb = new TaskBook();
            for (Task samplePerson : getSamplePersons()) {
                sampleAb.addTask(samplePerson);
            }
            return sampleAb;
        } catch (DuplicateTaskException e) {
            throw new AssertionError("sample data cannot contain duplicate persons", e);
        }
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) throws IllegalValueException {
        HashSet<Tag> tags = new HashSet<>();
        for (String s : strings) {
            tags.add(new Tag(s));
        }

        return tags;
    }

}
