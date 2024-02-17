package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.configuration.ApplicationConfig;
import static edu.java.bot.service.UserMessageProcessor.process;

public class Botik {
    final TelegramBot bot;
    final String token;

    public Botik(ApplicationConfig config) {

        this.token = config.telegramToken();

        this.bot = new TelegramBot.Builder(token).debug().build();
    }

    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        bot.execute(request);
    }

    public void start() {
        bot.setUpdatesListener(updates -> {
            for (var update : updates) {
                SendMessage sendMessage = process(update);

                SendResponse response = bot.execute(sendMessage);
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

    }

    public void close() {
        bot.removeGetUpdatesListener();
    }
}
