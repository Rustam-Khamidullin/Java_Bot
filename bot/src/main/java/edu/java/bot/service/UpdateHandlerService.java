package edu.java.bot.service;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dto.api.request.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateHandlerService {
    private final Botik botik;

    public void handleUpdate(LinkUpdateRequest update) {
        botik.execute(new SendMessage(
            update.chatId(),
            update.description()
        ));
    }
}
