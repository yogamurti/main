package teamthree.twodo.logic.parser;

import static teamthree.twodo.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import teamthree.twodo.commons.core.index.Index;
import teamthree.twodo.commons.exceptions.IllegalValueException;
import teamthree.twodo.logic.commands.DeleteCommand;
import teamthree.twodo.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns an DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new DeleteCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }
    }

}
