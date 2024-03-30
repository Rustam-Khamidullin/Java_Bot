package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.ScrapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("/track")
@RequiredArgsConstructor
public class Track extends Command {
    private final ScrapperService scrapperService;
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
        long chatId = update.message().chat().id();

        if (argument.isBlank()) {
            return new SendMessage(chatId, UNSUCCESSFUL_MESSAGE);
        }

        scrapperService.trackLink(chatId, argument);
        return new SendMessage(chatId, SUCCESSFUL_MESSAGE);
    }
}
