package teamthree.twodo.ui;

import java.net.URL;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import teamthree.twodo.model.category.Category;

public class CategoryListPanel extends UiPart {
    private static final String FXML = "CategoryListPanel.fxml";

    @FXML
    private ListView<Category> categoryListView;
    public CategoryListPanel(URL fxmlFileUrl) {
        super(fxmlFileUrl);
        // TODO Auto-generated constructor stub
    }

    public CategoryListPanel(String fxmlFileName) {
        super(fxmlFileName);
        // TODO Auto-generated constructor stub
    }

}
