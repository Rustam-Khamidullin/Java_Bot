package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class Track implements Command {
    private static final String SUCCESS_MESSAGE = "The link has been added to the tracked.";
    private static final String UNSUCCESS_MESSAGE = "It is not possible to track this link.";
    @Override
    public String command() {
        return "track";
    }

    @Override
    public String description() {
        return "start tracking the link";
    }

    @Override
    public SendMessage handle(Update update) {
        //track logic
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
