package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramException;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.command.Command;
import java.util.HashMap;
import java.util.Map;

public class UserMessageProcessor {
    private static final Map<String, Command> NAME_TO_CMD = new HashMap<>();

    static {
        for (Command cmd : Command.availableCommands()) {
            NAME_TO_CMD.put("/" + cmd.command(), cmd);
        }
    }

    private UserMessageProcessor() {
    }

    private static final String COMMAND_NOT_FOUND = "Command not found. Use /help.";

    public static SendMessage process(Update update) throws TelegramException {
        String text;
        long chatId;
        try {
            text = update.message().text();
            chatId = update.message().chat().id();
        } catch (RuntimeException e) {
            throw new TelegramException(e);
        }

        String cmdName = parseTelegramCommandArgument(text).command;

        Command command = NAME_TO_CMD.getOrDefault(cmdName, null);

        if (command == null) {
            return new SendMessage(chatId, COMMAND_NOT_FOUND);
        }
        return command.handle(update);
    }

    public static CommandArgument parseTelegramCommandArgument(String text) {
        if (!text.startsWith("/")) {
            return new CommandArgument(null, null);
        }

        String[] parts = text.split("\\s+", 2);

        if (parts.length == 0) {
            return new CommandArgument(null, null);
        }

        if (parts.length == 1) {
            return new CommandArgument(parts[0], null);
        }

        return new CommandArgument(parts[0], parts[1]);
    }

    public record CommandArgument(String command, String argument) {
    }
}
