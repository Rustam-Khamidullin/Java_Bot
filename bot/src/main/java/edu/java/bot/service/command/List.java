package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.ScrapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("/list")
@RequiredArgsConstructor
public class List extends Command {
    private final ScrapperService scrapperService;

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "list of tracking links";
    }

    @Override
    public boolean isArgumentNecessary() {
        return false;
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();
        java.util.List<String> links = scrapperService.getTrackingLink(chatId);
        String answer = generateAnswer(links);

        return new SendMessage(chatId, answer);
    }

    private String generateAnswer(java.util.List<String> links) {
        if (links.isEmpty()) {
            return "There is no tracking links.";
        }
        StringBuilder answer = new StringBuilder("Tracking links:\n");
        for (var link : links) {
            answer.append(link).append("\n");
        }

        return answer.toString();
    }
}
