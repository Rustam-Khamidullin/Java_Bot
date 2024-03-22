package edu.java.service.jpa;

import edu.java.domain.jpa.JpaLinkRepository;
import edu.java.dto.repository.Link;
import edu.java.service.LinkUpdateCheckerService;
import java.net.URI;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
public class JpaLinkUpdateCheckerService implements LinkUpdateCheckerService {
    private final JpaLinkRepository jpaLinkRepository;

    @Override
    @Transactional
    public List<Link> getUnupdatedLinks(Timestamp before) {
        return jpaLinkRepository.findByLastUpdateBefore(before).stream()
            .map(e -> new Link(
                e.getId(),
                URI.create(e.getUrl()),
                e.getChat().getId(),
                e.getLastUpdate()
            )).toList();
    }

    @Override
    @Transactional
    public void updateAllLinks(List<Link> links) {
        var ids = links.stream()
            .map(Link::linkId)
            .collect(Collectors.toSet());
        jpaLinkRepository.updateLastUpdateForIds(ids);
    }
}
