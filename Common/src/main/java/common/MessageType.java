package common;
/**
 * Enum отвечает за тип сообщений пересылаемых между
 * клиентом и сервером.
 */
public enum MessageType {
    NAME_REQUEST,       //запрос имени.
    USER_NAME,          //имя пользователя.
    USER_PASSWORD,       //пароль пользователя.
    NAME_ACCEPTED,      //имя принято.
    USER_ADDED,         //пользователь добавлен.
    USER_REMOVED,       //пользователь удален.
    TEXT,               //текстовое сообщение.
    FILE,                //передача файла
    TEST
}
