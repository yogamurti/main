package teamthree.twodo.model.category;

import java.util.ArrayList;
import java.util.List;

import teamthree.twodo.commons.core.ComponentManager;
import teamthree.twodo.commons.core.EventsCenter;
import teamthree.twodo.model.Model;
import teamthree.twodo.model.task.ReadOnlyTask;

public class CategoryManager extends ComponentManager {

    private final Model model;
    /**
     * The following are the three main categories which will be there by
     * default
     */
    private final Category allTasks = new Category("All", 0);
    private final Category completeTasks = new Category("Completed", 0);
    private List<Category> categoryList = new ArrayList<Category>();

    public CategoryManager(Model model) {
        this.model = model;
        setAllTasks();
        setCompleteTasks();

    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    private void setAllTasks() {
        allTasks.setNumberOfConstituents(model.getTaskBook().getTaskList().size());
    }

    private void setCompleteTasks() {
        int numComplete = 0;
        for (ReadOnlyTask t : model.getTaskBook().getTaskList()) {
            if (t.isCompleted()) {
                numComplete++;
            }
        }
        completeTasks.setNumberOfConstituents(numComplete);
    }

}
