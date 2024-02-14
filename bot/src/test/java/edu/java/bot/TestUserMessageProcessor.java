package edu.java.bot;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.UserMessageProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.lang.reflect.Method;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestUserMessageProcessor {
    static Update update;
    static Message message;

    @BeforeAll
    static void setup() {
        update = mock(Update.class);
        message = mock(Message.class);
        when(update.message()).thenReturn(message);
    }

    @Test
    void parseCommandNameTest() {
        try {
            Method method = UserMessageProcessor.class.getDeclaredMethod("parseCommandName", Update.class);
            method.setAccessible(true);

            when(message.text()).thenReturn("/command some text");
            Assertions.assertEquals("command", method.invoke(UserMessageProcessor.class, update));

            when(message.text()).thenReturn("/help");
            Assertions.assertEquals("help", method.invoke(UserMessageProcessor.class, update));

            when(message.text()).thenReturn(" /help");
            Assertions.assertEquals("help", method.invoke(UserMessageProcessor.class, update));

            when(message.text()).thenReturn("/");
            Assertions.assertNull(method.invoke(UserMessageProcessor.class, update));

            when(message.text()).thenReturn("");
            Assertions.assertNull(method.invoke(UserMessageProcessor.class, update));

            when(message.text()).thenReturn(null);
            Assertions.assertNull(method.invoke(UserMessageProcessor.class, update));

            when(message.text()).thenReturn("help");
            Assertions.assertNull(method.invoke(UserMessageProcessor.class, update));

            when(message.text()).thenReturn(" something /help");
            Assertions.assertNull(method.invoke(UserMessageProcessor.class, update));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
