package edu.java.bot.controller;

import edu.java.bot.dto.api.request.LinkUpdateRequest;
import edu.java.bot.exception.api.BadRequestException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Api {
    @PostMapping("/updates")
    public ResponseEntity<?> handleUpdate(@RequestBody @Valid LinkUpdateRequest linkUpdate) {
        boolean correct = true;
        if (!correct) {
            throw new BadRequestException(null, "Некорректные параметры запроса");
        }

        return ResponseEntity.ok("Обновление обработано");
    }
}
