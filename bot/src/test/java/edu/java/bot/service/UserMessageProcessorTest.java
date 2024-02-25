package edu.java.bot.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static edu.java.bot.service.UserMessageProcessor.parseTelegramCommandArgument;

class UserMessageProcessorTest {
    @Test
    void parseTelegramCommandNameTest() {
        Assertions.assertEquals(new UserMessageProcessor.CommandArgument("/command", "some text"),
            parseTelegramCommandArgument("/command some text"));

        Assertions.assertEquals(new UserMessageProcessor.CommandArgument("/help", null),
            parseTelegramCommandArgument("/help"));

        Assertions.assertEquals(new UserMessageProcessor.CommandArgument("/help", "a a a     "),
            parseTelegramCommandArgument("/help a a a     "));

        Assertions.assertEquals(new UserMessageProcessor.CommandArgument(null, null),
            parseTelegramCommandArgument(" /help"));

        Assertions.assertEquals(new UserMessageProcessor.CommandArgument(null, null),
            parseTelegramCommandArgument(""));

        Assertions.assertEquals(new UserMessageProcessor.CommandArgument(null, null),
            parseTelegramCommandArgument("help"));

        Assertions.assertEquals(new UserMessageProcessor.CommandArgument(null, null),
            parseTelegramCommandArgument(" something /help"));
    }
}
