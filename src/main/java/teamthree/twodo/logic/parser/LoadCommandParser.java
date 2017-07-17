//@@author A0162253M
package teamthree.twodo.logic.parser;

import static teamthree.twodo.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.io.File;

import teamthree.twodo.logic.commands.LoadCommand;
import teamthree.twodo.logic.parser.exceptions.ParseException;

public class LoadCommandParser {
    /**
     * Parses input arguments and creates a new LoadCommand object
     *
     * @throws ParseException
     *             if the user input does not conform to the expected format
     */
    public LoadCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        File file = new File(trimmedArgs);
        if (!file.isDirectory() || !file.exists()) {
            return new LoadCommand(args.trim());
        } else {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, LoadCommand.MESSAGE_USAGE));
        }
    }

}
