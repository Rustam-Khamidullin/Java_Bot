package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class Help extends Command {
    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "help command";
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

        for (var cmd : Command.availableCommands()) {
            answer.append(cmd.command()).append(" - ").append(cmd.description()).append("\n");
        }

        return answer.toString();
    }
}
