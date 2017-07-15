//@@author A0139267W
package teamthree.twodo.commons.core.options;

import static teamthree.twodo.commons.util.CollectionUtil.requireAllNonNull;

// Sets the default options for 2Do
public class DefaultOption extends Options {

    private Alarm alarm;
    private AutoMark autoMark;

    public DefaultOption(Alarm alarm, AutoMark autoMark) {
        super(alarm, autoMark);
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
    public String toString() {
        return "\n" + alarm.toString() + autoMark.toString();
    }

}
