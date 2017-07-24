package teamthree.twodo.model.task;

import static java.util.Objects.requireNonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

import teamthree.twodo.commons.core.Config;
import teamthree.twodo.commons.exceptions.IllegalValueException;
//@@author A0124399W
/**
 * Represents a Task's deadline in the task list. Consists of start, end and
 * notification times. If task only has start time, end time will be = start
 * time. Notification time = start time + notification period (either default or
 * user provided)
 */
public class Deadline {

    public static final String MESSAGE_DEADLINE_CONSTRAINTS_STRICT = "Deadlines MUST have time. "
            + "Deadlines can be informal, e.g. fri 10am, " + "but if providing exact dates, "
            + "they should be of the format MM/DD/YY" + " and Time can be either AM/PM or 24HR\n";

    public static final String DEADLINE_VALIDATION_REGEX = "\\d+am|\\d+pm|\\d+hrs|\\d+ am|\\d+ pm|\\d+ hrs|\\d+";
    public static final String DAY_PARSE_REGEX = "[^\\d]+";
    // This value is only to be used by edit command to indicate a change of date
    public static final String NULL_VALUE = "0000";
    public static final int MIN_WORD_LENGTH_FOR_DAY = 2;
    public static final Date DEFAULT_DATE = new Date(0);

    private static final long DAY_TO_MILLIS = 1000 * 60 * 60 * 24;
    private static final long WEEK_TO_MILLIS = DAY_TO_MILLIS * 7;
    // Java Date contains both time and date so don't need separate Time object.
    private Long notificationPeriod = Config.getDefaultNotificationPeriod();
    private Date startDate;
    private Date endDate;

    public Deadline() {

    }

    public Deadline(String startDate, String endDate, String notificationPeriod) throws IllegalValueException {
        requireNonNull(startDate);
        requireNonNull(endDate);
        requireNonNull(notificationPeriod);
        PrettyTimeParser dateParser = new PrettyTimeParser();
        if (!isValidDeadline(startDate, endDate, dateParser)) {
            throw new IllegalValueException(MESSAGE_DEADLINE_CONSTRAINTS_STRICT);
        }

        this.startDate = !startDate.equals(NULL_VALUE) ? dateParser.parseSyntax(startDate).get(0).getDates().get(0)
                : DEFAULT_DATE;
        this.endDate = !endDate.equals(NULL_VALUE) ? dateParser.parseSyntax(endDate).get(0).getDates().get(0)
                : DEFAULT_DATE;
        this.notificationPeriod = !notificationPeriod.equals(NULL_VALUE) ? parseNotificationPeriod(notificationPeriod)
                : Config.getDefaultNotificationPeriod();
    }

    public Deadline(Date startDate, Date endDate, Long notificationPeriod) {
        requireNonNull(startDate.toString());
        requireNonNull(endDate.toString());
        requireNonNull(notificationPeriod.toString());

        this.startDate = startDate;
        this.endDate = endDate;
        this.notificationPeriod = notificationPeriod;
    }

    public Deadline(Deadline deadline) {
        startDate = deadline.getStartDate();
        endDate = deadline.getEndDate();
        notificationPeriod = deadline.getNotificationPeriod();
    }

    private boolean isValidDeadline(String startDate, String endDate, PrettyTimeParser dateParser) {
        if (!startDate.equals(NULL_VALUE) && !endDate.equals(NULL_VALUE)) {
            return validate(startDate, dateParser) && validate(endDate, dateParser);
        } else if (!endDate.equals(NULL_VALUE)) {
            return validate(endDate, dateParser);
        } else {
            return validate(startDate, dateParser);
        }
    }

    private boolean validate(String date, PrettyTimeParser dateParser) {
        return Pattern.compile(DEADLINE_VALIDATION_REGEX).matcher(date).find()
                && !dateParser.parseSyntax(date).isEmpty();
    }

    public Long getNotificationPeriod() {
        return notificationPeriod;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Date getNotificationDate() {
        return new Date(startDate.getTime() - notificationPeriod);
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setNotificationPeriod(Long notificationPeriod) {
        this.notificationPeriod = notificationPeriod;
    }

    // Empty command
    public void setNotificationPeriod(String notificationPeriod) {
    }

    /**
     * Gets notification period from user-given arguments Will only accept days
     * and weeks else will return default notification period
     *
     * @param notificationPeriod
     * @return Long time in milliseconds
     */
    private Long parseNotificationPeriod(String notificationPeriod) {
        Matcher integerParser = Pattern.compile("\\d*").matcher(notificationPeriod);
        assert (integerParser.find());
        integerParser.find();
        int period = Integer.parseInt(integerParser.group().trim());
        if (notificationPeriod.toLowerCase().contains("day")) {
            return DAY_TO_MILLIS * period;
        } else if (notificationPeriod.toLowerCase().contains("week")) {
            return WEEK_TO_MILLIS * period;
        }
        return Config.getDefaultNotificationPeriod();
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
        if (startDate.equals(endDate)) {
            return "Deadline: " + dateFormat.format(startDate) + "\nReminder on: "
                    + dateFormat.format(getNotificationDate()) + "\n";
        }
        return "Starts: " + dateFormat.format(startDate) + "\nEnds: " + dateFormat.format(endDate) + "\nReminder on: "
                + dateFormat.format(getNotificationDate()) + "\n";
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Deadline // instanceof handles nulls
                        && this.toString().equals(((Deadline) other).toString())); // state check
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

}
