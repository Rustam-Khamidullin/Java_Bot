package edu.java.service;

import edu.java.client.BotClient;
import edu.java.client.github.GitHubClient;
import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.configuration.ApplicationConfiguration;
import edu.java.domain.LinkRepository;
import edu.java.dto.bot.request.LinkUpdateRequest;
import edu.java.dto.repository.Link;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkUpdaterService {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final LinkRepository linkRepository;
    private final ApplicationConfiguration.Scheduler scheduler;
    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;
    private final BotClient botClient;

    @Scheduled(fixedDelayString = "#{@scheduler.interval.toMillis()}")
    public void update() {
        logger.info("Links updating");

        List<Link> linksToUpdate = linkRepository.findAll(scheduler.forceCheckDelay().toMillis());

        for (var link : linksToUpdate) {
            checkForUpdates(link);
        }

        linkRepository.updateLinks(
            linksToUpdate.stream()
                .map(Link::linkId)
                .toArray(Long[]::new)
        );
    }

    public void checkForUpdates(Link link) {
        // todo chain of responsibility maybe

        OffsetDateTime lastUpdate =
            OffsetDateTime.ofInstant(link.lastUpdate().toInstant(), ZoneOffset.UTC);
        URI url = link.url();
        Path path = Paths.get(url.getPath());

        if (url.getHost().equals("github.com") && path.getNameCount() == 2) {
            var repository = gitHubClient.getRepository(
                path.getName(0).toString(),
                path.getName(1).toString()
            );

            if (repository.updatedAt().isAfter(lastUpdate)
                || repository.pushedAt().isAfter(lastUpdate)) {
                botClient.sendUpdate(
                    new LinkUpdateRequest(
                        link.chatId(),
                        url,
                        "github changes"
                    )
                );
            }
            return;
        }

        if (url.getHost().equals("stackoverflow.com")
            && path.getNameCount() == 2
            && path.getName(0).toString().equals("questions")) {
            // todo stackoverflow handling
        }
    }
}
