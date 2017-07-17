package teamthree.twodo.automark;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.google.common.eventbus.Subscribe;

import teamthree.twodo.commons.core.ComponentManager;
import teamthree.twodo.commons.events.model.DeadlineTimeReachedEvent;
import teamthree.twodo.commons.events.model.TaskBookChangedEvent;
import teamthree.twodo.model.Model;
import teamthree.twodo.model.task.Deadline;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.TaskWithDeadline;

//@@author A0139267W
// Manages the auto-completion marking of tasks whose deadline has elapsed
public class AutoMarkManager extends ComponentManager {
    // List of tasks yet to be completed
    private final List<ReadOnlyTask> uncompletedList = new ArrayList<ReadOnlyTask>();
    // Keeps track of tasks that have been completed
    private final HashSet<ReadOnlyTask> completed = new HashSet<ReadOnlyTask>();

    private final Model model;

    // In charge of scheduling and executing auto-completion markings
    private final Timer masterClock = new Timer();

    // Completion time of the task with the most imminent deadline
    private Date nextCompletionTime;

    public AutoMarkManager(Model model) {
        this.model = model;
        syncWithMasterTaskList(model.getTaskBook().getTaskList());
    }

    /**
     * Synchronizes internal uncompleted list with the main task list
     *
     * @param masterList
     *            Full list of tasks from the task list
     */
    private synchronized void syncWithMasterTaskList(List<ReadOnlyTask> masterList) {
        if (masterList == null || masterList.isEmpty()) {
            return;
        }
        // Clear list first to avoid duplicates
        uncompletedList.clear();
        // Adds tasks which are not in the completed set
        masterList.forEach((t) -> {
            if (t instanceof TaskWithDeadline && !completed.contains(t)) {
                uncompletedList.add(t);
            }
        });
        sortTasksByDeadline();
        updateNextCompletion();
        startTimerTask();
    }

    public void startTimerTask() {

        if (nextCompletionTime == null) {
            return;
        }
        masterClock.schedule(new NextReminder(), nextCompletionTime);

    }

    private Date getCompletionTime(ReadOnlyTask task) {
        return task.getDeadline().get().getEndDate();
    }

    // =====================HELPER CLASS==========================

    private class NextReminder extends TimerTask {

        /**
         * The following command will be run upon reaching the scheduled timing.
         * It will raise a DeadlineTimeReachedEvent with all the
         * tasks that have reached the completion deadline.
         *
         * After that it will update internal information.
         */
        @Override
        public void run() {
            List<ReadOnlyTask> tasksToRemindOf = new ArrayList<ReadOnlyTask>();
            Date currentDate = new Date();
            uncompletedList.forEach((t) -> {
                if (getCompletionTime(t).before(currentDate) || getCompletionTime(t).equals(nextCompletionTime)) {
                    tasksToRemindOf.add(t);
                }
            });
            if (tasksToRemindOf.size() > 0) {
                raise(new DeadlineTimeReachedEvent(tasksToRemindOf));
            }

            updateInternalData(tasksToRemindOf);

            startTimerTask();
        }

    }

    // =========================HELPER METHODS=================================

    /**
     * Transfers the most recently reminded tasks from the uncompleted list to
     * the completed set. Updates the nextReminderTime with the deadline
     * of the next activity on the uncompleted list. Called only after a
     * DeadlineTimeReachedEvent.
     *
     * @param completedTasks
     *            the tasks which were sent with the
     *            DeadlineTimeReachedEvent
     */

    private synchronized void updateInternalData(List<ReadOnlyTask> completedTasks) {
        uncompletedList.removeAll(completedTasks);
        completed.addAll(completedTasks);
        updateNextCompletion();
    }

    // Sorts task list by their deadline
    private void sortTasksByDeadline() {
        uncompletedList.sort(new Comparator<ReadOnlyTask>() {

            @Override
            public int compare(ReadOnlyTask t, ReadOnlyTask u) {
                return getCompletionTime(t).compareTo(getCompletionTime(u));
            }

        });
    }

    // Updates nextCompletionTime to the next one on the uncompletedList.
    private void updateNextCompletion() {
        if (!uncompletedList.isEmpty()) {
            nextCompletionTime = removeInvalidDates() ? null : getCompletionTime(uncompletedList.get(0));
        } else {
            nextCompletionTime = null;
        }
    }

    /**
     * Transfers all invalid dates (i.e Default Dates) from uncompleted list to
     * completed set. This avoids an invalid date exception from being thrown at
     * startTimerTask. Returns whether uncompleted list is empty after
     * operation.
     *
     * @return true if completion list is empty after removing all invalid
     *         dates.
     */
    private boolean removeInvalidDates() {
        while (!getCompletionTime(uncompletedList.get(0)).after(Deadline.DEFAULT_DATE)) {
            completed.add(uncompletedList.remove(0));
        }
        return uncompletedList.isEmpty();
    }

    // =======================EVENT HANDLERS===========================

    // Synchronizes the uncompleted list with the master list when there is a change
    @Subscribe
    public void handleTaskBookChangedEvent(TaskBookChangedEvent event) {
        syncWithMasterTaskList(model.getTaskBook().getTaskList());
    }

}
