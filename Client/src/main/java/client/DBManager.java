package client;

import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * Метод вернет строки из БД цена которых будет соответствовать ценовому диапазону.
     * @param tableName
     * @param lowerBorder - минимальная цена
     * @param upperBorder - максимальная цена
     * @throws SQLException
     */
    public static void returnFromDiapasonByCost(String tableName, int lowerBorder, int upperBorder) throws SQLException {
        String sql = String.format("SELECT * FROM %s WHERE cost >= %d AND cost <= %d;", tableName, lowerBorder, upperBorder);
        ResultSet rs = statement.executeQuery(sql);
        System.out.printf("%6s", "id");                             //printf - для удобства восприятия информации
        System.out.printf("%8s", "prodid");
        System.out.printf("%12s", "title");
        System.out.printf("%10s", "cost" + "\n");
        while (rs.next()){
            System.out.printf("%6d", rs.getInt(1));
            System.out.printf("%8d", rs.getInt(2));
            System.out.printf("%12s", rs.getString(3));
            System.out.printf("%10s", (rs.getInt(4) + "\n"));
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

    /**
     * Метод выведет на экран информацию о командах в приложении
     */
    private static void userInformation(){
        System.out.println("Ознакомтесь с информацией для работы: ");
        System.out.println("1.Чтобы узнать цену товара, введите ключевое слово \"цена\" и имя товара через пробел," +
                "\nзатем нажмите Enter. Пример: \"цена Товар_456\";");
        System.out.println("2.Чтобы изменить цену товара, введите ключевое слово \"сменитьцену\", имя товара через пробел " +
                "\nи новую цену товара, затем нажмите Enter. Пример: \"сменитьцену Товар_456 55\"");
        System.out.println("3.Чтобы получить список товаров из определенного ценового диапазона, " +
                "\nвведите ключевое слово \"товарыпоцене\", минимальную и максимальную цену товара, " +
                "\nзатем нажмите Enter. Пример: \"товарыпоцене 100 200\"");
        System.out.println("4. Для выхода введите \"/q\")");
    }
}
