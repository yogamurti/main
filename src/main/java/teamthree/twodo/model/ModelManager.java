package teamthree.twodo.model;

import static teamthree.twodo.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import teamthree.twodo.commons.core.ComponentManager;
import teamthree.twodo.commons.core.LogsCenter;
import teamthree.twodo.commons.core.UnmodifiableObservableList;
import teamthree.twodo.commons.events.model.TaskBookChangedEvent;
import teamthree.twodo.commons.util.StringUtil;
import teamthree.twodo.logic.commands.ListCommand.AttributeInputted;
import teamthree.twodo.model.tag.Tag;
import teamthree.twodo.model.task.Deadline;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.exceptions.DuplicateTaskException;
import teamthree.twodo.model.task.exceptions.TaskNotFoundException;

/**
 * Represents the in-memory model of the address book data. All changes to any
 * model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private TaskBook taskBook;
    private final FilteredList<ReadOnlyTask> filteredTasks;
    private final SortedList<ReadOnlyTask> sortedTasks;

    /**
     * Initializes a ModelManager with the given taskBook and userPrefs.
     */
    public ModelManager(ReadOnlyTaskBook taskBook, UserPrefs userPrefs) {
        super();
        requireAllNonNull(taskBook, userPrefs);

        logger.fine("Initializing with task book: " + taskBook + " and user prefs " + userPrefs);

        this.taskBook = new TaskBook(taskBook);
        filteredTasks = new FilteredList<>(this.taskBook.getTaskList());
        updateFilteredListToShowAllIncomplete(null, false);
        sortedTasks = new SortedList<>(filteredTasks);
    }

    public ModelManager() {
        this(new TaskBook(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlyTaskBook newData) {
        taskBook.resetData(newData);
        indicateTaskBookChanged();
    }

    @Override
    public ReadOnlyTaskBook getTaskBook() {
        return taskBook;
    }

    @Override
    public void setTaskBook(ReadOnlyTaskBook taskBook) {
        this.taskBook = new TaskBook(taskBook);
    }

    /** Raises an event to indicate the model has changed */
    private void indicateTaskBookChanged() {
        raise(new TaskBookChangedEvent(taskBook));
    }

    @Override
    public synchronized void deleteTask(ReadOnlyTask target) throws TaskNotFoundException {
        taskBook.removeTask(target);
        indicateTaskBookChanged();
    }

    @Override
    public synchronized void addTask(ReadOnlyTask person) throws DuplicateTaskException {
        taskBook.addTask(person);
        updateFilteredListToShowAllIncomplete(null, false);
        indicateTaskBookChanged();
    }

    @Override
    public void markTask(ReadOnlyTask target) throws TaskNotFoundException {
        taskBook.markTask(target);
        indicateTaskBookChanged();
    }

    @Override
    public void unmarkTask(ReadOnlyTask target) throws TaskNotFoundException {
        taskBook.unmarkTask(target);
        indicateTaskBookChanged();
    }

    @Override
    public void saveTaskBook() {
        indicateTaskBookChanged();
    }

    @Override
    public void updateTask(ReadOnlyTask target, ReadOnlyTask editedTask)
            throws DuplicateTaskException, TaskNotFoundException {
        requireAllNonNull(target, editedTask);

        taskBook.updateTask(target, editedTask);
        indicateTaskBookChanged();
    }

    //@@author A0107433N
    // =========== Filtered Task List Accessors
    // =============================================================

    /**
     * Return a list of {@code ReadOnlyTask} backed by the internal list of
     * {@code taskBook}
     */
    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredAndSortedTaskList() {
        sort();
        return new UnmodifiableObservableList<>(sortedTasks);
    }

    @Override
    public void updateFilteredListToShowAllIncomplete(Set<Tag> tagList, boolean listFloating) {
        updateFilteredTaskList(new PredicateExpression(new TagQualifier(tagList, true, listFloating)));
    }

    @Override
    public void updateFilteredListToShowAllComplete(Set<Tag> tagList, boolean listFloating) {
        updateFilteredTaskList(new PredicateExpression(new TagQualifier(tagList, false, listFloating)));
    }

    private void updateFilteredTaskList(Expression expression) {
        filteredTasks.setPredicate(expression::satisfies);
    }

    @Override
    public void updateFilteredTaskList(Set<String> keywords, boolean listIncomplete) {
        updateFilteredTaskList(new PredicateExpression(new TotalQualifier(keywords, listIncomplete)));
    }

    @Override
    public void updateFilteredListToShowPeriod(Deadline deadline,
            AttributeInputted attInput, boolean listIncomplete, Set<Tag> tagList) {
        updateFilteredTaskList(new PredicateExpression(new PeriodQualifier(deadline, attInput,
                listIncomplete, tagList)));
    }

    /**
     * Sorts list by deadline
     */
    @Override
    public void sort() {
        sortedTasks.setComparator(new Comparator<ReadOnlyTask>() {
            @Override
            public int compare(ReadOnlyTask task1, ReadOnlyTask task2) {
                if (task1.getDeadline().isPresent() && task2.getDeadline().isPresent()) {
                    return task1.getDeadline().get().getEndDate().compareTo(task2.getDeadline().get().getEndDate());
                } else {
                    if (task1.getDeadline().isPresent()) {
                        return -1;
                    } else if (task2.getDeadline().isPresent()) {
                        return 1;
                    } else {
                        return task1.getName().fullName.compareTo(task2.getName().fullName);
                    }
                }
            }
        });
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return taskBook.equals(other.taskBook) && filteredTasks.equals(other.filteredTasks);
    }

    // ========== Inner classes/interfaces used for filtering
    // =================================================

    interface Expression {
        boolean satisfies(ReadOnlyTask task);

        String toString();
    }

    private class PredicateExpression implements Expression {

        private final Qualifier qualifier;

        PredicateExpression(Qualifier qualifier) {
            this.qualifier = qualifier;
        }

        @Override
        public boolean satisfies(ReadOnlyTask task) {
            return qualifier.run(task);
        }

        @Override
        public String toString() {
            return qualifier.toString();
        }
    }

    interface Qualifier {
        boolean run(ReadOnlyTask task);

        String toString();
    }

    private class TotalQualifier implements Qualifier {
        private Set<String> keyWords;
        private boolean listIncomplete;

        TotalQualifier(Set<String> keyWords, boolean listIncomplete) {
            this.keyWords = keyWords;
            this.listIncomplete = listIncomplete;
        }

        @Override
        public boolean run(ReadOnlyTask task) {
            return (nameQualifies(task) || descriptionQualifies(task) || tagsQualifies(task))
                    && completedQualifies(task);
        }

        private boolean nameQualifies(ReadOnlyTask task) {
            return keyWords.stream()
                    .filter(keyword -> StringUtil.containsWordIgnoreCase(task.getName().fullName, keyword)).findAny()
                    .isPresent();
        }

        private boolean descriptionQualifies(ReadOnlyTask task) {
            if (task.getDeadline().isPresent()) {
                return keyWords.stream()
                        .filter(keyword -> StringUtil.containsWordIgnoreCase(task.getDescription().value, keyword))
                        .findAny().isPresent();
            } else {
                return false;
            }
        }

        private boolean tagsQualifies(ReadOnlyTask task) {
            boolean qualifies = false;
            Set<Tag> tags = task.getTags();
            Iterator<Tag> tagIterator = tags.iterator();
            while (!qualifies && tagIterator.hasNext()) {
                qualifies = keyWords.stream()
                        .filter(keyword -> StringUtil.containsWordIgnoreCase(tagIterator.next().tagName, keyword))
                        .findAny().isPresent();
            }
            return qualifies;
        }

        private boolean completedQualifies(ReadOnlyTask task) {
            return task.isCompleted() != listIncomplete;
        }

        @Override
        public String toString() {
            return "keywords=" + String.join(", ", keyWords);
        }

    }

    private class PeriodQualifier implements Qualifier {
        private Deadline deadlineToCheck;
        private AttributeInputted attInput;
        private boolean listIncomplete;
        private Set<Tag> tagList;

        PeriodQualifier(Deadline deadline, AttributeInputted attInput,
                boolean listIncomplete, Set<Tag> tagList) {
            this.deadlineToCheck = deadline;
            this.attInput = attInput;
            this.listIncomplete = listIncomplete;
            this.tagList = tagList;
        }

        @Override
        public boolean run(ReadOnlyTask task) {
            return deadlineQualifies(task) && tagsQualifies(task) && completedQualifies(task);
        }

        public boolean deadlineQualifies(ReadOnlyTask task) {
            if (!task.getDeadline().isPresent()) {
                return false;
            } else {
                if (task.isCompleted() == !listIncomplete) {
                    switch (attInput) {
                    case START:
                        return task.getDeadline().get().getStartDate().after(deadlineToCheck.getStartDate());
                    case END:
                        return task.getDeadline().get().getStartDate().before(deadlineToCheck.getEndDate());
                    case BOTH:
                        return task.getDeadline().get().getStartDate().after(deadlineToCheck.getStartDate())
                                && task.getDeadline().get().getStartDate().before(deadlineToCheck.getEndDate());
                    default:
                        return false;
                    }
                } else {
                    return false;
                }
            }

        }

        private boolean tagsQualifies(ReadOnlyTask task) {
            if (tagList == null || tagList.isEmpty()) {
                return true;
            }
            boolean qualifies = false;
            Set<Tag> tags = task.getTags();
            Iterator<Tag> tagIterator = tags.iterator();
            while (!qualifies && tagIterator.hasNext()) {
                qualifies = tagList.stream()
                        .filter(tag -> StringUtil.containsWordIgnoreCase(tagIterator.next().tagName, tag.tagName))
                        .findAny().isPresent();
            }
            return qualifies;
        }

        private boolean completedQualifies(ReadOnlyTask task) {
            return task.isCompleted() != listIncomplete;
        }
    }

    private class TagQualifier implements Qualifier {
        private Set<Tag> tagList;
        private boolean listIncomplete;
        private boolean showFloating;

        TagQualifier(Set<Tag> tagList, boolean listIncomplete, boolean showFloating) {
            this.tagList = tagList;
            this.listIncomplete = listIncomplete;
            this.showFloating = showFloating;
        }

        @Override
        public boolean run(ReadOnlyTask task) {
            return tagsQualifies(task) && completedQualifies(task) && floatingQualifies(task);
        }

        private boolean tagsQualifies(ReadOnlyTask task) {
            if (tagList == null || tagList.isEmpty()) {
                return true;
            }
            boolean qualifies = false;
            Set<Tag> tags = task.getTags();
            Iterator<Tag> tagIterator = tags.iterator();
            while (!qualifies && tagIterator.hasNext()) {
                qualifies = tagList.stream()
                        .filter(tag -> StringUtil.containsWordIgnoreCase(tagIterator.next().tagName, tag.tagName))
                        .findAny().isPresent();
            }
            return qualifies;
        }

        private boolean completedQualifies(ReadOnlyTask task) {
            return task.isCompleted() != listIncomplete;
        }

        private boolean floatingQualifies(ReadOnlyTask task) {
            return task.getDeadline().isPresent() != showFloating;
        }
        @Override
        public String toString() {
            return tagList.toString();
        }

    }
}
