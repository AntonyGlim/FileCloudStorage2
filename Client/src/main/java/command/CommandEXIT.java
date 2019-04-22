package command;

import client.DBManager;
import client.FileProperties;
import client.FilesList;
import common.ConsoleHelper;
import exception.PathIsNotFoundException;

import java.sql.SQLException;


/**
 * The command is executed at the end of the program.
 */
public class CommandEXIT extends CommandClientOnly {

    private static String tableName = "fileslist";


    public void execute() throws Exception {
        try {
            FilesList filesList = getFilesList();
            DBManager dbManager = new DBManager();
            try {
                dbManager.connect();
                dbManager.deleteAllFromTable(tableName); //перед каждым сохранением удаляем все из БД
                if (filesList.size() > 0){
                    for (FileProperties file : filesList.getFileList()) {
                        dbManager.insertIntoTable(
                                tableName,
                                file.getName(),
                                file.getSize(),
                                file.getAbsolutePath().toString(),
                                file.getTimeWhenAdd().getTime()
                        );
                    }
                    ConsoleHelper.writeMessage("Список файлов сохранен.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                ConsoleHelper.writeMessage("Ошибка БД");
            } finally {
                dbManager.disconnect();
            }
        } catch (PathIsNotFoundException e) {
            ConsoleHelper.writeMessage("Файл не был найден.");
        }
        ConsoleHelper.writeMessage("До встречи!");
    }
}