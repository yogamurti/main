# A0162253M-reused
###### \java\teamthree\twodo\commons\core\ConfigTest.java
``` java
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
```
