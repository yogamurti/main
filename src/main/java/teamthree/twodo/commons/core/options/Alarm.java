//@@author A0139267W
package teamthree.twodo.commons.core.options;

// Represents an alarm setting to be set for tasks
public class Alarm {

    public final String alarm;

    public Alarm(String alarm) {
        this.alarm = alarm;
    }

    @Override
    public String toString() {
        return "Alarm: " + alarm + "\n";
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Alarm // instanceof handles nulls
                && this.alarm.equals(((Alarm) other).alarm)); // state check
    }

    @Override
    public int hashCode() {
        return alarm.hashCode();
    }
}
