package teamthree.twodo.model.task;

import static java.util.Objects.requireNonNull;

import java.util.Date;

import teamthree.twodo.commons.exceptions.IllegalValueException;

/**
 * Represents a Task's phone number in the address book. Guarantees: immutable;
 * is valid as declared in {@link #isValidDeadline(String)}
 */
public class Deadline {

    public static final String MESSAGE_DEADLINE_CONSTRAINTS_STRICT = "Dates should be of the format MM/DD/YY,"
            + " and Time should be 24HR format";
    public static final String PHONE_VALIDATION_REGEX = "\\d{3,}";
    public static final String DATE_PARSING_REGEX = "\\d{6}"; // Date should be mmddyyyy
    public static final String TIME_PARSING_REGEX = "\\d{4}";
    public final String value;
    // Java Date contains both time and date so don't need separate Time object.
    private Date startDate;
    private Date endDate;
    private Date notificationDate;

    /**
     * Validates given phone number.
     *
     * @throws IllegalValueException
     *             if given phone string is invalid.
     */
    public Deadline(String deadline) throws IllegalValueException {
        requireNonNull(deadline);
        String trimmedDeadline = deadline.trim();
        /*
         * if (!isValidDeadline(trimmedPhone)) { throw new
         * IllegalValueException(MESSAGE_DEADLINE_CONSTRAINTS); }
         */
        this.value = trimmedDeadline;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Date getNotificationDate() {
        return notificationDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setNotificationDate(Date notificationDate) {
        this.notificationDate = notificationDate;
    }

    /**
     * Returns true if a given string is a valid person phone number.
     */
    public static boolean isValidDeadline(String test) {
        return test.matches(PHONE_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Deadline // instanceof handles nulls
                        && this.value.equals(((Deadline) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
