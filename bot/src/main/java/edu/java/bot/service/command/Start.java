package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.ScrapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("/start")
@RequiredArgsConstructor
public class Start extends Command {
    private final ScrapperService scrapperService;
    private static final String SUCCESSFUL_MESSAGE = "The user has been successfully registered.";

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "registering a new user";
    }

    @Override
    public boolean isArgumentNecessary() {
        return false;
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();
        scrapperService.register(chatId);

        return new SendMessage(chatId, SUCCESSFUL_MESSAGE);
    }
}
