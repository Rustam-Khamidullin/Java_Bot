package edu.java.service.jdbc;


import edu.java.domain.jdbc.JdbcChatRepository;
import edu.java.domain.jdbc.JdbcLinkRepository;
import edu.java.dto.repository.Link;
import edu.java.service.LinkService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final JdbcLinkRepository linkRepository;
    private final JdbcChatRepository chatRepository;

    @Override
    public Link add(long tgChatId, URI url) {
        chatRepository.addOrGetExisting(tgChatId);
        return linkRepository.addOrGetExisting(tgChatId, url);
    }

    @Override
    public Link remove(long tgChatId, URI url) {
        return linkRepository.remove(tgChatId, url);
    }

    @Override
    public List<Link> listAll(long tgChatId) {
        return linkRepository.findAll(tgChatId);
    }
}
