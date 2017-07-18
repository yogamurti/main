package teamthree.twodo.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import teamthree.twodo.model.category.Category;

//@@author A0124399W
public class CategoryCard extends UiPart<Region> {
    private static final String FXML = "CategoryCard.fxml";

    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private FlowPane number;

    public CategoryCard(Category category, int displayedIndex) {
        super(FXML);
        name.setText(category.getName());
        id.setText(displayedIndex + ". ");
        number.getChildren().add(new Label(category.getNumberOfConstituents().toString()));
        //Need to reduce width of flowpane to fit it in the same line
        number.setPrefWidth(25.0);
    }

}
