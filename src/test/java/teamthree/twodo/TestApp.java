package teamthree.twodo;

import java.util.function.Supplier;

import javafx.stage.Screen;
import javafx.stage.Stage;
import teamthree.twodo.commons.core.Config;
import teamthree.twodo.commons.core.GuiSettings;
import teamthree.twodo.model.ReadOnlyTaskBook;
import teamthree.twodo.model.UserPrefs;
import teamthree.twodo.storage.UserPrefsStorage;
import teamthree.twodo.storage.XmlSerializableAddressBook;
import teamthree.twodo.testutil.TestUtil;

/**
 * This class is meant to override some properties of MainApp so that it will be suited for
 * testing
 */
public class TestApp extends MainApp {

    public static final String SAVE_LOCATION_FOR_TESTING = TestUtil.getFilePathInSandboxFolder("sampleData.xml");
    public static final String APP_TITLE = "Test App";

    protected static final String DEFAULT_PREF_FILE_LOCATION_FOR_TESTING =
            TestUtil.getFilePathInSandboxFolder("pref_testing.json");
    protected static final String ADDRESS_BOOK_NAME = "Test";
    protected Supplier<ReadOnlyTaskBook> initialDataSupplier = () -> null;
    protected String saveFileLocation = SAVE_LOCATION_FOR_TESTING;

    public TestApp() {
    }

    public TestApp(Supplier<ReadOnlyTaskBook> initialDataSupplier, String saveFileLocation) {
        super();
        this.initialDataSupplier = initialDataSupplier;
        this.saveFileLocation = saveFileLocation;

        // If some initial local data has been provided, write those to the file
        if (initialDataSupplier.get() != null) {
            TestUtil.createDataFileWithData(
                    new XmlSerializableAddressBook(this.initialDataSupplier.get()),
                    this.saveFileLocation);
        }
    }

    @Override
    protected Config initConfig(String configFilePath) {
        Config config = super.initConfig(configFilePath);
        config.setAppTitle(APP_TITLE);
        config.setUserPrefsFilePath(DEFAULT_PREF_FILE_LOCATION_FOR_TESTING);
        return config;
    }

    @Override
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        UserPrefs userPrefs = super.initPrefs(storage);
        double x = Screen.getPrimary().getVisualBounds().getMinX();
        double y = Screen.getPrimary().getVisualBounds().getMinY();
        userPrefs.updateLastUsedGuiSetting(new GuiSettings(600.0, 600.0, (int) x, (int) y));
        userPrefs.setAddressBookFilePath(saveFileLocation);
        userPrefs.setAddressBookName(ADDRESS_BOOK_NAME);
        return userPrefs;
    }


    @Override
    public void start(Stage primaryStage) {
        ui.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
