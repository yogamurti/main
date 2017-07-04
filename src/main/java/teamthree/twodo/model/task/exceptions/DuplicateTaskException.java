package teamthree.twodo.model.task.exceptions;

import teamthree.twodo.commons.exceptions.DuplicateDataException;

/**
 * Signals that the operation will result in duplicate Task objects.
 */
public class DuplicateTaskException extends DuplicateDataException {
    public DuplicateTaskException() {
        super("Operation would result in duplicate persons");
    }
}
