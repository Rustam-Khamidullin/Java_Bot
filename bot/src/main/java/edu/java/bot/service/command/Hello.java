package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class Hello implements Command {
    @Override
    public String command() {
        return "hello";
    }

    @Override
    public String description() {
        return "send hello";
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        String answer = "Hi man!";
        return new SendMessage(chatId, answer);
    }
}
