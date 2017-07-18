package teamthree.twodo.commons.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ConfigTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    //@@author A0162253M-reused
    @Test
    public void toString_defaultObject_stringReturned() {
        String defaultConfigAsString = "App title : 2Do\n"
                + "Current log level : INFO\n"
                + "Preference file Location : preferences.json\n"
                + "Task Book file Location data/2Do.xml";

        assertEquals(defaultConfigAsString, new Config().toString());
    }

    @Test
    public void equalsMethod() {
        Config defaultConfig = new Config();
        assertNotNull(defaultConfig);
        assertTrue(defaultConfig.equals(defaultConfig));
    }


}
