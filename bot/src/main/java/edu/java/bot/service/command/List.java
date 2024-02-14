package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.ArrayList;

public class List implements Command {
    @Override
    public String command() {
        return "list";
    }

    @Override
    public String description() {
        return "list of tracking links";
    }

    @Override
    public SendMessage handle(Update update) {
        //list logic
        java.util.List<String> list = new ArrayList();

        long chatId = update.message().chat().id();
        StringBuilder answer;
        if (list.isEmpty()) {
            answer = new StringBuilder("There are no tracked links");
        } else {
            answer = new StringBuilder("List of links:\n");
            for (var link : list) {
                answer.append(link).append("\n");
            }
        }
        return new SendMessage(chatId, answer.toString());
    }
}
