package teamthree.twodo.model.category;

/**
 * Class which defines the functionality of a category Category is mainly
 * functionality for maintaining the category list UI component.
 */
public class Category {
    private String name;
    private int numberOfConstituents;

    public Category(String name, int numberOfConstituents) {
        setName(name);
        setNumberOfConstituents(numberOfConstituents);
    }

    // Returns the name of the category
    public String getName() {
        return name;
    }

    // Returns the number of tasks under that category
    public int getNumberOfConstituents() {
        return numberOfConstituents;
    }

    public void setNumberOfConstituents(int number) {
        numberOfConstituents = number;
    }

    public void setName(String name) {
        this.name = name;
    }

}
