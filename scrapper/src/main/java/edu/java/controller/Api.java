package edu.java.controller;

import edu.java.dto.api.request.AddLinkRequest;
import edu.java.dto.api.request.RemoveLinkRequest;
import edu.java.dto.api.response.LinkResponse;
import edu.java.dto.api.response.ListLinksResponse;
import edu.java.dto.repository.Link;
import edu.java.exception.api.NotFoundException;
import edu.java.service.ChatService;
import edu.java.service.LinkService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Api {
    private final Logger logger = Logger.getLogger("ApiController");
    private final LinkService linkService;
    private final ChatService chatService;

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<?> registerChat(@PathVariable("id") Long id) {
        chatService.register(id);

        logger.info("Чат %d зарегистрирован".formatted(id));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<?> deleteChat(@PathVariable("id") Long id) {
        if (!chatService.unregister(id)) {
            throw new NotFoundException("Чат не существует");
        }

        logger.info("Чат %d успешно удалён".formatted(id));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/links")
    public ResponseEntity<ListLinksResponse> getAllLinks(@RequestHeader("Tg-Chat-Id") Long tgChatId) {
        List<LinkResponse> links =
            linkService.listAll(tgChatId).stream()
                .map(e -> new LinkResponse(e.linkId(), e.url().toString())).toList();
        ListLinksResponse response = new ListLinksResponse(links, links.size());

        logger.info("Отслеживаемые ссылки отправлены");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/links")
    public ResponseEntity<LinkResponse> addLink(
        @RequestHeader("Tg-Chat-Id") Long tgChatId,
        @RequestBody @Valid AddLinkRequest request
    ) {
        Link link = linkService.add(tgChatId, URI.create(request.link()));
        LinkResponse response = new LinkResponse(link.linkId(), link.url().toString());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/links")
    public ResponseEntity<LinkResponse> removeLink(
        @RequestHeader("Tg-Chat-Id") Long tgChatId,
        @RequestBody @Valid RemoveLinkRequest request
    ) {
        Link link = linkService.remove(tgChatId, URI.create(request.link()));

        if (link == null) {
            throw new NotFoundException("Ссылка не найдена");
        }

        LinkResponse response = new LinkResponse(link.linkId(), link.url().toString());
        logger.info("Ссылка удалена");
        return ResponseEntity.ok(response);
    }
}
