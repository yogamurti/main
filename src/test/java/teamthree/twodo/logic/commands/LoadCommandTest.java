package teamthree.twodo.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.common.eventbus.Subscribe;

import teamthree.twodo.commons.core.EventsCenter;
import teamthree.twodo.commons.events.logic.LoadCommandExecutedEvent;
import teamthree.twodo.logic.commands.exceptions.CommandException;
//@@author A0162253M
public class LoadCommandTest {
    private static final String VALID_FILEPATH = "data/2Do.xml";
    private static final String INVALID_FILEPATH = "data/2Do.txt";

    private boolean isEventCaught = false;

    @Subscribe
    private void handleLoadCommandExecutedEvent(LoadCommandExecutedEvent e) {
        isEventCaught = true;
    }

    @Before
    public void setUp() {
        EventsCenter.getInstance().registerHandler(this);
    }

    @Test
    public void execute_load_success() throws CommandException {
        CommandResult result = new LoadCommand(VALID_FILEPATH).execute();
        assertEquals(String.format(LoadCommand.MESSAGE_SUCCESS , VALID_FILEPATH), result.feedbackToUser);
        assertTrue(isEventCaught);
    }

    @Test
    public void excecute_load_throwCommandException() throws CommandException {
        try {
            LoadCommand loadCommand = new LoadCommand(INVALID_FILEPATH);
            loadCommand.execute();
        } catch (CommandException e) {
            assertEquals(String.format(String.format(SaveCommand.MESSAGE_INVALID_PATH, INVALID_FILEPATH)),
                e.getMessage());
            assertFalse(isEventCaught);
        }
    }
}
