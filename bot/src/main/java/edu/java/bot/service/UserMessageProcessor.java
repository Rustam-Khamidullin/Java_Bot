package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.command.Command;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static edu.java.bot.service.command.Command.availableCommands;

public class UserMessageProcessor {
    private static final Map<String, Command> NAME_TO_CMD = new HashMap<>();

    static {
        for (Command cmd : availableCommands()) {
            NAME_TO_CMD.put(cmd.command(), cmd);
        }
    }

    private UserMessageProcessor() {
    }

    private static final Pattern COMMAND_PATTERN = Pattern.compile("^/(\\w+)");
    private static final String COMMAND_NOT_FOUND = "Command not found. Use /help.";

    public static SendMessage process(Update update) {
        String cmdName = parseCommandName(update);
        long chatId = update.message().chat().id();

        Command command = NAME_TO_CMD.getOrDefault(cmdName, null);

        SendMessage answer;
        if (command == null) {
            answer = new SendMessage(chatId, COMMAND_NOT_FOUND);
        } else {
            answer = command.handle(update);
        }

        return answer;
    }

    private static String parseCommandName(Update update) {
        String text = update.message().text();

        if (text == null) {
            return null;
        }

        Matcher matcher = COMMAND_PATTERN.matcher(text);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }
}
