package teamthree.twodo.logic.parser;

import static teamthree.twodo.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import teamthree.twodo.logic.commands.AddCommand;
import teamthree.twodo.logic.commands.ClearCommand;
import teamthree.twodo.logic.commands.DeleteCommand;
import teamthree.twodo.logic.commands.EditCommand;
import teamthree.twodo.logic.commands.ExitCommand;
import teamthree.twodo.logic.commands.FindCommand;
import teamthree.twodo.logic.commands.HelpCommand;
import teamthree.twodo.logic.commands.HistoryCommand;
import teamthree.twodo.logic.commands.ListCommand;
import teamthree.twodo.logic.commands.LoadCommand;
import teamthree.twodo.logic.commands.MarkCommand;
import teamthree.twodo.logic.commands.OptionsCommand;
import teamthree.twodo.logic.commands.RedoCommand;
import teamthree.twodo.logic.commands.SaveCommand;
import teamthree.twodo.logic.commands.UndoCommand;
import teamthree.twodo.logic.commands.UnmarkCommand;
import teamthree.twodo.logic.parser.exceptions.ParseException;

//@@author A0107433N
// Parses input arguments and creates a new HelpCommand object
public class HelpCommandParser implements CommandParser {

    public HelpCommand parse(String args) throws ParseException {
        switch (args.trim()) {
        case AddCommand.COMMAND_WORD:
        case AddCommand.COMMAND_WORD_QUICK:
        case AddCommand.COMMAND_WORD_FAST:
            return new HelpCommand(AddCommand.MESSAGE_USAGE);
        case AddCommand.COMMAND_WORD_TAG:
            return new HelpCommand(AddCommand.MESSAGE_USAGE_TAG);
        case ClearCommand.COMMAND_WORD:
        case ClearCommand.COMMAND_WORD_FAST:
            return new HelpCommand(ClearCommand.MESSAGE_USAGE);
        case EditCommand.COMMAND_WORD:
        case EditCommand.COMMAND_WORD_FAST:
            return new HelpCommand(EditCommand.MESSAGE_USAGE);
        case DeleteCommand.COMMAND_WORD:
        case DeleteCommand.COMMAND_WORD_QUICK:
        case DeleteCommand.COMMAND_WORD_SHORT:
        case DeleteCommand.COMMAND_WORD_FAST:
            return new HelpCommand(DeleteCommand.MESSAGE_USAGE);
        case ExitCommand.COMMAND_WORD:
        case ExitCommand.COMMAND_WORD_FAST:
        case ExitCommand.COMMAND_WORD_SECOND:
        case ExitCommand.COMMAND_WORD_FAST_SECOND:
            return new HelpCommand(ExitCommand.MESSAGE_USAGE);
        case HistoryCommand.COMMAND_WORD:
            return new HelpCommand(HistoryCommand.MESSAGE_USAGE);
        case LoadCommand.COMMAND_WORD:
            return new HelpCommand(LoadCommand.MESSAGE_USAGE);
        case ListCommand.COMMAND_WORD:
        case ListCommand.COMMAND_WORD_FAST:
            return new HelpCommand(ListCommand.MESSAGE_USAGE);
        case FindCommand.COMMAND_WORD:
        case FindCommand.COMMAND_WORD_FAST:
            return new HelpCommand(FindCommand.MESSAGE_USAGE);
        case FindCommand.COMMAND_WORD_HISTORY:
            return new HelpCommand(HelpCommand.MESSAGE_HISTORY_USAGE);
        case ListCommand.COMMAND_WORD_FLOATING:
            return new HelpCommand(HelpCommand.MESSAGE_FLOATING_USAGE);
        case MarkCommand.COMMAND_WORD:
        case MarkCommand.COMMAND_WORD_FAST:
            return new HelpCommand(MarkCommand.MESSAGE_USAGE);
        case OptionsCommand.COMMAND_WORD:
        case OptionsCommand.COMMAND_WORD_SECOND:
        case OptionsCommand.COMMAND_WORD_FAST:
            return new HelpCommand(OptionsCommand.MESSAGE_USAGE);
        case RedoCommand.COMMAND_WORD:
        case RedoCommand.COMMAND_WORD_FAST:
            return new HelpCommand(RedoCommand.MESSAGE_USAGE);
        case SaveCommand.COMMAND_WORD:
        case SaveCommand.COMMAND_WORD_FAST:
            return new HelpCommand(SaveCommand.MESSAGE_USAGE);
        case UndoCommand.COMMAND_WORD:
        case UndoCommand.COMMAND_WORD_FAST:
            return new HelpCommand(UndoCommand.MESSAGE_USAGE);
        case UnmarkCommand.COMMAND_WORD:
        case UnmarkCommand.COMMAND_WORD_FAST:
            return new HelpCommand(UnmarkCommand.MESSAGE_USAGE);
        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }

    }

}
