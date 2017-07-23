package teamthree.twodo.logic.parser;

import teamthree.twodo.logic.commands.Command;
import teamthree.twodo.logic.parser.exceptions.ParseException;

//@@author A0107433N
/**
 * API of CommandParsers
 */
public interface CommandParser {

    /**
     * Parses the command and returns the result.
     * @param args The extra arguments after the command word entered by the user.
     * @return the command object.
     * @throws ParseException If the user input does not conform the expected format.
     */
    public Command parse(String args) throws ParseException;

}
