package teamthree.twodo.model.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AddressTest {

    @Test
    public void isValidAddress() {
        // invalid addresses
        assertFalse(Note.isValidAddress("")); // empty string
        assertFalse(Note.isValidAddress(" ")); // spaces only

        // valid addresses
        assertTrue(Note.isValidAddress("Blk 456, Den Road, #01-355"));
        assertTrue(Note.isValidAddress("-")); // one character
        assertTrue(Note.isValidAddress("Leng Inc; 1234 Market St; San Francisco CA 2349879; USA")); // long address
    }
}
