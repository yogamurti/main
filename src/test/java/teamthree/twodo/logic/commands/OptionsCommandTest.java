package teamthree.twodo.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import teamthree.twodo.automark.AutoMarkManager;
import teamthree.twodo.automark.AutoMarkManagerStud;
import teamthree.twodo.commons.core.Config;
import teamthree.twodo.commons.core.ConfigStud;
import teamthree.twodo.commons.core.options.Alarm;
import teamthree.twodo.commons.core.options.AutoMark;
import teamthree.twodo.commons.core.options.Options;
import teamthree.twodo.logic.CommandHistory;
import teamthree.twodo.model.Model;
import teamthree.twodo.model.ModelManager;
import teamthree.twodo.model.UserPrefs;
import teamthree.twodo.testutil.TypicalTask;

//@@author A0139267W
public class OptionsCommandTest {
    private static final Long DEFAULT_NOTIFICATION_PERIOD = (long) 1000 * 60 * 60 * 24;
    private static final String DEFAULT_NOTIFICATION_PERIOD_STRING = "1 day";
    private static final String VALID_ALARM_INPUT = "2 days";
    private static final boolean VALID_AUTOMARK_INPUT = true;
    private static final Options VALID_CHANGE_ALARM = new Options(
            new Alarm(VALID_ALARM_INPUT), new AutoMark(AutoMarkManager.getSetToRun()));
    private static final Options VALID_CHANGE_AUTOMARK = new Options(
            new Alarm(Config.defaultNotificationPeriodToString()), new AutoMark(VALID_AUTOMARK_INPUT));
    private static final Options VALID_CHANGE_ARGUMENTS = new Options(
            new Alarm(VALID_ALARM_INPUT), new AutoMark(VALID_AUTOMARK_INPUT));
    private static final Options SAME_AS_DEFAULT = new Options(
            new Alarm(Config.defaultNotificationPeriodToString()), new AutoMark(AutoMarkManager.getSetToRun()));

    private Model model = new ModelManager(new TypicalTask().getTypicalTaskBook(), new UserPrefs());

    @Test
    public void execute_validAlarmArgument_success() throws Exception {
        OptionsCommand optionsCommand = prepareCommand(VALID_CHANGE_ALARM);

        ConfigStud.changeDefaultNotificationPeriod(VALID_ALARM_INPUT);
        String expectedMessage = String.format(OptionsCommand.MESSAGE_UPDATE_OPTIONS_SUCCESS, VALID_CHANGE_ALARM);

        assertTrue(Config.getDefaultNotificationPeriod().equals(ConfigStud.getDefaultNotificationPeriod()) == false);
        assertTrue(Config.defaultNotificationPeriodToString().equals(ConfigStud.defaultNotificationPeriodToString())
                == false);

        CommandTestUtil.assertCommandSuccessSkeleton(optionsCommand, expectedMessage);

        assertTrue(AutoMarkManager.getSetToRun() == false);
        assertEquals(Config.getDefaultNotificationPeriod(), ConfigStud.getDefaultNotificationPeriod());
        assertEquals(Config.defaultNotificationPeriodToString(), ConfigStud.defaultNotificationPeriodToString());

        // reset to initial state
        Config.changeDefaultNotificationPeriod(DEFAULT_NOTIFICATION_PERIOD_STRING);
        ConfigStud.changeDefaultNotificationPeriod(DEFAULT_NOTIFICATION_PERIOD_STRING);
    }

    @Test
    public void execute_validAutoMarkArgument_success() throws Exception {
        OptionsCommand optionsCommand = prepareCommand(VALID_CHANGE_AUTOMARK);

        AutoMarkManagerStud.setToRun(true);
        String expectedMessage = String.format(OptionsCommand.MESSAGE_UPDATE_OPTIONS_SUCCESS, VALID_CHANGE_AUTOMARK);

        assertTrue(AutoMarkManager.getSetToRun() != AutoMarkManagerStud.getSetToRun());

        CommandTestUtil.assertCommandSuccessSkeleton(optionsCommand, expectedMessage);

        assertEquals(AutoMarkManager.getSetToRun(), AutoMarkManagerStud.getSetToRun());
        assertEquals(Config.getDefaultNotificationPeriod(), DEFAULT_NOTIFICATION_PERIOD);
        assertEquals(Config.defaultNotificationPeriodToString(), DEFAULT_NOTIFICATION_PERIOD_STRING);

        // reset to initial state
        AutoMarkManager.setToRun(false);
        AutoMarkManagerStud.setToRun(false);
    }

    @Test
    public void execute_validArguments_success() throws Exception {
        OptionsCommand optionsCommand = prepareCommand(VALID_CHANGE_ARGUMENTS);

        ConfigStud.changeDefaultNotificationPeriod(VALID_ALARM_INPUT);
        AutoMarkManagerStud.setToRun(true);
        String expectedMessage = String.format(OptionsCommand.MESSAGE_UPDATE_OPTIONS_SUCCESS, VALID_CHANGE_ARGUMENTS);

        assertTrue(Config.getDefaultNotificationPeriod().equals(ConfigStud.getDefaultNotificationPeriod()) == false);
        assertTrue(Config.defaultNotificationPeriodToString().equals(ConfigStud.defaultNotificationPeriodToString())
                == false);
        assertTrue(AutoMarkManager.getSetToRun() != AutoMarkManagerStud.getSetToRun());

        CommandTestUtil.assertCommandSuccessSkeleton(optionsCommand, expectedMessage);

        assertEquals(Config.getDefaultNotificationPeriod(), ConfigStud.getDefaultNotificationPeriod());
        assertEquals(Config.defaultNotificationPeriodToString(), ConfigStud.defaultNotificationPeriodToString());
        assertEquals(AutoMarkManager.getSetToRun(), AutoMarkManagerStud.getSetToRun());
        // reset to initial state
        Config.changeDefaultNotificationPeriod(DEFAULT_NOTIFICATION_PERIOD_STRING);
        ConfigStud.changeDefaultNotificationPeriod(DEFAULT_NOTIFICATION_PERIOD_STRING);
        AutoMarkManager.setToRun(false);
        AutoMarkManagerStud.setToRun(false);
    }

    @Test
    public void execute_sameAsDefaultOptions_failure() throws Exception {
        OptionsCommand optionsCommand = prepareCommand(SAME_AS_DEFAULT);

        String expectedMessage = String.format(OptionsCommand.MESSAGE_DUPLICATE_OPTIONS, SAME_AS_DEFAULT);

        CommandTestUtil.assertCommandFailureSkeleton(optionsCommand, expectedMessage);

        assertEquals(Config.getDefaultNotificationPeriod(), ConfigStud.getDefaultNotificationPeriod());
        assertEquals(Config.defaultNotificationPeriodToString(), ConfigStud.defaultNotificationPeriodToString());
        assertEquals(AutoMarkManager.getSetToRun(), AutoMarkManagerStud.getSetToRun());
    }

    // Returns a {@code OptionsCommand} with the parameter {@code option}
    private OptionsCommand prepareCommand(Options option) {
        OptionsCommand optionsCommand = new OptionsCommand(option);
        optionsCommand.setData(model, new CommandHistory(), null);
        return optionsCommand;
    }
}
