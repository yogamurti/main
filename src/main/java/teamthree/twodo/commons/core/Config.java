package teamthree.twodo.commons.core;

import java.util.Objects;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Config values used by the application
public class Config {

    // Default notification period of 1 day. Can be changed by user.
    private static Long notificationPeriod = (long) (1000 * 60 * 60 * 24);
    private static String notificationPeriodToString = "one day";
    private static String defaultConfigFile = "config.json";

    // To change default notification period
    private static final long DAY_TO_MILLIS = 1000 * 60 * 60 * 24;
    private static final long WEEK_TO_MILLIS = DAY_TO_MILLIS * 7;

    // Config values customizable through config file
    private String appTitle = "Description App";
    private Level logLevel = Level.INFO;
    private String userPrefsFilePath = "preferences.json";
    private String taskBookFilePath = "data/2Do.xml";

    public static Long getDefaultNotificationPeriod() {
        return notificationPeriod;
    }

    public static void setDefaultNotificationPeriod(long newNotificationPeriod) {
        notificationPeriod = newNotificationPeriod;
    }

    public String getAppTitle() {
        return appTitle;
    }

    public void setAppTitle(String appTitle) {
        this.appTitle = appTitle;
    }

    public Level getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(Level logLevel) {
        this.logLevel = logLevel;
    }

    public String getUserPrefsFilePath() {
        return userPrefsFilePath;
    }

    public void setUserPrefsFilePath(String userPrefsFilePath) {
        this.userPrefsFilePath = userPrefsFilePath;
    }

    public void setTaskBookFilePath(String taskBookFilePath) {
        this.taskBookFilePath = taskBookFilePath;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Config)) { //this handles null as well.
            return false;
        }

        Config o = (Config) other;

        return Objects.equals(appTitle, o.appTitle) && Objects.equals(logLevel, o.logLevel)
                && Objects.equals(userPrefsFilePath, o.userPrefsFilePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appTitle, logLevel, userPrefsFilePath);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("App title : " + appTitle);
        sb.append("\nCurrent log level : " + logLevel);
        sb.append("\nPreference file Location : " + userPrefsFilePath);
        return sb.toString();
    }

    public static String getDefaultConfigFile() {
        return defaultConfigFile;
    }

    public String getTaskBookFilePath() {
        return taskBookFilePath;
    }

    //@@author A0139267W
    public static void changeDefaultNotificationPeriod(String newNotificationPeriod) {
        notificationPeriodToString = newNotificationPeriod;
        Matcher integerParser = Pattern.compile("\\d*").matcher(newNotificationPeriod);
        assert (integerParser.find());
        integerParser.find();
        int period = Integer.parseInt(integerParser.group().trim());
        long newDefault = 0;
        if (newNotificationPeriod.toLowerCase().contains("day")) {
            newDefault = DAY_TO_MILLIS * period;
        } else if (newNotificationPeriod.toLowerCase().contains("week")) {
            newDefault = WEEK_TO_MILLIS * period;
        }
        notificationPeriod = newDefault;
    }

    public static String defaultNotificationPeriodToString() {
        return notificationPeriodToString;
    }
}
