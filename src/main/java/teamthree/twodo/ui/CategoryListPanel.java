package teamthree.twodo.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import teamthree.twodo.model.category.Category;

//@@author A0124399W
/**
 * Panel containing the list of categories.
 */
public class CategoryListPanel extends UiPart<Region> {
    private static final String FXML = "CategoryListPanel.fxml";

    @FXML
    private ListView<Category> categoryListView;

    public CategoryListPanel(ObservableList<Category> catList) {
        super(FXML);
        setConnections(catList);
    }
    private void setConnections(ObservableList<Category> catList) {
        categoryListView.setItems(catList);
        categoryListView.setCellFactory(listView -> new CatListViewCell());
    }
    class CatListViewCell extends ListCell<Category> {

        @Override
        protected void updateItem(Category category, boolean empty) {
            super.updateItem(category, empty);

            if (empty || category == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new CategoryCard(category, getIndex() + 1).getRoot());
            }
        }
    }
}
