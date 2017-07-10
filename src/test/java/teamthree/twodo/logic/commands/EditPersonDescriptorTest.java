package teamthree.twodo.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static teamthree.twodo.testutil.EditCommandTestUtil.DESC_CSMOD;
import static teamthree.twodo.testutil.EditCommandTestUtil.DESC_EVENT;
import static teamthree.twodo.testutil.EditCommandTestUtil.VALID_DESCRIPTION_EVENT;
import static teamthree.twodo.testutil.EditCommandTestUtil.VALID_NAME_EVENT;
import static teamthree.twodo.testutil.EditCommandTestUtil.VALID_START_DATE;
import static teamthree.twodo.testutil.EditCommandTestUtil.VALID_TAG_WORK;

import org.junit.Test;

import teamthree.twodo.logic.commands.EditCommand.EditTaskDescriptor;
import teamthree.twodo.testutil.EditTaskDescriptorBuilder;

public class EditPersonDescriptorTest {

    @Test
    public void equals() throws Exception {
        // same values -> returns true
        EditTaskDescriptor descriptorWithSameValues = new EditTaskDescriptor(DESC_CSMOD);
        assertTrue(DESC_CSMOD.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_CSMOD.equals(DESC_CSMOD));

        // null -> returns false
        assertFalse(DESC_CSMOD.equals(null));

        // different types -> returns false
        assertFalse(DESC_CSMOD.equals(5));

        // different values -> returns false
        assertFalse(DESC_CSMOD.equals(DESC_EVENT));

        // different name -> returns false
        EditTaskDescriptor editedAmy = new EditTaskDescriptorBuilder(DESC_CSMOD).withName(VALID_NAME_EVENT).build();
        assertFalse(DESC_CSMOD.equals(editedAmy));

        // different phone -> returns false
        editedAmy = new EditTaskDescriptorBuilder(DESC_CSMOD).withStartDeadline(VALID_START_DATE).build();
        assertFalse(DESC_CSMOD.equals(editedAmy));

        // different email -> returns false
        editedAmy = new EditTaskDescriptorBuilder(DESC_CSMOD).build();
        assertFalse(DESC_CSMOD.equals(editedAmy));

        // different address -> returns false
        editedAmy = new EditTaskDescriptorBuilder(DESC_CSMOD).withDescription(VALID_DESCRIPTION_EVENT).build();
        assertFalse(DESC_CSMOD.equals(editedAmy));

        // different tags -> returns false
        editedAmy = new EditTaskDescriptorBuilder(DESC_CSMOD).withTags(VALID_TAG_WORK).build();
        assertFalse(DESC_CSMOD.equals(editedAmy));
    }
}
