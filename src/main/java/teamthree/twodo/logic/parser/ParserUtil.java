package teamthree.twodo.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import teamthree.twodo.commons.core.index.Index;
import teamthree.twodo.commons.exceptions.IllegalValueException;
import teamthree.twodo.commons.util.StringUtil;
import teamthree.twodo.model.tag.Tag;
import teamthree.twodo.model.task.Deadline;
import teamthree.twodo.model.task.Description;
import teamthree.twodo.model.task.Name;

/**
 * Contains utility methods used for parsing strings in the various *Parser
 * classes
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    public static final String MESSAGE_INSUFFICIENT_PARTS = "Number of parts must be more than 1.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException
     *             if the specified index is invalid (not non-zero unsigned
     *             integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws IllegalValueException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new IllegalValueException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * All Parse Methods below @return non-empty Optional objects
     */

    /**
     * Parses a {@code Optional<String> name} into an {@code Optional<Name>} if
     * {@code name} is present.
     */
    public static Optional<Name> parseName(Optional<String> name) throws IllegalValueException {
        requireNonNull(name);
        return name.isPresent() ? Optional.of(new Name(name.get())) : Optional.empty();
    }

    /**
     * Parses a {@code Optional<String> phone} into an {@code Optional
     * <Deadline>} if {@code phone} is present.
     */
    public static Optional<Deadline> parseDeadlineForAdd(Optional<String> startTime, Optional<String> endTime,
            Optional<String> notificationPeriod) throws IllegalValueException {
        requireNonNull(startTime);
        requireNonNull(endTime);
        requireNonNull(notificationPeriod);
        // This method should only be called when start time is specified by user
        assert (startTime.isPresent());
        String start = startTime.get();
        String end = endTime.isPresent() ? endTime.get() : startTime.get();
        String notification = notificationPeriod.isPresent() ? notificationPeriod.get()
                : Deadline.NULL_VALUE;
        return Optional.of(new Deadline(start, end, notification));
    }

    /**
     * Parses {@code Optional<String> startTime, endTime and notificationPeriod} into {@code Optional
     * <Deadline>} if they are present is present.
     */
    public static Optional<Deadline> parseDeadlineForEdit(Optional<String> startTime, Optional<String> endTime,
            Optional<String> notificationPeriod) throws IllegalValueException {
        requireNonNull(startTime);
        requireNonNull(endTime);
        requireNonNull(notificationPeriod);
        //short circuit if all are empty
        if (allNotPresent(startTime, endTime, notificationPeriod)) {
            return Optional.empty();
        }
        String start = startTime.isPresent() ? startTime.get() : Deadline.NULL_VALUE;
        String end = endTime.isPresent() ? endTime.get() : Deadline.NULL_VALUE;
        String notification = notificationPeriod.isPresent() ? notificationPeriod.get() : Deadline.NULL_VALUE;
        return Optional.of(new Deadline(start, end, notification));
    }
    /**
     *
     * Returns true if all the Optionals are empty
     *
     */
    @SafeVarargs
    private static boolean allNotPresent(Optional <String>... times) {
        for (Optional<String> time : times) {
            if (time.isPresent()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Parses a {@code Optional<String> address} into an {@code Optional
     * <Description>} if {@code address} is present.
     */
    public static Optional<Description> parseDescription(Optional<String> description) throws IllegalValueException {
        requireNonNull(description);
        return description.isPresent() ? Optional.of(new Description(description.get()))
                : Optional.empty();
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws IllegalValueException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }
        return tagSet;
    }
}
