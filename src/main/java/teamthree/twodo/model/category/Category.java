package teamthree.twodo.model.category;

/**
 * Category is a definition of a task. It consists of the general categories of
 * complete, incomplete, floating, deadline and others (defined by user's tags).
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
    public Integer getNumberOfConstituents() {
        return numberOfConstituents;
    }

    public void setNumberOfConstituents(int number) {
        numberOfConstituents = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Category)) {
            return false;
        }

        return name.equals(((Category) other).getName());
    }

}
