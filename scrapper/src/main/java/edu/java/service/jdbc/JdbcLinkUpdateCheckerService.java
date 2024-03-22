package edu.java.service.jdbc;

import edu.java.domain.jdbc.JdbcLinkRepository;
import edu.java.dto.repository.Link;
import edu.java.service.LinkUpdateCheckerService;
import java.sql.Timestamp;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JdbcLinkUpdateCheckerService implements LinkUpdateCheckerService {
    private final JdbcLinkRepository linkRepository;

    @Override
    public List<Link> getUnupdatedLinks(Timestamp before) {
        return linkRepository.findAll(before);
    }

    @Override
    public void updateAllLinks(List<Link> links) {
        linkRepository.updateLinks(
            links.stream()
                .map(Link::linkId)
                .toArray(Long[]::new)
        );
    }
}
