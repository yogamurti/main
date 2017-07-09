package teamthree.twodo.testutil;

import teamthree.twodo.commons.exceptions.IllegalValueException;
import teamthree.twodo.logic.commands.EditCommand.EditTaskDescriptor;

/**
 * Utility class containing the constants required for tests related to EditCommand
 */
public class EditCommandTestUtil {
    public static final String VALID_NAME_CSMOD = "CS2103 PROJ V-1.0";
    public static final String VALID_NAME_EVENT = "Lief-Ericson Day";
    public static final String VALID_END_DATE = "sat 10am";
    public static final String VALID_START_DATE = "fri 10am";
    public static final String VALID_DESCRIPTION_MOD = "FML";
    public static final String VALID_DESCRIPTION_EVENT = "INGA-THINGA-THHURGIN";
    public static final String VALID_TAG_WORK = "PLAY";
    public static final String VALID_TAG_SPONGEBOB = "SPONGEBAAAAB!";

    public static final EditTaskDescriptor DESC_CSMOD;
    public static final EditTaskDescriptor DESC_EVENT;

    static {
        try {
            DESC_CSMOD = new EditTaskDescriptorBuilder().withName(VALID_NAME_CSMOD)
                    .withStartDeadline(VALID_START_DATE).withDescription(VALID_DESCRIPTION_MOD)
                    .withTags(VALID_TAG_SPONGEBOB).build();
            DESC_EVENT = new EditTaskDescriptorBuilder().withName(VALID_NAME_EVENT)
                    .withStartAndEndDeadline(VALID_START_DATE, VALID_END_DATE).withDescription(VALID_DESCRIPTION_EVENT)
                    .withTags(VALID_TAG_WORK, VALID_TAG_SPONGEBOB).build();
        } catch (IllegalValueException ive) {
            throw new AssertionError("Method should not fail.");
        }
    }
}
