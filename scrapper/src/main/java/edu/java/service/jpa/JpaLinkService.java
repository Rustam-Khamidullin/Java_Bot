package edu.java.service.jpa;

import edu.java.domain.jpa.JpaChatRepository;
import edu.java.domain.jpa.JpaLinkRepository;
import edu.java.dto.repository.Link;
import edu.java.service.LinkService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    private final JpaLinkRepository linkRepository;
    private final JpaChatRepository chatRepository;

    @Override
    @Transactional
    public Link add(long tgChatId, URI url) {
        chatRepository.saveIfNotExists(tgChatId);
        var link = linkRepository.saveIfNotExists(tgChatId, url);

        return new Link(
            link.getId(),
            url,
            tgChatId,
            link.getLastUpdate()
        );
    }

    @Override
    @Transactional
    public Link remove(long tgChatId, URI url) {
        var chat = chatRepository.getReferenceById(tgChatId);
        var link = linkRepository.findByChatAndUrl(chat, url.toString());

        if (link.isEmpty()) {
            return null;
        }

        linkRepository.deleteByChatAndUrl(chat, url.toString());
        return new Link(
            link.get().getId(),
            url,
            tgChatId,
            link.get().getLastUpdate()
        );
    }

    @Override
    @Transactional
    public List<Link> listAll(long tgChatId) {
        var chat = chatRepository.getReferenceById(tgChatId);
        return linkRepository.findAllByChat(chat).stream()
            .map(e -> new Link(
                e.getId(),
                URI.create(e.getUrl()),
                e.getChat().getId(),
                e.getLastUpdate()
            )).toList();
    }
}
