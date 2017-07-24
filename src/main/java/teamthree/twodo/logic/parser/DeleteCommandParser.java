package teamthree.twodo.logic.parser;

import static teamthree.twodo.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_CATEGORY;

import teamthree.twodo.commons.core.index.Index;
import teamthree.twodo.commons.exceptions.IllegalValueException;
import teamthree.twodo.logic.commands.DeleteCommand;
import teamthree.twodo.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements CommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * DeleteCommand and returns an DeleteCommand object for execution.
     *
     * @throws ParseException
     *             if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        try {
            String[] splitArgs;
            Index index;
            if ((splitArgs = args.trim().split(" ")).length > 1
                    && splitArgs[0].trim().equals(PREFIX_CATEGORY.toString())) {
                index = ParserUtil.parseIndex(splitArgs[1]);
                return new DeleteCommand(index, true);
            } else {
                index = ParserUtil.parseIndex(args);
                return new DeleteCommand(index, false);
            }
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }
    }

}
