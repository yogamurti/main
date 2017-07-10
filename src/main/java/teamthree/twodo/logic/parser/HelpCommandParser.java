package teamthree.twodo.logic.parser;

import static teamthree.twodo.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import teamthree.twodo.logic.commands.AddCommand;
import teamthree.twodo.logic.commands.DeleteCommand;
import teamthree.twodo.logic.commands.EditCommand;
import teamthree.twodo.logic.commands.HelpCommand;
import teamthree.twodo.logic.commands.SelectCommand;
import teamthree.twodo.logic.parser.exceptions.ParseException;

public class HelpCommandParser {

    public HelpCommandParser() {
    }

    public HelpCommand parse(String args) throws ParseException {
        switch (args.trim()) {
        case AddCommand.COMMAND_WORD:
            return new HelpCommand(AddCommand.MESSAGE_USAGE);
        case EditCommand.COMMAND_WORD:
            return new HelpCommand(EditCommand.MESSAGE_USAGE);
        case SelectCommand.COMMAND_WORD:
            return new HelpCommand(SelectCommand.MESSAGE_USAGE);
        case DeleteCommand.COMMAND_WORD:
            return new HelpCommand(DeleteCommand.MESSAGE_USAGE);
        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }

    }

}
