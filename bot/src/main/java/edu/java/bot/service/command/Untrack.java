package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class Untrack implements Command {
    private static final String SUCCESS_MESSAGE = "Link tracking has been discontinued.";
    private static final String UNSUCCESS_MESSAGE = "Failed to stop tracking.";

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
        String answer;
        if (success) {
            answer = SUCCESS_MESSAGE;
        } else {
            answer = UNSUCCESS_MESSAGE;
        }
        return new SendMessage(chatId, answer);
    }
}
