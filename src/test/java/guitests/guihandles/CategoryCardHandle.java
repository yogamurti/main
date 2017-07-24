package guitests.guihandles;

import guitests.GuiRobot;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class CategoryCardHandle extends GuiHandle {
    private static final String NAME_FIELD_ID = "#name";
    private static final String NUMBER_FIELD_ID = "#number";
    private Node node;

    public CategoryCardHandle(GuiRobot guiRobot, Stage primaryStage, Node node) {
        super(guiRobot, primaryStage, null);
        this.node = node;
    }
    protected String getTextFromLabel(String fieldId) {
        return getTextFromLabel(fieldId, node);
    }
    public String getFullName() {
        return getTextFromLabel(NAME_FIELD_ID);
    }
    private Region getNumberContainer() {
        return guiRobot.from(node).lookup(NUMBER_FIELD_ID).query();
    }
    public String getNumber() {
        return getNumberContainer().getChildrenUnmodifiable().get(0).getAccessibleText();
    }
}
