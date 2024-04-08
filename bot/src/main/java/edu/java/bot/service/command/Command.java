package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public abstract class Command {
    protected String argument = null;

    abstract public String command();

    abstract public String description();

    abstract public boolean isArgumentNecessary();

    abstract public SendMessage handle(Update update);

    final public void setArgument(String argument) {
        this.argument = argument;
    }

    final public String getArgument() {
        return argument;
    }
}
