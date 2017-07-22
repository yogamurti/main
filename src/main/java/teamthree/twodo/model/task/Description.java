package teamthree.twodo.model.task;

import static java.util.Objects.requireNonNull;

import teamthree.twodo.commons.exceptions.IllegalValueException;

/**
 * Represents a Task's note in the TaskList.
 * Guarantees: immutable; is valid as declared in {@link #isValidDescription(String)}
 */
public class Description {

    public static final String MESSAGE_DESCRIPTION_CONSTRAINTS =
            "Task description can take any value, and it can be blank";

    public static final String DESCRIPTION_VALIDATION_REGEX = "[^\\s].*";

    public final String value;

    /**
     * Validates if a given string is a valid description
     *
     * @throws IllegalValueException if given address string is invalid.
     */
    public Description(String description) throws IllegalValueException {
        requireNonNull(description);
        if (!isValidDescription(description)) {
            value = "No description.";
        } else {
            this.value = description;
        }
    }

    /**
     * Returns true if a given string is a valid description.
     */
    public static boolean isValidDescription(String test) {
        return test.matches(DESCRIPTION_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value + "\n";
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Description // instanceof handles nulls
                && this.value.equals(((Description) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
