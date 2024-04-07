package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("/help")
@RequiredArgsConstructor
public class Help extends Command {
    private final Map<String, Command> commands;

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "show available commands";
    }

    @Override
    public boolean isArgumentNecessary() {
        return false;
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();

        String answer = generateAnswer();

        return new SendMessage(chatId, answer);
    }

    private String generateAnswer() {
        StringBuilder answer = new StringBuilder("List of commands:\n");

        for (var cmd : commands.entrySet()) {
            answer.append(cmd.getKey()).append(" - ").append(cmd.getValue().description()).append("\n");
        }

        return answer.toString();
    }
}
