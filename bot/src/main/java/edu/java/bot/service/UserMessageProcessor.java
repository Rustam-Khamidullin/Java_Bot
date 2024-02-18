package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.command.Command;
import java.util.HashMap;
import java.util.Map;

public class UserMessageProcessor {
    private static final Map<String, Command> NAME_TO_CMD = new HashMap<>();

    static {
        for (Command cmd : Command.availableCommands()) {
            NAME_TO_CMD.put(cmd.command(), cmd);
        }
    }

    private UserMessageProcessor() {
    }

    private static final String COMMAND_NOT_FOUND = "Command not found. Use /help.";

    public static SendMessage process(Update update) {
        if (update == null) {
            throw new NullPointerException();
        }

        String cmdName = parseTelegramCommandName(update.message().text());
        long chatId = update.message().chat().id();

        Command command = NAME_TO_CMD.getOrDefault(cmdName, null);

        if (command == null) {
            return new SendMessage(chatId, COMMAND_NOT_FOUND);
        }
        return command.handle(update);
    }

    public static String parseTelegramCommandName(String message) {
        if (message == null || !message.startsWith("/")) {
            return null;
        }

        int commandEndIndex = message.indexOf(' ', 1);
        String stringCommand;
        if (commandEndIndex == -1) {
            stringCommand = message.substring(1);
        } else {
            stringCommand = message.substring(1, commandEndIndex);
        }

        if (stringCommand.length() <= 1) {
            return null;
        }

        return stringCommand;
    }

    public static String parseTelegramCommandArgument(String message) {
        if (message == null || !message.startsWith("/")) {
            return null;
        }

        String[] parts = message.split("\\s+", 2);

        if (parts.length != 2 || parts[1].isBlank()) {
            return null;
        }

        return parts[1];
    }
}
