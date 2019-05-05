package common;

import common.exception.InvalidInputFormatException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleHelper {
    private static BufferedReader bis = new BufferedReader(new InputStreamReader(System.in));

    public static void writeMessage(String message) {
        System.out.println(message);
    }

    public static String readString() throws IOException {
        String text = bis.readLine();
        return text;
    }

    public static int readInt() throws IOException, InvalidInputFormatException {
        try {
            String text = readString();
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e){
            writeMessage("Не корректный ввод");
            throw new InvalidInputFormatException();
        }
    }
}
