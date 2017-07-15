//@@author A0139267W
package teamthree.twodo.commons.core.options;

// Sets the default options for 2Do
public class DefaultOption extends Options {

    private Alarm alarm;
    private AutoMark autoMark;

    public DefaultOption(Alarm alarm, AutoMark autoMark) {
        super(alarm, autoMark);
    }

    public void editAlarm(Alarm newAlarm) {
        alarm = newAlarm;
    }

    public void editAutoMark(AutoMark newAutoMark) {
        autoMark = newAutoMark;
    }

}
