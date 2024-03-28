package edu.java.service;

import edu.java.domain.ChatRepository;
import edu.java.domain.LinkRepository;
import edu.java.dto.repository.Link;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LinkService {
    private final LinkRepository linkRepository;
    private final ChatRepository chatRepository;

    public Link add(long tgChatId, URI url) {
        chatRepository.addOrGetExists(tgChatId);
        return linkRepository.addOrGetExists(tgChatId, url);
    }

    public Link remove(long tgChatId, URI url) {
        return linkRepository.remove(tgChatId, url);
    }

    public List<Link> listAll(long tgChatId) {
        return linkRepository.findAll(tgChatId);
    }
}
