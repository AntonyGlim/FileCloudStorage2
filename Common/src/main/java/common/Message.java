package common;

import java.io.*;
import java.util.ArrayList;

/**
 * Class responsible for messages sent
 * A Message is data that one party sends and the other receives.
 * Each message must be of type MessageType, and some additional
 * data, for example, a text message must contain text, the file field must contain a file.
 * Since messages will be created in one program, and read in another, it is convenient to use the mechanism
 * serialization to translate a class into a sequence of bits and vice versa.
 */
public class Message implements Serializable {

    private final MessageType type;
    private final String text;
    private final File file;
    private byte[] bytes; //the body of the file will be stored in the array
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