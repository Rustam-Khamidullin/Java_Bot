package edu.java.controller;

import edu.java.dto.api.request.AddLinkRequest;
import edu.java.dto.api.request.RemoveLinkRequest;
import edu.java.dto.api.response.LinkResponse;
import edu.java.dto.api.response.ListLinksResponse;
import edu.java.exception.api.NotFoundException;
import jakarta.validation.Valid;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Api {
    private final Logger logger = Logger.getLogger("ControllerApi");

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<?> registerChat(@PathVariable("id") Long id) {
        logger.info("Чат %d зарегистрирован".formatted(id));

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<?> deleteChat(@PathVariable("id") Long id) {
        boolean chatExist = true;
        if (!chatExist) {
            throw new NotFoundException("Чат не существует");
        }

        logger.info("Чат %d успешно удалён".formatted(id));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/links")
    public ResponseEntity<ListLinksResponse> getAllLinks(@RequestHeader("Tg-Chat-Id") Long tgChatId) {
        List<LinkResponse> links = List.of();
        ListLinksResponse response = new ListLinksResponse(links, links.size());
        logger.info("Отслеживаемые ссылки отправлены");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/links")
    public ResponseEntity<LinkResponse> addLink(
        @RequestHeader("Tg-Chat-Id") Long tgChatId,
        @RequestBody @Valid AddLinkRequest request
    ) {
        LinkResponse response = new LinkResponse(tgChatId, request.link());
        logger.info("Ссылка успешно добавлена");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/links")
    public ResponseEntity<LinkResponse> removeLink(
        @RequestHeader("Tg-Chat-Id") Long tgChatId,
        @RequestBody @Valid RemoveLinkRequest request
    ) {
        boolean linkExist = request.link().equals("string");
        if (!linkExist) {
            throw new NotFoundException("Ссылка не найдена");
        }

        LinkResponse response = new LinkResponse(tgChatId, request.link());
        logger.info("Ссылка удалена");

        return ResponseEntity.ok(response);
    }
}
