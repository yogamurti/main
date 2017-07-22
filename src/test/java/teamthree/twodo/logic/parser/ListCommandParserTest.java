package teamthree.twodo.logic.parser;

import static org.junit.Assert.assertTrue;
import static teamthree.twodo.logic.commands.ListCommand.COMMAND_WORD_FLOATING;
import static teamthree.twodo.logic.commands.ListCommand.COMMAND_WORD_HISTORY;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_DEADLINE_END;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_DEADLINE_START;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import teamthree.twodo.logic.commands.Command;
import teamthree.twodo.logic.commands.ListCommand;
import teamthree.twodo.logic.commands.ListCommand.AttributeInputted;
import teamthree.twodo.model.tag.Tag;
import teamthree.twodo.model.task.Deadline;

//author A0107433N
/**
 * Tests parser() in ListCommandParser for different inputs;
 */
public class ListCommandParserTest {

    public static final String VALID_START_DATE = "last week 10am";
    public static final String VALID_END_DATE = "next week 10am";
    public static final String VALID_TAG_NAME = "project";

    public static final String VALID_EMPTY = "";
    public static final String VALID_FLOAT = COMMAND_WORD_FLOATING;
    public static final String VALID_HISTORY = COMMAND_WORD_HISTORY;
    public static final String VALID_FLOAT_HISTORY = VALID_FLOAT + " " + VALID_HISTORY;
    public static final String VALID_HISTORY_FLOAT = VALID_HISTORY + " " + VALID_FLOAT;
    public static final String VALID_START = " " + PREFIX_DEADLINE_START + VALID_START_DATE;
    public static final String VALID_END = " " + PREFIX_DEADLINE_END + VALID_END_DATE;
    public static final String VALID_START_END = VALID_START + " " + VALID_END;
    public static final String VALID_END_START = VALID_END + " " + VALID_START;
    public static final String VALID_TAG = " " + PREFIX_TAG + VALID_TAG_NAME;
    public static final String VALID_GIBBERISH = " zz@/ nme#24HMKA";

    private ListCommandParser parser = new ListCommandParser();
    private Set<Tag> emptyTagList = new HashSet<>();
    private ListCommand expectedCommand;

    @Test
    public void parseEmptySuccess() throws Exception {
        expectedCommand = new ListCommand(null, AttributeInputted.NONE, true, false, emptyTagList);
        assertParseSuccess(VALID_EMPTY, expectedCommand);

        expectedCommand = new ListCommand(null, AttributeInputted.NONE, true, false, emptyTagList);
        assertParseSuccess(VALID_GIBBERISH, expectedCommand);
    }

    @Test
    public void parseFloatAndHistorySuccess() throws Exception {
        expectedCommand = new ListCommand(null, AttributeInputted.NONE, true, true, emptyTagList);
        assertParseSuccess(VALID_FLOAT, expectedCommand);

        expectedCommand = new ListCommand(null, AttributeInputted.NONE, false, false, emptyTagList);
        assertParseSuccess(VALID_HISTORY, expectedCommand);

        expectedCommand = new ListCommand(null, AttributeInputted.NONE, false, true, emptyTagList);
        assertParseSuccess(VALID_FLOAT_HISTORY, expectedCommand);

        expectedCommand = new ListCommand(null, AttributeInputted.NONE, false, true, emptyTagList);
        assertParseSuccess(VALID_HISTORY_FLOAT, expectedCommand);
    }

    @Test
    public void parseDeadlineSuccess() throws Exception {
        expectedCommand = new ListCommand(new Deadline(VALID_START, Deadline.NULL_VALUE, Deadline.NULL_VALUE),
                AttributeInputted.START, true, false, emptyTagList);
        assertParseSuccess(VALID_START, expectedCommand);

        expectedCommand = new ListCommand(new Deadline(Deadline.NULL_VALUE, VALID_END, Deadline.NULL_VALUE),
                AttributeInputted.END, true, false, emptyTagList);
        assertParseSuccess(VALID_END, expectedCommand);

        expectedCommand = new ListCommand(new Deadline(VALID_START, VALID_END, Deadline.NULL_VALUE),
                AttributeInputted.BOTH, true, false, emptyTagList);
        assertParseSuccess(VALID_START_END, expectedCommand);

        expectedCommand = new ListCommand(new Deadline(VALID_START, VALID_END, Deadline.NULL_VALUE),
                AttributeInputted.BOTH, true, false, emptyTagList);
        assertParseSuccess(VALID_END_START, expectedCommand);
    }

    @Test
    public void parseTagSuccess() throws Exception {
        Set<Tag> testTagList = new HashSet<>();
        testTagList.add(new Tag(VALID_TAG_NAME));
        expectedCommand = new ListCommand(null, AttributeInputted.NONE, true, false, testTagList);
        assertParseSuccess(VALID_TAG, expectedCommand);
    }

    /**
     * Asserts the parsing of {@code userInput} is successful and the result
     * matches {@code expectedCommand}
     */
    private void assertParseSuccess(String userInput, ListCommand expectedCommand) throws Exception {
        Command command = parser.parse(userInput);
        assertTrue(expectedCommand.equals(command));
    }
}
