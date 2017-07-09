package teamthree.twodo.logic.parser;

import static teamthree.twodo.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.io.File;

import teamthree.twodo.logic.commands.SaveCommand;
import teamthree.twodo.logic.parser.exceptions.ParseException;

public class SaveCommandParser {
    /**
     * Parses input arguments and creates a new SaveCommand object
     *
     * @throws ParseException
     *             if the user input does not conform to the expected format
     */
    public SaveCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        File file = new File(trimmedArgs);
        if (!file.isDirectory() || !file.exists()) {
            return new SaveCommand(args.trim());
        } else {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SaveCommand.MESSAGE_USAGE));
        }
    }

}
