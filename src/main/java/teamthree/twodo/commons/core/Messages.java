package teamthree.twodo.commons.core;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_TASK_DISPLAYED_INDEX = "The task index provided is invalid";
    public static final String MESSAGE_INVALID_TAG_DISPLAYED_INDEX = "The tag index provided is invalid";
    public static final String MESSAGE_DEFAULT_TAG_INDEX = "Default tags cannot be deleted";
    public static final String MESSAGE_TASKS_LISTED_OVERVIEW = "%1$d tasks listed!";
    public static final String MESSAGE_LOAD_FAILED = "Error in loading file. File may be corrupt or invalid.";
    public static final String MESSAGE_INVALID_DEADLINE = "The end date cannot be before the start date";
    public static final String MESSAGE_INVALID_START_ADD = "Add an end date to your current command "
            + "to add a new event task. "
            + "Use only end date to add a new deadline task.";
    public static final String MESSAGE_INVALID_NOTIFICATION_EDIT = "Add a start or end date before adding "
            + "notification period.";
    public static final String MESSAGE_INVALID_NOTIFICATION_ADD = "Add an end date before adding "
            + "notification period.";
    public static final String MESSAGE_INVALID_AUTOMARK_ARGUMENT = "Automark argument must be either true or false!";

}
