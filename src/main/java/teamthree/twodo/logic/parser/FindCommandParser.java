package teamthree.twodo.logic.parser;

import static teamthree.twodo.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import teamthree.twodo.logic.commands.FindCommand;
import teamthree.twodo.logic.parser.exceptions.ParseException;

//@@author A0107433N
/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns an FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        boolean listIncomplete = !args.contains(FindCommand.COMMAND_WORD_HISTORY);
        if (!listIncomplete) {
            args = removeHistoryCommand(args);
        }
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        // keywords delimited by whitespace
        final String[] keywords = trimmedArgs.split("\\s+");
        final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));
        return new FindCommand(keywordSet, listIncomplete);
    }

    // removes history command from argument String
    private String removeHistoryCommand(String args) {
        int index = args.indexOf(FindCommand.COMMAND_WORD_HISTORY);
        return args.substring(0, index) + args.substring(index + 2, args.length());
    }
}
