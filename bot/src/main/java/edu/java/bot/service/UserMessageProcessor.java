package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.command.Command;
import edu.java.bot.service.command.Hello;
import edu.java.bot.service.command.Help;
import edu.java.bot.service.command.List;
import edu.java.bot.service.command.Track;
import edu.java.bot.service.command.Untrack;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserMessageProcessor {
    public static java.util.List<? extends Command> commands() {
        return java.util.List.of(
            new Help(),
            new Hello(),
            new Track(),
            new Untrack(),
            new List()
        );
    }

    private static final Map<String, Command> NAME_TO_CMD = new HashMap<>();

    static {
        for (var cmd : commands()) {
            NAME_TO_CMD.put(cmd.command(), cmd);
        }
    }

    private static final Pattern FIRST_WORD_PATTERN = Pattern.compile("^\\s*/(\\w+)");
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

        Matcher matcher = FIRST_WORD_PATTERN.matcher(text);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }
}
