package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.ScrapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("/untrack")
@RequiredArgsConstructor
public class Untrack extends Command {
    private final ScrapperService scrapperService;
    private static final String SUCCESSFUL_MESSAGE = "Link tracking has been discontinued.";
    private static final String UNSUCCESSFUL_MESSAGE = "Failed to untarck this link.";

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "stop tracking the link";
    }

    @Override
    public boolean isArgumentNecessary() {
        return true;
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();
        if (argument.isBlank()
            || !scrapperService.removeLink(chatId, argument)) {
            return new SendMessage(chatId, UNSUCCESSFUL_MESSAGE);
        }

        return new SendMessage(chatId, SUCCESSFUL_MESSAGE);
    }
}
