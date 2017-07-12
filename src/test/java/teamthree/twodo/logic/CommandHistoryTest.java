package teamthree.twodo.logic;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import teamthree.twodo.logic.parser.exceptions.ParseException;

public class CommandHistoryTest {
    private CommandHistory history;

    @Before
    public void setUp() {
        history = new CommandHistory();
    }

    @Test
    public void add() throws ParseException {
        final String validCommand = "clear";
        final String invalidCommand = "adds Bob";

        history.addToUserInputHistory(validCommand);
        history.addToUserInputHistory(invalidCommand);
        assertEquals(Arrays.asList(validCommand, invalidCommand), history.getUserInputHistory());
    }
}
