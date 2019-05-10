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
    DISCONNECTION_OK, //just reserved but not use
    UPLOAD_FILE,
    UPLOAD_FILE_OK,
    UPLOAD_BIG_FILE,
    UPLOAD_BIG_FILE_END,
    UPLOAD_BIG_FILE_OK,
    DOWNLOAD_FILE,
    DOWNLOAD_FILE_OK,


//    NAME_REQUEST,       //запрос имени.
//    USER_NAME,          //имя пользователя.
//    USER_PASSWORD,       //пароль пользователя.
//    NAME_ACCEPTED,      //имя принято.
//    USER_ADDED,         //пользователь добавлен.
//    USER_REMOVED,       //пользователь удален.
//    TEXT,               //текстовое сообщение.

}
