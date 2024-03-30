package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.service.ScrapperService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrackTest {
    @Mock
    private Update mockedUpdate;

    @Mock
    private Message mockedMessage;

    @Mock
    private Chat mockedChat;

    @Mock
    private ScrapperService mockedScrapperService;

    @Test
    public void testTrack() {
        when(mockedUpdate.message()).thenReturn(mockedMessage);
        when(mockedMessage.chat()).thenReturn(mockedChat);
        when(mockedChat.id()).thenReturn(1L);

        Track trackCommand = new Track(mockedScrapperService);
        trackCommand.setArgument("argument");

        Assertions.assertDoesNotThrow(() -> trackCommand.handle(mockedUpdate));
    }
}
