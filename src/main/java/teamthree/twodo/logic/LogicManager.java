package teamthree.twodo.logic;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import teamthree.twodo.commons.core.ComponentManager;
import teamthree.twodo.commons.core.LogsCenter;
import teamthree.twodo.logic.commands.Command;
import teamthree.twodo.logic.commands.CommandResult;
import teamthree.twodo.logic.commands.exceptions.CommandException;
import teamthree.twodo.logic.parser.Parser;
import teamthree.twodo.logic.parser.exceptions.ParseException;
import teamthree.twodo.model.Model;
import teamthree.twodo.model.task.ReadOnlyTask;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final CommandHistory history;
    private final Parser parser;

    public LogicManager(Model model) {
        this.model = model;
        this.history = new CommandHistory();
        this.parser = new Parser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        try {
            logger.info("----------------[USER COMMAND][" + commandText + "]");
            Command command = parser.parseCommand(commandText);
            command.setData(model, history);
            return command.execute();
        } finally {
            history.addToUserInputHistory(commandText);
        }
    }

    @Override
    public ObservableList<ReadOnlyTask> getFilteredTaskList() {
        return model.getFilteredTaskList();
    }
}
