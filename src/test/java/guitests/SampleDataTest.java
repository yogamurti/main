package guitests;

import teamthree.twodo.model.TaskList;
import teamthree.twodo.testutil.TestUtil;

public class SampleDataTest extends TaskBookGuiTest {
    @Override
    protected TaskList getInitialData() {
        // return null to force test app to load data from file only
        return null;
    }

    @Override
    protected String getDataFileLocation() {
        // return a non-existent file location to force test app to load sample data
        return TestUtil.getFilePathInSandboxFolder("SomeFileThatDoesNotExist1234567890.xml");
    }

    /*@Test
    public void addressBook_dataFileDoesNotExist_loadSampleData() throws Exception {
        commandBox.runCommand(listFloating);
        Task[] expectedList = SampleDataUtil.getSamplePersons();
        assertTrue(personListPanel.isListMatching(expectedList));
    }*/
}

