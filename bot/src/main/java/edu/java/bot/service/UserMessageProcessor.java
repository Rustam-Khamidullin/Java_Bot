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
    private static final String ARGUMENT_NOT_FOUND = "The command requires an argument";

    public static SendMessage process(Update update) {
        String text = update.message().text();
        long chatId = update.message().chat().id();

        CommandArgument commandArgument = parseTelegramCommandArgument(text);

        String cmdName = commandArgument.command;
        String argument = commandArgument.argument;
        Command command = NAME_TO_CMD.getOrDefault(cmdName, null);

        if (command == null) {
            return new SendMessage(chatId, COMMAND_NOT_FOUND);
        }

        if (command.isArgumentNecessary() && argument == null) {
            return new SendMessage(chatId, ARGUMENT_NOT_FOUND);
        }

        command.setArgument(argument);
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
