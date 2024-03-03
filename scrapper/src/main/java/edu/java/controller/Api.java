package edu.java.controller;

import edu.java.dto.api.request.AddLinkRequest;
import edu.java.dto.api.request.RemoveLinkRequest;
import edu.java.dto.api.response.LinkResponse;
import edu.java.dto.api.response.ListLinksResponse;
import edu.java.exception.api.BadRequestException;
import edu.java.exception.api.NotFoundException;
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
    private static final String INVALID_ARGUMENT = "Некорректные параметры запроса";
    private final Logger logger = Logger.getLogger("ControllerApi");

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<?> registerChat(@PathVariable("id") Long id) {
        boolean correct = id == 1;
        if (!correct) {
            throw new BadRequestException(null, INVALID_ARGUMENT);
        }

        logger.info("Чат %d зарегистрирован" .formatted(id));

        return ResponseEntity.ok().body("Чат зарегистрирован");
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<?> deleteChat(@PathVariable("id") Long id) {
        boolean correct = id == 1;
        if (!correct) {
            throw new BadRequestException(null, INVALID_ARGUMENT);
        }

        boolean chatExist = true;
        if (!chatExist) {
            throw new NotFoundException(null, "Чат не существует");
        }

        logger.info("Чат %d успешно удалён" .formatted(id));

        return ResponseEntity.ok().body("Чат успешно удалён");
    }

    @GetMapping("/links")
    public ResponseEntity<ListLinksResponse> getAllLinks(@RequestHeader("Tg-Chat-Id") Long tgChatId) {
        boolean correct = tgChatId == 1;
        if (!correct) {
            throw new BadRequestException(null, INVALID_ARGUMENT);
        }

        List<LinkResponse> links = List.of();
        ListLinksResponse response = new ListLinksResponse(links, links.size());
        logger.info("Отслеживаемые ссылки отправлены");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/links")
    public ResponseEntity<LinkResponse> addLink(
        @RequestHeader("Tg-Chat-Id") Long tgChatId,
        @RequestBody AddLinkRequest request
    ) {
        boolean correct = tgChatId == 1;
        if (!correct) {
            throw new BadRequestException(null, INVALID_ARGUMENT);
        }

        LinkResponse response = new LinkResponse(tgChatId, request.link());
        logger.info("Ссылка успешно добавлена");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/links")
    public ResponseEntity<LinkResponse> removeLink(
        @RequestHeader("Tg-Chat-Id") Long tgChatId,
        @RequestBody RemoveLinkRequest request
    ) {
        boolean correct = tgChatId == 1;
        if (!correct) {
            throw new BadRequestException(null, INVALID_ARGUMENT);
        }

        boolean linkExist = request.link().equals("string");
        if (!linkExist) {
            throw new NotFoundException(null, "Ссылка не найдена");
        }

        LinkResponse response = new LinkResponse(tgChatId, request.link());
        logger.info("Ссылка удалена");

        return ResponseEntity.ok(response);
    }
}
