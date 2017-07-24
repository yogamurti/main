package guitests.guihandles;

import guitests.GuiRobot;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import teamthree.twodo.TestApp;
import teamthree.twodo.model.category.Category;
import teamthree.twodo.model.category.CategoryManager;

public class CategoryListPanelHandle extends GuiHandle {
    public static final String CARD_PANE_ID = "#cardPane";
    private static final String CATEGORY_LIST_VIEW_ID = "#categoryListView";
    public CategoryListPanelHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage, TestApp.APP_TITLE);
    }
    public ListView<Category> getListView() {
        return getNode(CATEGORY_LIST_VIEW_ID);
    }
    /**
     * Returns true if the list is showing the categories details correctly.
     *
     * @param catMan
     *            A list of person in the correct order.
     */
    public boolean isListMatching(CategoryManager catMan) {
        return catMan.getCategoryList().equals(getListView().getItems());
    }
}
