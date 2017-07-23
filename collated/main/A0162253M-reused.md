# A0162253M-reused
###### \java\teamthree\twodo\logic\LogicManager.java
``` java
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private Model model;
    private CategoryManager catMan;
    private final CommandHistory history;
    private final UndoCommandHistory undoHistory;
    private final Parser parser;

    public LogicManager(Model model, CategoryManager catMan) {
        this.model = model;
        this.history = new CommandHistory();
        this.parser = new Parser();
        this.undoHistory = new UndoCommandHistory();
        this.catMan = catMan;
    }
    public LogicManager(Model model) {
        this.model = model;
        this.history = new CommandHistory();
        this.parser = new Parser();
        this.undoHistory = new UndoCommandHistory();
        this.catMan = null;
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        try {
            logger.info("----------------[USER COMMAND][" + commandText + "]");
            Command command = parser.parseCommand(commandText);
            command.setData(model, history, undoHistory, catMan);
            return command.execute();
        } finally {
            history.addToUserInputHistory(commandText);
            history.add(commandText);
        }
    }

    @Override
    public ObservableList<ReadOnlyTask> getFilteredTaskList() {
        return model.getFilteredAndSortedTaskList();
    }

    @Override
    public CommandHistory getCommandHistory() {
        return history;
    }

    @Override
    public void setModel(Model model) {
        this.model = model;
    }
}
```
