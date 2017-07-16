package teamthree.twodo.model.category;

import java.util.ArrayList;
import java.util.List;

import teamthree.twodo.commons.core.ComponentManager;
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
    private final Category incompleteTasks = new Category("Incomplete", 0);

    private List<Category> categoryList = new ArrayList<Category>();

    public CategoryManager(Model model) {
        this.model = model;
        updateDefaultCategories();
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }
    /**
     * Update the category list with all tags in the Model 
     */
    private void updateCategoryListWithTags() {
        model.getTaskBook().getTagList().forEach((tag)->{
            for(ReadOnlyTask t: model.getTaskBook().getTaskBook()) {
                if(t.getTags().contains(tag))
            }
        });
    }

    // Updates all the default categories
    private void updateDefaultCategories() {
        setAllTasks();
        setCompleteTasks();
    }

    private void setAllTasks() {
        allTasks.setNumberOfConstituents(model.getTaskBook().getTaskList().size());
    }

    // Sets the number of complete and incomplete tasks categories
    private void setCompleteTasks() {
        int numComplete = 0;
        int numIncomplete = 0;
        for (ReadOnlyTask t : model.getTaskBook().getTaskList()) {
            if (t.isCompleted()) {
                numComplete++;
            } else {
                numIncomplete++;
            }
        }
        completeTasks.setNumberOfConstituents(numComplete);
        incompleteTasks.setNumberOfConstituents(numIncomplete);
    }

}
