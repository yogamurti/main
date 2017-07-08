package teamthree.twodo.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static teamthree.twodo.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_DEADLINE_END;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_DEADLINE_START;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_NAME;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_TAG;
import static teamthree.twodo.testutil.EditCommandTestUtil.VALID_DESCRIPTION_EVENT;
import static teamthree.twodo.testutil.EditCommandTestUtil.VALID_DESCRIPTION_MOD;
import static teamthree.twodo.testutil.EditCommandTestUtil.VALID_END_DATE;
import static teamthree.twodo.testutil.EditCommandTestUtil.VALID_NAME_CSMOD;
import static teamthree.twodo.testutil.EditCommandTestUtil.VALID_START_DATE;
import static teamthree.twodo.testutil.EditCommandTestUtil.VALID_TAG_SPONGEBOB;
import static teamthree.twodo.testutil.EditCommandTestUtil.VALID_TAG_WORK;
import static teamthree.twodo.testutil.TypicalPersons.INDEX_FIRST_PERSON;
import static teamthree.twodo.testutil.TypicalPersons.INDEX_SECOND_PERSON;
import static teamthree.twodo.testutil.TypicalPersons.INDEX_THIRD_PERSON;

import org.junit.Test;

import teamthree.twodo.commons.core.index.Index;
import teamthree.twodo.logic.commands.Command;
import teamthree.twodo.logic.commands.EditCommand;
import teamthree.twodo.logic.commands.EditCommand.EditTaskDescriptor;
import teamthree.twodo.logic.parser.exceptions.ParseException;
import teamthree.twodo.model.tag.Tag;
import teamthree.twodo.model.task.Deadline;
import teamthree.twodo.model.task.Description;
import teamthree.twodo.testutil.EditTaskDescriptorBuilder;

public class EditCommandParserTest {

    private static final String NAME_DESC_MOD = " " + PREFIX_NAME + VALID_NAME_CSMOD;
    private static final String DEADLINE_DESC_MOD = " " + PREFIX_DEADLINE_START + VALID_START_DATE;
    private static final String DEADLINE_DESC_EVENT = " " + PREFIX_DEADLINE_START + VALID_START_DATE
            + PREFIX_DEADLINE_END + VALID_END_DATE;
    private static final String ADDRESS_DESC_AMY = " " + PREFIX_DESCRIPTION + VALID_DESCRIPTION_MOD;
    private static final String ADDRESS_DESC_BOB = " " + PREFIX_DESCRIPTION + VALID_DESCRIPTION_EVENT;
    private static final String TAG_DESC_SPONGE = " " + PREFIX_TAG + VALID_TAG_SPONGEBOB;
    private static final String TAG_DESC_WORK = " " + PREFIX_TAG + VALID_TAG_WORK;
    private static final String TAG_EMPTY = " " + PREFIX_TAG;

    //Must be valid description of a date
    private static final String INVALID_DEADLINE_DESC = " " + PREFIX_DEADLINE_START + "911a";
    private static final String INVALID_DESCRIPTION = " " + PREFIX_DESCRIPTION;
    private static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "hubby*"; // '*' not allowed in tags

    private static final String MESSAGE_INVALID_FORMAT = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
            EditCommand.MESSAGE_USAGE);

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(VALID_NAME_CSMOD, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure("1", EditCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure("", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure("-5" + NAME_DESC_MOD, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure("0" + NAME_DESC_MOD, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure("1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure("1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure("1" + INVALID_DEADLINE_DESC, Deadline.MESSAGE_DEADLINE_CONSTRAINTS_STRICT); // invalid phone
        assertParseFailure("1" + INVALID_DESCRIPTION, Description.MESSAGE_DESCRIPTION_CONSTRAINTS); // invalid address
        assertParseFailure("1" + INVALID_TAG_DESC, Tag.MESSAGE_TAG_CONSTRAINTS); // invalid tag

        // valid phone followed by invalid phone. The test case for invalid phone followed by valid phone
        // is tested at {@code parse_invalidValueFollowedByValidValue_success()}
        assertParseFailure("1" + DEADLINE_DESC_EVENT + INVALID_DEADLINE_DESC,
                Deadline.MESSAGE_DEADLINE_CONSTRAINTS_STRICT);

        // while parsing {@code PREFIX_TAG} alone will reset the tags of the {@code Task} being edited,
        // parsing it together with a valid tag results in error
        assertParseFailure("1" + TAG_DESC_SPONGE + TAG_DESC_WORK + TAG_EMPTY, Tag.MESSAGE_TAG_CONSTRAINTS);
        assertParseFailure("1" + TAG_DESC_SPONGE + TAG_EMPTY + TAG_DESC_WORK, Tag.MESSAGE_TAG_CONSTRAINTS);
        assertParseFailure("1" + TAG_EMPTY + TAG_DESC_SPONGE + TAG_DESC_WORK, Tag.MESSAGE_TAG_CONSTRAINTS);

    }

    @Test
    public void parse_allFieldsSpecified_success() throws Exception {
        Index targetIndex = INDEX_SECOND_PERSON;
        String userInput = targetIndex.getOneBased() + DEADLINE_DESC_EVENT + TAG_DESC_WORK + ADDRESS_DESC_AMY
                + NAME_DESC_MOD + TAG_DESC_SPONGE;

        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withName(VALID_NAME_CSMOD)
                .withStartDeadline(VALID_START_DATE).withDescription(VALID_DESCRIPTION_MOD)
                .withTags(VALID_TAG_WORK, VALID_TAG_SPONGEBOB).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() throws Exception {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + DEADLINE_DESC_EVENT;

        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withStartDeadline(VALID_END_DATE)
                .build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() throws Exception {
        // name
        Index targetIndex = INDEX_THIRD_PERSON;
        String userInput = targetIndex.getOneBased() + NAME_DESC_MOD;
        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withName(VALID_NAME_CSMOD).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(userInput, expectedCommand);

        // phone
        userInput = targetIndex.getOneBased() + DEADLINE_DESC_MOD;
        descriptor = new EditTaskDescriptorBuilder().withStartDeadline(VALID_START_DATE).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(userInput, expectedCommand);

        // address
        userInput = targetIndex.getOneBased() + ADDRESS_DESC_AMY;
        descriptor = new EditTaskDescriptorBuilder().withDescription(VALID_DESCRIPTION_MOD).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(userInput, expectedCommand);

        // tags
        userInput = targetIndex.getOneBased() + TAG_DESC_SPONGE;
        descriptor = new EditTaskDescriptorBuilder().withTags(VALID_TAG_SPONGEBOB).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() throws Exception {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + DEADLINE_DESC_MOD + ADDRESS_DESC_AMY + TAG_DESC_SPONGE
                + DEADLINE_DESC_MOD + ADDRESS_DESC_AMY + TAG_DESC_SPONGE + DEADLINE_DESC_EVENT + ADDRESS_DESC_BOB
                + TAG_DESC_WORK;

        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withStartDeadline(VALID_START_DATE)
                .withDescription(VALID_DESCRIPTION_EVENT)
                .withTags(VALID_TAG_SPONGEBOB, VALID_TAG_WORK).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValueFollowedByValidValue_success() throws Exception {
        // no other valid values specified
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + INVALID_DEADLINE_DESC + DEADLINE_DESC_EVENT;
        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withStartDeadline(VALID_START_DATE).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(userInput, expectedCommand);

        // other valid values specified
        userInput = targetIndex.getOneBased() + INVALID_DEADLINE_DESC + ADDRESS_DESC_BOB + DEADLINE_DESC_EVENT;
        descriptor = new EditTaskDescriptorBuilder().withStartDeadline(VALID_START_DATE)
                .withDescription(VALID_DESCRIPTION_EVENT).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(userInput, expectedCommand);
    }

    @Test
    public void parse_resetTags_success() throws Exception {
        Index targetIndex = INDEX_THIRD_PERSON;
        String userInput = targetIndex.getOneBased() + TAG_EMPTY;

        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withTags().build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(userInput, expectedCommand);
    }

    /**
     * Asserts the parsing of {@code userInput} is unsuccessful and the error
     * message equals to {@code expectedMessage}
     */
    private void assertParseFailure(String userInput, String expectedMessage) {
        try {
            parser.parse(userInput);
            fail("An exception should have been thrown.");
        } catch (ParseException pe) {
            assertEquals(expectedMessage, pe.getMessage());
        }
    }

    /**
     * Asserts the parsing of {@code userInput} is successful and the result
     * matches {@code expectedCommand}
     */
    private void assertParseSuccess(String userInput, EditCommand expectedCommand) throws Exception {
        Command command = parser.parse(userInput);
        assert expectedCommand.equals(command);
    }
}
