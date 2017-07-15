//@@author A0139267W
package teamthree.twodo.commons.core.options;

import static teamthree.twodo.commons.util.CollectionUtil.requireAllNonNull;

// Represents an alarm setting to be set for tasks
public class Alarm {
    public static final String EMPTY_ALARM = " ";

    public final String alarm;

    public Alarm(String alarm) {
        requireAllNonNull(alarm);
        this.alarm = alarm;
    }

    @Override
    public String toString() {
        requireAllNonNull(alarm);
        return "Alarm: " + alarm + "\n";
    }

    public String getValue() {
        return alarm;
    }

    @Override
    public boolean equals(Object other) {
        requireAllNonNull(alarm);
        return other == this // short circuit if same object
                || (other instanceof Alarm // instanceof handles nulls
                && this.alarm.equals(((Alarm) other).alarm)); // state check
    }

    @Override
    public int hashCode() {
        requireAllNonNull(alarm);
        return alarm.hashCode();
    }

    public boolean isEmpty() {
        return alarm.equals(EMPTY_ALARM);
    }

}
