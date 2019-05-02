package common;

import java.io.*;

/**
 * Класс, отвечающий за пересылаемые сообщения
 * Сообщение Message - это данные, которые одна сторона отправляет, а вторая принимает.
 * Каждое сообщение должно иметь тип MessageType, а некоторые и дополнительные
 * данные, например, текстовое сообщение должно содержать текст, файловое поле должно содержать файл.
 * Т.к. сообщения будут создаваться в одной программе, а читаться в другой, удобно воспользоваться механизмом
 * сериализации для перевода класса в последовательность битов и наоборот.
 */
public class Message implements Serializable {

    private final MessageType type; //будет содержать тип сообщения
    private final String text; //будет содержать текст сообщения
    private final File file; //будет содержать файл для пересылки
    private byte[] bytes; //собственно в массиве будет храниться тело файла

    public Message(MessageType type) {
        this.type = type;
        text = null;
        file = null;
    }

    public Message(MessageType type, String text) {
        this.type = type;
        this.text = text;
        file = null;
    }

    public Message(MessageType type, File file) throws IOException {
        this.type = type;
        this.text = null;
        this.file = file;
        FileInputStream fileInputStream = new FileInputStream(file);
        bytes = new byte[(int)file.length()];
        fileInputStream.read(bytes);
    }

    public MessageType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public File getFile() {
        return file;
    }

    public byte[] getBytes() {
        return bytes;
    }
}