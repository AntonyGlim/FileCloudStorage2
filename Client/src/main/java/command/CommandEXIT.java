package command;

import client.DBManager;
import common.ConsoleHelper;


/**
 * The command is executed at the end of the program.
 */
public class CommandEXIT implements Command {

    private static String tableName = "fileslist";
    DBManager dbManager = new DBManager();

    public void execute() throws Exception {

        try {
            dbManager.connect();
            dbManager.deleteAllFromTable(tableName);
        }
        ConsoleHelper.writeMessage("До встречи!");
    }
}
