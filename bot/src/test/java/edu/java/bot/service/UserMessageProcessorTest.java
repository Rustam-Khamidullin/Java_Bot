package edu.java.bot.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static edu.java.bot.service.UserMessageProcessor.parseTelegramCommandArgument;
import static edu.java.bot.service.UserMessageProcessor.parseTelegramCommandName;

class UserMessageProcessorTest {
    @Test
    void parseTelegramCommandNameTest() {
        Assertions.assertEquals("command", parseTelegramCommandName("/command some text"));

        Assertions.assertEquals("help", parseTelegramCommandName("/help"));

        Assertions.assertNull(parseTelegramCommandName(" /help"));

        Assertions.assertNull(parseTelegramCommandName("/"));

        Assertions.assertNull(parseTelegramCommandName(""));

        Assertions.assertNull(parseTelegramCommandName(null));

        Assertions.assertNull(parseTelegramCommandName("help"));

        Assertions.assertNull(parseTelegramCommandName(" something /help"));
    }

    @Test
    void parseTelegramCommandArgumentTest() {
        Assertions.assertEquals("some text", parseTelegramCommandArgument("/command some text"));

        Assertions.assertEquals("some text", parseTelegramCommandArgument("/command               some text"));

        Assertions.assertEquals("some  text", parseTelegramCommandArgument("/command               some  text"));

        Assertions.assertNull(parseTelegramCommandArgument("/help"));

        Assertions.assertNull(parseTelegramCommandArgument(" something /help"));
    }
}
