package client;

import command.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс реализует паттерн Command
 */
public class CommandExecutor {
    private static final Map<ClientOperation, Command> allKnownCommandsMap = new HashMap<ClientOperation, Command>();

    static {
        allKnownCommandsMap.put(ClientOperation.ADD, new CommandADD());
        allKnownCommandsMap.put(ClientOperation.REMOVE, new CommandREMOVE());
        allKnownCommandsMap.put(ClientOperation.CONTENT, new CommandCONTENT());
        allKnownCommandsMap.put(ClientOperation.EXIT, new CommandEXIT());
    }

    private CommandExecutor() {
    }

    public static void execute(ClientOperation operation) throws Exception {
        allKnownCommandsMap.get(operation).execute();
    }

}
