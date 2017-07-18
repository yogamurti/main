// @@author A0162253M
package teamthree.twodo.logic.commands;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import teamthree.twodo.commons.core.EventsCenter;
import teamthree.twodo.commons.events.storage.TaskBookFilePathChangedEvent;
import teamthree.twodo.logic.commands.exceptions.CommandException;

/** Saves TaskBook to the specified directory */

public class SaveCommand extends Command {

    public static final String COMMAND_WORD = "save";
    public static final String COMMAND_WORD_UNIXSTYLE = "-s";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Save the TaskList created with 2Do into"
            + "FILE_PATH/FILENAME.xml\n" + "Example: (WINDOWS) C:/Users/Desktop/2Do.xml\n"
            + " Example: (MAC) /User/Username/Desktop/2Do.xml\n";

    public static final String MESSAGE_SUCCESS = "File is successfully saved to: %1$s\n";
    public static final String MESSAGE_INVALID_PATH = "File Path %1$s is invalid\n";
    public static final String MESSAGE_FAILURE = "Failed to save file to %1$s\n";
    private static final String XML = ".xml";
    public final String filePath;

    public SaveCommand(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public CommandResult execute() throws CommandException {
        if (!isXml(filePath)) {
            throw new CommandException(String.format(MESSAGE_INVALID_PATH, filePath));
        }
        try {
            Paths.get(filePath);
            EventsCenter.getInstance().post(new TaskBookFilePathChangedEvent(filePath));
            model.saveTaskBook();
            return new CommandResult(String.format(MESSAGE_SUCCESS, filePath));
        } catch (InvalidPathException e) {
            throw new CommandException(String.format(MESSAGE_INVALID_PATH, filePath));
        }
    }

    private boolean isXml(String filePath) {
        return filePath.endsWith(XML);
    }
}
