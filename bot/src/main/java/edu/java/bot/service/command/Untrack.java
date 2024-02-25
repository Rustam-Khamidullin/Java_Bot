package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class Untrack extends Command {
    private static final String SUCCESSFUL_MESSAGE = "Link tracking has been discontinued.";
    private static final String UNSUCCESSFUL_MESSAGE = "Failed to stop tracking.";

    @Override
    public String command() {
        return "untrack";
    }

    @Override
    public String description() {
        return "stop tracking the link";
    }

    @Override
    public SendMessage handle(Update update) {
        //untrack logic
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
