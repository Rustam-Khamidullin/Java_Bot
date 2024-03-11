package edu.java.service;

import java.util.logging.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class LinkUpdaterScheduler {
    Logger logger = Logger.getLogger("LinkUpdaterScheduler");

    @Scheduled(fixedDelayString = "#{@scheduler.interval.toMillis()}")
    public void update() {
        logger.info("Executing link update task");
    }
}
