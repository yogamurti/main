package guitests.guihandles;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import guitests.GuiRobot;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import teamthree.twodo.model.tag.Tag;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.TaskWithDeadline;

/**
 * Provides a handle to a person card in the person list panel.
 */
public class TaskCardHandle extends GuiHandle {
    private static final String NAME_FIELD_ID = "#name";
    private static final String DESCRIPTION_FIELD_ID = "#description";
    private static final String DEADLINE_FIELD_ID = "#deadline";
    private static final String TAGS_FIELD_ID = "#tags";

    private Node node;

    public TaskCardHandle(GuiRobot guiRobot, Stage primaryStage, Node node) {
        super(guiRobot, primaryStage, null);
        this.node = node;
    }

    protected String getTextFromLabel(String fieldId) {
        return getTextFromLabel(fieldId, node);
    }

    public String getFullName() {
        return getTextFromLabel(NAME_FIELD_ID);
    }

    public String getDescription() {
        return getTextFromLabel(DESCRIPTION_FIELD_ID);
    }

    public String getDeadline() {
        return getTextFromLabel(DEADLINE_FIELD_ID);
    }

    public List<String> getTags() {
        return getTags(getTagsContainer());
    }

    private List<String> getTags(Region tagsContainer) {
        return tagsContainer.getChildrenUnmodifiable().stream().map(node -> ((Labeled) node).getText())
                .collect(Collectors.toList());
    }

    private List<String> getTags(Set<Tag> tags) {
        return tags.stream().map(tag -> tag.tagName).collect(Collectors.toList());
    }

    private Region getTagsContainer() {
        return guiRobot.from(node).lookup(TAGS_FIELD_ID).query();
    }

    public boolean isSamePerson(ReadOnlyTask person) {
        if (person instanceof TaskWithDeadline) {
            return getFullName().equals(person.getName().fullName)
                    && getDeadline().equals(person.getDeadline().get().toString())
                    && getDescription().equals(person.getDescription().value)
                    && getTags().equals(getTags(person.getTags()));
        } else {
            return getFullName().equals(person.getName().fullName)
                    && getDescription().equals(person.getDescription().value)
                    && getTags().equals(getTags(person.getTags()));
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TaskCardHandle) {
            TaskCardHandle handle = (TaskCardHandle) obj;
            return getFullName().equals(handle.getFullName()) && getDeadline().equals(handle.getDeadline())
                    && getDescription().equals(handle.getDescription()) && getTags().equals(handle.getTags());
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return getFullName() + " " + getDescription();
    }
}

