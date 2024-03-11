package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.service.command.Command;

public class Botik {
    private final TelegramBot bot;

    public Botik(ApplicationConfig config) {
        String token = config.telegramToken();

        this.bot = new TelegramBot.Builder(token).debug().build();
        bot.execute(new SetMyCommands(Command.getListApiCommands().toArray(new BotCommand[0])));
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

                SendMessage sendMessage = UserMessageProcessor.process(update);
                bot.execute(sendMessage);
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

    }

    public void close() {
        bot.removeGetUpdatesListener();
    }
}
