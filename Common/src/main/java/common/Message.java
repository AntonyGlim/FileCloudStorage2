package common;

import java.io.*;
import java.util.ArrayList;

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
    private ArrayList<FileProperties> fileList;

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
        try (FileInputStream fileInputStream = new FileInputStream(file)){
            bytes = new byte[(int)file.length()];
            fileInputStream.read(bytes);
        }
    }

    /** For big files (part by part sending) */
    public Message(MessageType type, File file, String text, byte[] bytes) throws IOException {
        this.type = type;
        this.file = file;
        this.text = text;
        this.bytes = bytes;
    }

    public Message(MessageType type, ArrayList<FileProperties> fileList){
        this.type = type;
        this.fileList = fileList;
        this.text = null;
        this.file = null;
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

    public ArrayList<FileProperties> getFileList() {
        return fileList;
    }
}