package edu.java.service;

import edu.java.client.BotClient;
import edu.java.dto.bot.request.LinkUpdateRequest;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BotNotificationService {
    @Value("${app.useQueue}")
    private boolean useQueue;

    private final ScrapperQueueProducerService queueProducer;
    private final BotClient botClient;

    public BotNotificationService(ScrapperQueueProducerService queueProducer, BotClient botClient) {
        this.queueProducer = queueProducer;
        this.botClient = botClient;
    }

    @SneakyThrows
    public void sendNotification(LinkUpdateRequest update) {
        if (useQueue) {
            queueProducer.send(update);
        } else {
            botClient.sendUpdate(update);
        }
    }
}
