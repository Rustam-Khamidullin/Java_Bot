package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public abstract class Command {
    private static final java.util.List<Command> COMMANDS =
        java.util.List.of(
            new Start(),
            new Help(),
            new Track(),
            new Untrack(),
            new List()
        );

    protected String argument = null;

    public static java.util.List<BotCommand> getListApiCommands() {
        return COMMANDS.stream()
            .map(cmd -> new BotCommand(cmd.command(), cmd.description()))
            .toList();
    }

    public static java.util.List<Command> availableCommands() {
        return COMMANDS;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    abstract public String command();

    abstract public String description();

    abstract public boolean isArgumentNecessary();

    abstract public SendMessage handle(Update update);

}
