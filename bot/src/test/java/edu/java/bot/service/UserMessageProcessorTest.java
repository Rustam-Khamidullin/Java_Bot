package edu.java.bot.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserMessageProcessorTest {
    @Autowired
    private UserMessageProcessor userMessageProcessor;

    @Test
    void parseTelegramCommandNameTest() {
        Assertions.assertEquals(
            new UserMessageProcessor.CommandArgument("/command", "some text"),
            userMessageProcessor.parseTelegramCommandArgument("/command some text")
        );

        Assertions.assertEquals(
            new UserMessageProcessor.CommandArgument("/help", null),
            userMessageProcessor.parseTelegramCommandArgument("/help")
        );

        Assertions.assertEquals(
            new UserMessageProcessor.CommandArgument("/help", "a a a     "),
            userMessageProcessor.parseTelegramCommandArgument("/help a a a     ")
        );

        Assertions.assertEquals(
            new UserMessageProcessor.CommandArgument(null, null),
            userMessageProcessor.parseTelegramCommandArgument(" /help")
        );

        Assertions.assertEquals(
            new UserMessageProcessor.CommandArgument(null, null),
            userMessageProcessor.parseTelegramCommandArgument("")
        );

        Assertions.assertEquals(
            new UserMessageProcessor.CommandArgument(null, null),
            userMessageProcessor.parseTelegramCommandArgument("help")
        );

        Assertions.assertEquals(
            new UserMessageProcessor.CommandArgument(null, null),
            userMessageProcessor.parseTelegramCommandArgument(" something /help")
        );
    }
}
