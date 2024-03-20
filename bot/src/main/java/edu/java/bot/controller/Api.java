package edu.java.bot.controller;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dto.api.request.LinkUpdateRequest;
import edu.java.bot.service.Botik;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Api {
    private final Botik botik;

    @PostMapping("/updates")
    public ResponseEntity<?> handleUpdate(@RequestBody @Valid LinkUpdateRequest linkUpdate) {
        //todo move from here
        botik.execute(new SendMessage(
            linkUpdate.chatId(),
            linkUpdate.description()
        ));

        return ResponseEntity.ok().build();
    }
}
