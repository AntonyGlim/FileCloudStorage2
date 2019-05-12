package common;

/**
 * All messages from client to server and backward
 * must have one of this type
 */
public enum MessageType {

    AUTHORIZATION,
    AUTHORIZATION_OK,

    REGISTRATION,
    REGISTRATION_OK,

    DISCONNECTION,

    UPLOAD_FILE,
    UPLOAD_FILE_OK,
    UPLOAD_BIG_FILE,
    UPLOAD_BIG_FILE_END,

    DOWNLOAD_FILE,
    DOWNLOAD_FILE_OK,
    DOWNLOAD_BIG_FILE,
    DOWNLOAD_BIG_FILE_END,

    FILE_LIST,
    FILE_LIST_OK,

    DELETE_FILE_FROM_SERVER,
    DELETE_FILE_FROM_SERVER_OK,
}
