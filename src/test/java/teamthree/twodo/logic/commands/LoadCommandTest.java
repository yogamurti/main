package teamthree.twodo.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.common.eventbus.Subscribe;

import teamthree.twodo.commons.core.EventsCenter;
import teamthree.twodo.commons.events.storage.TaskBookFilePathChangedEvent;
import teamthree.twodo.logic.commands.exceptions.CommandException;

public class LoadCommandTest {
    private static final String VALID_FILEPATH = "data/2Do.xml";

    private boolean isEventCaught = false;

    @Subscribe
    private void handleTaskBookFilePathChangedEvent(TaskBookFilePathChangedEvent e) {
        isEventCaught = true;
    }

    @Before
    public void setUp() {
        EventsCenter.getInstance().registerHandler(this);
    }

    @Test
    public void excecute_load_success() throws CommandException {
        CommandResult result = new LoadCommand(VALID_FILEPATH).execute();
        assertEquals(String.format(LoadCommand.MESSAGE_SUCCESS , VALID_FILEPATH), result.feedbackToUser);
        assertTrue(isEventCaught);
    }

}
