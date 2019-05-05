package database;

import common.ConsoleHelper;
import common.exception.PathIsNotFoundException;
import exeption.NoSuchUserException;
import user.User;

import java.nio.file.Paths;
import java.sql.*;
import java.util.Date;


/**
 * Класс будет отвечать за работу с базой данных
 */
public class DBManager {
    private static Connection connection;
    private static Statement statement;
    private static String tableName = "users";

    /**
     * Устанавливаем соединение с БД
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:Server/src/main/java/database/users.db");
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
     */
    public static void insertIntoTable(int name, int password, long timeWhenAdd, long timeLastVisit) throws SQLException {
        try {
            connect();
            String sql = String.format("INSERT INTO %s (name, password, registration_date, time_last_visit) " +
                    "VALUES ('%d', '%d', '%d', '%d');", tableName, name, password, timeWhenAdd, timeLastVisit);
            statement.execute(sql);
        } catch (ClassNotFoundException e) {
            ConsoleHelper.writeMessage("Ошибка при добавлении данных");
        } finally {
            disconnect();
        }
    }

    /**
     * Метод вернет FilesListManager, который сформирует из БД.
     * @throws SQLException
     */
    public static User returnUserFromDBbyNameAndPass(int name, int password) throws SQLException, NoSuchUserException {
        User user = null;
        try {
            connect();
            String sql = String.format("SELECT * FROM %s WHERE name = %d AND password = %d", tableName, name, password);
            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()) {
                user = new User(
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getLong(4),
                        rs.getLong(5)
                );
            } else {
                throw new NoSuchUserException();
            }

        } catch (ClassNotFoundException e) {
            ConsoleHelper.writeMessage("Ошибка при загрузке данных");
        } finally {
            disconnect();
        }
        return user;
    }

    /**
     * Метод удаляет все данные из таблицы.
     * @throws SQLException
     */
    public static void deleteAllFromTable() throws SQLException {
        try {
            connect();
            String sql = String.format("DELETE FROM %s ", tableName);
            statement.execute(sql);
        } catch (ClassNotFoundException e) {
            ConsoleHelper.writeMessage("Ошибка при работе с БД");
        } finally {
            disconnect();
        }
    }

}
