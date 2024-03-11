package edu.java.bot.controller;

import edu.java.bot.dto.api.request.LinkUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Api {
    @PostMapping("/updates")
    public ResponseEntity<?> handleUpdate(@RequestBody @Valid LinkUpdateRequest linkUpdate) {
        //update logic

        return ResponseEntity.ok().build();
    }
}
