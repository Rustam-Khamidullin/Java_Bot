package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.configuration.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Botik {
    private final TelegramBot bot;
    private final UserMessageProcessor userMessageProcessor;

    @Autowired
    public Botik(
        ApplicationConfig config,
        UserMessageProcessor userMessageProcessor,
        BotCommand[] apiCommands
    ) {
        String token = config.telegramToken();

        this.bot = new TelegramBot.Builder(token).debug().build();
        this.userMessageProcessor = userMessageProcessor;
        bot.execute(new SetMyCommands(apiCommands));
    }

    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        bot.execute(request);
    }

    public void start() {
        bot.setUpdatesListener(updates -> {
            for (var update : updates) {
                if (update == null || update.message() == null || update.message().text() == null) {
                    continue;
                }

                SendMessage sendMessage = userMessageProcessor.process(update);
                bot.execute(sendMessage);
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

    }

    public void close() {
        bot.removeGetUpdatesListener();
    }
}
