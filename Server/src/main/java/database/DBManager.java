package database;

import common.ConsoleHelper;
import exeption.NoSuchUserException;
import user.User;

import java.sql.*;

/**
 * Work with DB.
 * All public methods has own connect() and disconnect()
 * In DB stored information about list of users which has registration on Server
 */
public class DBManager {
    private static Connection connection;
    private static Statement statement;
    private static String tableName = "users";

    /** Database connection */
    private static synchronized void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:Server/src/main/java/database/" + tableName + ".db");
        statement = connection.createStatement();
    }

    /** Database disconnection */
    private static synchronized void disconnect(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Add an entry to the table */
    public static synchronized void insertIntoTable(int name, int password, long timeWhenAdd, long timeLastVisit) throws SQLException {
        try {
            connect();
            String sql = String.format("INSERT INTO %s (name, password, registration_date, time_last_visit) " +
                    "VALUES ('%d', '%d', '%d', '%d');",
                    tableName,
                    name,
                    password,
                    timeWhenAdd,
                    timeLastVisit
            );
            statement.execute(sql);
        } catch (ClassNotFoundException e) {
            ConsoleHelper.writeMessage("Ошибка при добавлении данных");
        } finally {
            disconnect();
        }
    }

    /** Update time user last visit server*/
    public static synchronized void updateUserTimeLastVisit(int name) throws SQLException, ClassNotFoundException {
        try {
            connect();
            String sql = String.format("UPDATE %s SET time_last_visit = %d WHERE name = %d;",
                    tableName,
                    System.currentTimeMillis(),
                    name
            );
            int count = statement.executeUpdate(sql);

            if (count > 0){                                                             //TODO delete this
                ConsoleHelper.writeMessage("Данные обновлены");                         //TODO delete this
            } else {                                                                    //TODO delete this
                ConsoleHelper.writeMessage("К сожалению, обновления не произошло!");    //TODO delete this
            }                                                                           //TODO delete this
        } catch (ClassNotFoundException e) {
            ConsoleHelper.writeMessage("Ошибка при обновлении данных");
        } finally {
            disconnect();
        }
    }

    /** Return User, by name and password */
    public static synchronized User returnUserFromDBbyNameAndPass(int name, int password) throws SQLException, NoSuchUserException {
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

}
