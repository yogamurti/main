package teamthree.twodo.model;

import java.util.Objects;

import teamthree.twodo.commons.core.GuiSettings;

/**
 * Represents User's preferences.
 */
public class UserPrefs {

    private GuiSettings guiSettings;
    private String taskBookFilePath = "data/addressbook.xml";
    private String notifiedListFilePath = "data/notifiedtasks.xml";
    private String taskBookName = "2Do";

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

    public String getTaskBookFilePath() {
        return taskBookFilePath;
    }

    public String getNotifiedListFilePath() {
        return notifiedListFilePath;
    }

    public void setTaskBookFilePath(String addressBookFilePath) {
        this.taskBookFilePath = addressBookFilePath;
    }

    public void setNotifiedListFilePath(String notifiedListFilePath) {
        this.taskBookFilePath = notifiedListFilePath;
    }

    public String getTaskBookName() {
        return taskBookName;
    }

    public void setTaskBookName(String addressBookName) {
        this.taskBookName = addressBookName;
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

        return Objects.equals(guiSettings, o.guiSettings) && Objects.equals(taskBookFilePath, o.taskBookFilePath)
                && Objects.equals(notifiedListFilePath, o.notifiedListFilePath)
                && Objects.equals(taskBookName, o.taskBookName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guiSettings, taskBookFilePath, taskBookName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gui Settings : " + guiSettings.toString());
        sb.append("\nLocal data file location : " + taskBookFilePath + ", " + notifiedListFilePath);
        sb.append("\nTaskBook name : " + taskBookName);
        return sb.toString();
    }

}
