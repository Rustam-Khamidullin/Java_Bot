package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import static edu.java.bot.service.UserMessageProcessor.commands;

public class Help implements Command {
    @Override
    public String command() {
        return "help";
    }

    @Override
    public String description() {
        return "help command";
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();

        StringBuilder answer = new StringBuilder("List of commands:\n");

        for (var cmd : commands()) {
            answer.append("/").append(cmd.command()).append(" - ").append(cmd.description()).append("\n");
        }

        return new SendMessage(chatId, answer.toString());
    }
}
