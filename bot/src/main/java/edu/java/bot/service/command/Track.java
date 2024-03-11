package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class Track extends Command {
    private static final String SUCCESSFUL_MESSAGE = "The link has been added to the tracked.";
    private static final String UNSUCCESSFUL_MESSAGE = "It is not possible to track this link.";

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "start tracking a link";
    }

    @Override
    public boolean isArgumentNecessary() {
        return true;
    }

    @Override
    public SendMessage handle(Update update) {
        //track logic
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
