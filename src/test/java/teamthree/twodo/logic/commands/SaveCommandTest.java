package teamthree.twodo.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.common.eventbus.Subscribe;

import teamthree.twodo.commons.core.EventsCenter;
import teamthree.twodo.commons.events.storage.TaskListFilePathChangedEvent;
import teamthree.twodo.logic.commands.exceptions.CommandException;
import teamthree.twodo.model.Model;
import teamthree.twodo.model.ModelManager;
import teamthree.twodo.model.UserPrefs;
import teamthree.twodo.testutil.TypicalTask;

//@@author A0162253M
public class SaveCommandTest {

    private static final String VALID_FILEPATH = "data/2Do.xml";
    private static final String INVALID_FILEPATH = "data/2Do.txt";

    private Model model = new ModelManager(new TypicalTask().getTypicalTaskList(), new UserPrefs());

    private boolean isEventCaught = false;

    @Subscribe
    private void handleTaskListFilePathChangedEvent(TaskListFilePathChangedEvent e) {
        isEventCaught = true;
    }

    @Before
    public void setUp() {
        EventsCenter.getInstance().registerHandler(this);
    }

    @Test
    public void excecute_save_success() throws CommandException {
        SaveCommand saveCommand = new SaveCommand(VALID_FILEPATH);
        saveCommand.setData(model, null, null);
        CommandResult result = saveCommand.execute();
        assertEquals(String.format(SaveCommand.MESSAGE_SUCCESS , VALID_FILEPATH), result.feedbackToUser);
        assertTrue(isEventCaught);
    }

    @Test
    public void excecute_save_throwCommandException() throws CommandException {
        try {
            SaveCommand saveCommand = new SaveCommand(INVALID_FILEPATH);
            saveCommand.setData(model, null, null);
            saveCommand.execute();
        } catch (CommandException e) {
            assertEquals(String.format(String.format(SaveCommand.MESSAGE_INVALID_PATH, INVALID_FILEPATH)),
                e.getMessage());
            assertFalse(isEventCaught);
        }
    }
}

