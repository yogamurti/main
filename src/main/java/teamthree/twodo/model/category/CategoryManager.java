package teamthree.twodo.model.category;

import java.util.ArrayList;
import java.util.List;

import com.google.common.eventbus.Subscribe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import teamthree.twodo.commons.core.ComponentManager;
import teamthree.twodo.commons.core.UnmodifiableObservableList;
import teamthree.twodo.commons.events.model.TaskBookChangedEvent;
import teamthree.twodo.model.Model;
import teamthree.twodo.model.tag.Tag;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.TaskWithDeadline;

//@@author A0124399W
/**
 * Manager class for maintaining the different categories in the task manager.
 */
public class CategoryManager extends ComponentManager {

    private final Model model;
    /**
     * ==============DEFAULT CATEGORIES=======================
     */
    private final Category allTasks = new Category("All", 0);
    private final Category completeTasks = new Category("Completed", 0);
    private final Category incompleteTasks = new Category("Incomplete", 0);
    private final Category floatingTasks = new Category("Floating Tasks", 0);
    private final Category tasksWithDeadline = new Category("Tasks with Deadline", 0);
    /**
     * =======================================================
     */
    private ObservableList<Category> categoryList = FXCollections.observableArrayList();
    private List<Category> defaultCategories = new ArrayList<Category>();
    private List<Category> otherCategories = new ArrayList<Category>();;

    public CategoryManager(Model model) {
        this.model = model;
        initDefaultCategories();
        updateCategoryListWithTags();
        resetCategoryList();
    }

    private void initDefaultCategories() {
        updateDefaultCategories();
        addToDefaultCategoryList(allTasks, completeTasks, incompleteTasks, floatingTasks, tasksWithDeadline);
    }

    public ObservableList<Category> getCategoryList() {
        return new UnmodifiableObservableList<Category>(categoryList);
    }

    /**
     * Resets the main category list with the default and other categories.
     */
    public synchronized void resetCategoryList() {
        categoryList.clear();
        categoryList.addAll(defaultCategories);
        categoryList.addAll(otherCategories);
    }

    /**
     * Update the other categories list with all tags in the Model.
     */
    private void updateCategoryListWithTags() {
        ArrayList<Category> tempList = new ArrayList<Category>();
        model.getTaskBook().getTagList().forEach((tag) -> {
            int numConstituents;
            if ((numConstituents = getNumberOfConstituents(tag)) > 0) {
                tempList.add(new Category(tag.tagName, numConstituents));
            }
        });
        otherCategories = tempList;
    }

    private int getNumberOfConstituents(Tag tag) {
        int numConstituents = 0;
        for (ReadOnlyTask t : model.getTaskBook().getTaskList()) {
            if (t.getTags().contains(tag)) {
                numConstituents++;
            }
        }
        return numConstituents;
    }

    // Updates all the default categories
    private void updateDefaultCategories() {
        setAllTasks();
        setCompleteTasks();
        setFloatingTasks();
    }

    private void addToDefaultCategoryList(Category... categories) {
        for (Category category : categories) {
            defaultCategories.add(category);
        }
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
    /**
     * Sets both floating and tasks with deadlines
     */
    private void setFloatingTasks() {
        int numFloating = 0;
        int numWithDeadline = 0;
        for (ReadOnlyTask t : model.getTaskBook().getTaskList()) {
            if (!(t instanceof TaskWithDeadline)) {
                numFloating++;
            } else {
                numWithDeadline++;
            }
        }
        floatingTasks.setNumberOfConstituents(numFloating);
        tasksWithDeadline.setNumberOfConstituents(numWithDeadline);
    }

    /** ========================EVENT HANDLERS========================= */
    /**
     * Updates the category list when there is a change in the taskbook.
     */
    @Subscribe
    public void handleTaskBookChangedEvent(TaskBookChangedEvent event) {
        refreshAllMainList();
    }

    //Updates all categories
    private void refreshAllMainList() {
        updateDefaultCategories();
        updateCategoryListWithTags();
        resetCategoryList();
    }

}
