//@@author A0139267W
package teamthree.twodo.commons.core.options;

import static teamthree.twodo.commons.util.CollectionUtil.requireAllNonNull;

public class Options {

    private final Alarm alarm;
    private final AutoMark autoMark;

    public Options(Alarm alarm, AutoMark autoMark) {
        requireAllNonNull(alarm, autoMark);
        this.alarm = alarm;
        this.autoMark = autoMark;
    }

    @Override
    public String toString() {
        return "\n" + alarm.toString() + autoMark.toString();
    }

    public Alarm getAlarm() {
        return alarm;
    }

    public AutoMark getAutoMark() {
        return autoMark;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Options // instanceof handles nulls
                && this.alarm.equals(((Options) other).alarm)) // state check
                && this.autoMark == ((Options) other).autoMark; // state check
    }
}
