package edu.java.bot.controller;

import edu.java.bot.dto.api.request.LinkUpdateRequest;
import edu.java.bot.service.UpdateHandlerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Api {
    private final UpdateHandlerService updateHandlerService;

    @PostMapping("/updates")
    public ResponseEntity<?> handleUpdate(@RequestBody @Valid LinkUpdateRequest linkUpdate) {
        updateHandlerService.handleUpdate(linkUpdate);
        return ResponseEntity.ok().build();
    }
}
