package client;

import exception.PathIsNotFoundException;

import java.nio.file.Paths;
import java.sql.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static client.FilesList.getFilesList;

/**
 * Класс будет отвечать за работу с базой данных
 */
public class DBManager {
    private static Connection connection;
    private static Statement statement;
    private static String tableName = "fileslist";                                                 //Имя таблицы вынесенео, для удобства
    private static String[] tokens = {" ", " ", " ", " "};                                              //Костыль, который защищает программу от вылета, если пользователь введет не правильные запросы

    /**
     * Устанавливаем соединение с БД
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:Client/src/main/java/database/files.db");
        statement = connection.createStatement();
    }

    /**
     * Закрываем соединение с БД
     */
    public static void disconnect(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод добавляет запись в таблицу
     * @param tableName
     * @param name
     * @param size
     * @param absolutePath
     * @param timeWhenAdd
     * @throws SQLException
     */
    public static void insertIntoTable(String tableName, String name, long size, String absolutePath, long timeWhenAdd) throws SQLException {
        String sql = String.format("INSERT INTO %s (name, size, absolutePath, timeWhenAdd) " +
                "VALUES ('%s', '%d', '%s', '%d');", tableName, name, size, absolutePath, timeWhenAdd);
        statement.execute(sql);
    }

    /**
     * Метод вернет FilesList, который сформирует из БД.
     * @throws SQLException
     */
    public static FilesList returnFilesListFromDB() throws SQLException, PathIsNotFoundException {
        String sql = String.format("SELECT * FROM %s;", tableName);
        ResultSet rs = statement.executeQuery(sql);
        FilesList filesList = getFilesList();
        while (rs.next()){
            filesList.addFile(new FileProperties(
                    rs.getString(1),
                    rs.getLong(2),
                    Paths.get(rs.getString(3)),
                    new Date(rs.getLong(2))
            ));
        }
        return filesList;
    }












    /**
     * Метод удаляет все данные из таблицы - метод для отладки
     * @param tableName
     * @throws SQLException
     */
    public static void deleteAllFromTable(String tableName) throws SQLException {
        String sql = String.format("DELETE FROM %s ", tableName);
        statement.execute(sql);
    }

    /**
     * Метод возвращает стоимость товара по его имени.
     * Если ResultSet не будет иметь хотябы одного элемента
     * Пользователь увидит сообщение, что товара с таким именем нет
     * @param tableName
     * @param titleToFined
     * @throws SQLException
     */
    public static void returnCostByName(String tableName, String titleToFined) throws SQLException {
        String sql = String.format("SELECT cost " +
                "FROM %s WHERE title = '%s';", tableName, titleToFined);
        ResultSet rs = statement.executeQuery(sql);
        if (rs.next()) {
            int cost = rs.getInt("cost");
            System.out.println("Стоимость товара: " + cost + "р.");
        } else {
            System.out.println("Такого товара нет.");
        }
    }

    /**
     * Метод обновит значение цены по имени.
     * Если пользователь введет не существующее имя - вернется предупреждение
     * @param tableName
     * @param titleToUbdateCost
     * @param newCost
     * @throws SQLException
     */
    public static void updateCostByName(String tableName, String titleToUbdateCost, int newCost) throws SQLException {
        String sql = String.format("UPDATE %s SET cost = '%d' WHERE title = '%s';", tableName, newCost, titleToUbdateCost);
        int count = statement.executeUpdate(sql);
        if (count > 0){
            System.out.println("Стоимость " + titleToUbdateCost + " изменена.");
        } else {
            System.out.println("К сожалению, обновления не произошло! Проверьте имя товара.");
        }
    }



    /**
     * Метод проверит, является-ли строка введенная пользователем положительным числом
     * @param str
     * @return
     */
    private static boolean isValidNumber(String str){
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(str);
        return m.matches();
    }

}
