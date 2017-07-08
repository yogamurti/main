package teamthree.twodo.testutil;

import teamthree.twodo.commons.core.index.Index;
import teamthree.twodo.commons.exceptions.IllegalValueException;
import teamthree.twodo.model.TaskBook;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.model.task.exceptions.DuplicateTaskException;

/**
 *
 */
public class TypicalPersons {

    public static final Index INDEX_FIRST_PERSON = Index.fromOneBased(1);
    public static final Index INDEX_SECOND_PERSON = Index.fromOneBased(2);
    public static final Index INDEX_THIRD_PERSON = Index.fromOneBased(3);

    public final Task alice, benson, carl, daniel, elle, fiona, george, hoon, ida;

    public TypicalPersons() {
        try {
            alice = new TaskWithDeadlineBuilder().withName("Alice Pauline")
                    .withDescription("123, Jurong West Ave 6, #08-111").withDeadline("17 april 2018")
                    .withTags("friends").build();
            benson = new TaskWithDeadlineBuilder().withName("Benson Meier")
                    .withDescription("311, Clementi Ave 2, #02-25").withTags("owesMoney", "friends").build();
            carl = new TaskWithDeadlineBuilder().withName("Carl Kurz").withDeadline("fri10am")
                    .withDescription("wall street").build();
            daniel = new TaskWithDeadlineBuilder().withName("Daniel Meier").withDeadline("sat10pm")
                    .withDescription("10th street").build();
            elle = new TaskWithDeadlineBuilder().withName("Elle Meyer").withDeadline("next week")
                    .withDescription("michegan ave").build();
            fiona = new TaskWithDeadlineBuilder().withName("Fiona Kunz").withDeadline("next mon")
                    .withDescription("little tokyo").build();
            george = new TaskWithDeadlineBuilder().withName("George Best").withDeadline("july 2018")
                    .withDescription("4th street").build();

            // Manually added
            hoon = new TaskWithDeadlineBuilder().withName("Hoon Meier").withDeadline("march 2020")
                    .withDescription("little india").build();
            ida = new TaskWithDeadlineBuilder().withName("Ida Mueller").withDeadline("tomorrow")
                    .withDescription("chicago ave").build();
        } catch (IllegalValueException e) {
            throw new AssertionError("Sample data cannot be invalid", e);
        }
    }

    public static void loadAddressBookWithSampleData(TaskBook ab) {
        for (Task task : new TypicalPersons().getTypicalPersons()) {
            try {
                ab.addTask(new Task(task));
            } catch (DuplicateTaskException e) {
                assert false : "not possible";
            }
        }
    }

    public Task[] getTypicalPersons() {
        return new Task[] { alice, benson, carl, daniel, elle, fiona, george };
    }

    public TaskBook getTypicalAddressBook() {
        TaskBook ab = new TaskBook();
        loadAddressBookWithSampleData(ab);
        return ab;
    }
}
