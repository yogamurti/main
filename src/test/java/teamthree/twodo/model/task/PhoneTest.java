package teamthree.twodo.model.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PhoneTest {

    @Test
    public void isValidPhone() {
        // invalid phone numbers
        assertFalse(Deadline.isValidDeadline("")); // empty string
        assertFalse(Deadline.isValidDeadline(" ")); // spaces only
        assertFalse(Deadline.isValidDeadline("91")); // less than 3 numbers
        assertFalse(Deadline.isValidDeadline("phone")); // non-numeric
        assertFalse(Deadline.isValidDeadline("9011p041")); // alphabets within digits
        assertFalse(Deadline.isValidDeadline("9312 1534")); // spaces within digits

        // valid phone numbers
        assertTrue(Deadline.isValidDeadline("911")); // exactly 3 numbers
        assertTrue(Deadline.isValidDeadline("93121534"));
        assertTrue(Deadline.isValidDeadline("124293842033123")); // long phone numbers
    }
}
