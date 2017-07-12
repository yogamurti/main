package teamthree.twodo.logic.parser;

import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_DEADLINE_END;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_DEADLINE_START;

import java.util.stream.Stream;

import teamthree.twodo.commons.exceptions.IllegalValueException;
import teamthree.twodo.logic.commands.ListCommand;
import teamthree.twodo.logic.commands.ListCommand.AttributeInputted;
import teamthree.twodo.logic.parser.exceptions.ParseException;
import teamthree.twodo.model.task.Deadline;

public class ListCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * ListCommand and returns an ListCommand object for execution.
     * @throws IllegalValueException
     */
    public ListCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_DEADLINE_START, PREFIX_DEADLINE_END);
        if (!arePrefixesPresent(argMultimap, PREFIX_DEADLINE_START)
                && !arePrefixesPresent(argMultimap, PREFIX_DEADLINE_END)) {
            return new ListCommand();
        }
        try {
            Deadline deadline = ParserUtil.parseDeadlineForList(argMultimap.getValue(PREFIX_DEADLINE_START),
                    argMultimap.getValue(PREFIX_DEADLINE_END)).get();
            AttributeInputted attInput;
            if (argMultimap.getValue(PREFIX_DEADLINE_START).isPresent()
                    && argMultimap.getValue(PREFIX_DEADLINE_END).isPresent()) {
                attInput = AttributeInputted.BOTH;
            } else {
                if (argMultimap.getValue(PREFIX_DEADLINE_START).isPresent()) {
                    attInput = AttributeInputted.START;
                } else {
                    attInput = AttributeInputted.END;
                }
            }
            return new ListCommand(deadline, attInput);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional}
     * values in the given {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
