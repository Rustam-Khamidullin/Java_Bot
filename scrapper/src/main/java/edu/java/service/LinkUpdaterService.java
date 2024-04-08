package edu.java.service;

import edu.java.client.BotClient;
import edu.java.client.github.GitHubClient;
import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.configuration.ApplicationConfiguration;
import edu.java.dto.bot.request.LinkUpdateRequest;
import edu.java.dto.repository.Link;
import edu.java.dto.stackoverflow.Questions;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
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
    private final LinkUpdateCheckerService linkUpdateCheckerService;
    private final ApplicationConfiguration.Scheduler scheduler;

    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;
    private final BotClient botClient;

    @Scheduled(fixedDelayString = "#{@scheduler.interval.toMillis()}")
    public void update() {
        logger.info("Links updating");

        List<Link> linksToUpdate = linkUpdateCheckerService.getUnupdatedLinks(
            new Timestamp(System.currentTimeMillis() - scheduler.forceCheckDelay().toMillis())
        );

        if (linksToUpdate.isEmpty()) {
            return;
        }

        for (var link : linksToUpdate) {
            updateLink(link);
        }

        linkUpdateCheckerService.updateAllLinks(linksToUpdate);
    }

    private void updateLink(Link link) {
        logger.info("Updating " + link);

        // todo chain of responsibility maybe

        OffsetDateTime lastUpdate =
            OffsetDateTime.ofInstant(link.lastUpdate().toInstant(), ZoneOffset.UTC);
        URI url = link.url();
        if (url.getHost() == null) {
            return;
        }

        Path path = Paths.get(url.getPath());

        if (url.getHost() == null) {
            return;
        }

        if (url.getHost().equals("github.com") && path.getNameCount() == 2) {
            var repository = gitHubClient.getRepository(
                path.getName(0).toString(),
                path.getName(1).toString()
            );

            String description = null;

            if (repository.updatedAt().isAfter(lastUpdate)) {
                description = url + " has new update.";
            } else if (repository.pushedAt().isAfter(lastUpdate)) {
                description = url + " has something pushed.";
            }

            if (description != null) {
                botClient.sendUpdate(
                    new LinkUpdateRequest(
                        link.chatId(),
                        url,
                        description
                    )
                );
            }
        } else if (url.getHost().equals("stackoverflow.com")
            && path.getNameCount() == 2
            && path.getName(0).toString().equals("questions")) {
            Questions questions = stackOverflowClient.getQuestions(
                Integer.parseInt(path.getName(1).toString()));

            if (!questions.items().isEmpty()) {
                OffsetDateTime lastActivityDate = questions.items().getFirst().lastActivityDate();
                OffsetDateTime lastEditDate = questions.items().getFirst().lastEditDate();

                String description = null;

                if (lastEditDate.isAfter(lastUpdate)) {
                    description = url + "edited.";
                } else if (lastActivityDate.isAfter(lastUpdate)) {
                    description = url + "new activity.";
                }

                if (description != null) {
                    botClient.sendUpdate(
                        new LinkUpdateRequest(
                            link.chatId(),
                            url,
                            description
                        )
                    );
                }
            }
        }
    }
}
