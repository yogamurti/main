package teamthree.twodo.commons.core.options;

import static teamthree.twodo.commons.util.CollectionUtil.requireAllNonNull;

//@@author A0139267W
// Represents a global option setting that can be edited
public class Options {

    private Alarm alarm;
    private AutoMark autoMark;

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

    public void editAlarm(Alarm newAlarm) {
        requireAllNonNull(newAlarm);
        alarm = newAlarm;
    }

    public void editAutoMark(AutoMark newAutoMark) {
        requireAllNonNull(newAutoMark);
        autoMark = newAutoMark;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Options // instanceof handles nulls
                && this.alarm.equals(((Options) other).alarm) // state check
                && this.autoMark.equals(((Options) other).autoMark)); // state check
    }
}
