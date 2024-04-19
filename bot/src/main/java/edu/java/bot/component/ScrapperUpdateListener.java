package edu.java.bot.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.bot.dto.api.request.LinkUpdateRequest;
import edu.java.bot.service.UpdateHandlerService;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScrapperUpdateListener {
    private final ObjectMapper objectMapper;
    private final UpdateHandlerService updateHandlerService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final AtomicInteger requestCounter = new AtomicInteger(0);
    private final AtomicInteger incorrectRequestCounter = new AtomicInteger(0);
    @Value("${kafka.updateTopicName}")
    private String topicName;
    @Value("${kafka.dlqSuffix}")
    private String dlqSuffix;

    @KafkaListener(topics = "${kafka.updateTopicName}")
    public void listen(String request) {
        try {
            LinkUpdateRequest update = objectMapper.readValue(request, LinkUpdateRequest.class);

            updateHandlerService.handleUpdate(update);
            requestCounter.incrementAndGet();
        } catch (Exception e) {
            sendDlqUpdate(request);
        }
    }

    public void sendDlqUpdate(String unhandledRequest) {
        kafkaTemplate.send(topicName + dlqSuffix, unhandledRequest);
        incorrectRequestCounter.incrementAndGet();
    }

    public int getRequestCounter() {
        return requestCounter.get();
    }

    public int getIncorrectRequestCounter() {
        return incorrectRequestCounter.get();
    }
}
