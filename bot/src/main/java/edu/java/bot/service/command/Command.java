package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public abstract class Command {
    public static java.util.List<Command> availableCommands() {
        return java.util.List.of(
            new Start(),
            new Help(),
            new Track(),
            new Untrack(),
            new List()
        );
    }

    abstract public String command();

    abstract public String description();

    abstract public SendMessage handle(Update update);

    BotCommand toApiCommand() {
        return new BotCommand(command(), description());
    }
}
