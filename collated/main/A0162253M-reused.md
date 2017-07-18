# A0162253M-reused
###### \java\teamthree\twodo\logic\LogicManager.java
``` java
    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        try {
            logger.info("----------------[USER COMMAND][" + commandText + "]");
            Command command = parser.parseCommand(commandText);
            command.setData(model, history, undoHistory);
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

    public CommandHistory getCommandHistory() {
        return history;
    }

    public void setModel(Model model) {
        this.model = model;
    }
}
```
