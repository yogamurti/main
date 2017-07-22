package teamthree.twodo.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static teamthree.twodo.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static teamthree.twodo.testutil.TypicalTask.INDEX_FIRST_TASK;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import teamthree.twodo.commons.exceptions.IllegalValueException;
import teamthree.twodo.model.tag.Tag;
import teamthree.twodo.model.task.Description;

public class ParserUtilTest {
    private static final String INVALID_DESCRIPTION = " ";
    private static final String INVALID_TAG = "#friend";

    //    private static final String VALID_PHONE = "123456";
    private static final String VALID_DESCRIPTION = "O$P$";

    private static final String VALID_TAG_1 = "friend";
    private static final String VALID_TAG_2 = "neighbour";

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void parseIndexInvalidInputThrowsIllegalValueException() throws Exception {
        thrown.expect(IllegalValueException.class);
        ParserUtil.parseIndex("10 a");
    }

    @Test
    public void parseIndexOutOfRangeInputThrowsIllegalValueException() throws Exception {
        thrown.expect(IllegalValueException.class);
        thrown.expectMessage(MESSAGE_INVALID_INDEX);
        ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1));
    }

    @Test
    public void parseIndexValidInputSuccess() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_TASK, ParserUtil.parseIndex("1"));
        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_TASK, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseNameNullThrowsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        ParserUtil.parseName(null);
    }

    @Test
    public void parseNameOptionalEmptyReturnsOptionalEmpty() throws Exception {
        assertFalse(ParserUtil.parseName(Optional.empty()).isPresent());
    }

    @Test
    public void parseDeadlineForAddNullThrowsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        ParserUtil.parseDeadlineForAdd(null, null, null);
    }

    @Test
    public void parseDescriptionNullThrowsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        ParserUtil.parseDescription(null);
    }

    @Test
    public void parseDescriptionOptionalEmptyReturnsOptionalEmpty() throws Exception {
        Optional<Description> actualDescription = ParserUtil.parseDescription(Optional.empty());
        assertFalse(actualDescription.isPresent());
    }

    @Test
    public void parseDescriptionInvalidValueReturnsNoDescription() throws Exception {
        Description expectedAddress = new Description("No description.");
        Optional<Description> actualAddress = ParserUtil.parseDescription(Optional.of(INVALID_DESCRIPTION));

        assertEquals(expectedAddress, actualAddress.get());
    }

    @Test
    public void parseDescriptionValidValueReturnsDescription() throws Exception {
        Description expectedAddress = new Description(VALID_DESCRIPTION);
        Optional<Description> actualAddress = ParserUtil.parseDescription(Optional.of(VALID_DESCRIPTION));

        assertEquals(expectedAddress, actualAddress.get());
    }

    @Test
    public void parseTagsNullThrowsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        ParserUtil.parseTags(null);
    }

    @Test
    public void parseTagsCollectionWithInvalidTagsThrowsIllegalValueException() throws Exception {
        thrown.expect(IllegalValueException.class);
        ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, INVALID_TAG));
    }

    @Test
    public void parseTagsEmptyCollectionReturnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseTagsCollectionWithValidTagsReturnsTagSet() throws Exception {
        Set<Tag> actualTagSet = ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2));
        Set<Tag> expectedTagSet = new HashSet<Tag>(Arrays.asList(new Tag(VALID_TAG_1), new Tag(VALID_TAG_2)));

        assertEquals(expectedTagSet, actualTagSet);
    }
}
