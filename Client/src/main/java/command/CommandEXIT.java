package command;

import common.ConsoleHelper;

/**
 * The command is executed at the end of the program.
 */
public class CommandEXIT implements Command {
    public void execute() throws Exception {
        ConsoleHelper.writeMessage("До встречи!");
    }
}
