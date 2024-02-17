package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class Start extends Command {
    private static final String SUCCESSFUL_MESSAGE = "The user has been successfully registered.";
    private static final String UNSUCCESSFUL_MESSAGE = "User registration failed.";

    @Override
    public String command() {
        return "start";
    }

    @Override
    public String description() {
        return "registering a new user";
    }

    @Override
    public SendMessage handle(Update update) {
        //start logic
        boolean success = true;

        long chatId = update.message().chat().id();
        String answer = generateAnswer(success);

        return new SendMessage(chatId, answer);
    }

    private String generateAnswer(boolean success) {
        if (success) {
            return SUCCESSFUL_MESSAGE;
        }
        return UNSUCCESSFUL_MESSAGE;
    }
}
