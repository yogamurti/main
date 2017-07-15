//@@author A0139267W
package teamthree.twodo.commons.core.options;

//Represents an auto-mark setting for tasks
public class AutoMark {

    public final String autoMark;

    public AutoMark(String autoMark) {
        this.autoMark = autoMark;
    }

    @Override
    public String toString() {
        return "Automark: " + autoMark + "\n";
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AutoMark // instanceof handles nulls
                && this.autoMark == ((AutoMark) other).autoMark); // state check
    }

}
