package common;

import java.io.File;
import java.io.Serializable;

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

    public Message(MessageType type, File file) {
        this.type = type;
        this.text = null;
        this.file = file;
    }

    public Message(MessageType type, String text, File file) {
        this.type = type;
        this.text = text;
        this.file = file;
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
}