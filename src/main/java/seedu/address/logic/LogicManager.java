package seedu.address.logic;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Person;
import seedu.address.storage.StorageManager;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final ModelManager model;
    private final Parser parser;

    public LogicManager(ModelManager model, StorageManager storage) {
        this.model = model;
        this.parser = new Parser();
    }

    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        Command command = parser.parseCommand(commandText);
        command.setData(model);
        return command.execute();
    }

    public ObservableList<Person> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }
}
