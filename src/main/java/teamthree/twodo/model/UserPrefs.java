package teamthree.twodo.model;

import java.util.Objects;

import teamthree.twodo.commons.core.GuiSettings;

/**
 * Represents User's preferences.
 */
public class UserPrefs {

    private GuiSettings guiSettings;
    private String taskListFilePath = "data/2Do.xml";
    private String notifiedListFilePath = "data/notifiedtasks.xml";
    private String taskListName = "My2DoList";

    public UserPrefs() {
        this.setGuiSettings(500, 500, 0, 0);
    }

    public GuiSettings getGuiSettings() {
        return guiSettings == null ? new GuiSettings() : guiSettings;
    }

    public void updateLastUsedGuiSetting(GuiSettings guiSettings) {
        this.guiSettings = guiSettings;
    }

    public void setGuiSettings(double width, double height, int x, int y) {
        guiSettings = new GuiSettings(width, height, x, y);
    }

    public String getTaskListFilePath() {
        return taskListFilePath;
    }

    public String getNotifiedListFilePath() {
        return notifiedListFilePath;
    }

    public void setTaskListFilePath(String addressBookFilePath) {
        this.taskListFilePath = addressBookFilePath;
    }

    public void setNotifiedListFilePath(String notifiedListFilePath) {
        this.taskListFilePath = notifiedListFilePath;
    }

    public String getTaskListName() {
        return taskListName;
    }

    public void setTaskListName(String addressBookName) {
        this.taskListName = addressBookName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UserPrefs)) { //this handles null as well.
            return false;
        }

        UserPrefs o = (UserPrefs) other;

        return Objects.equals(guiSettings, o.guiSettings) && Objects.equals(taskListFilePath, o.taskListFilePath)
                && Objects.equals(notifiedListFilePath, o.notifiedListFilePath)
                && Objects.equals(taskListName, o.taskListName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guiSettings, taskListFilePath, taskListName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gui Settings : " + guiSettings.toString());
        sb.append("\nLocal data file location : " + taskListFilePath + ", " + notifiedListFilePath);
        sb.append("\nTaskBook name : " + taskListName);
        return sb.toString();
    }

}
