package teamthree.twodo.commons.core.options;

import static teamthree.twodo.commons.util.CollectionUtil.requireAllNonNull;

//@@author A0139267W
// Represents an alarm setting that will be automatically set for tasks with deadlines
public class Alarm {
    private final String alarm;

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
                && this.alarm.equals(((Alarm) other).getValue())); // state check
    }

}
