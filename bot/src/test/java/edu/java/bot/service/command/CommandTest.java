package edu.java.bot.service.command;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@RequiredArgsConstructor
public class CommandTest {
    private final Map<String, Command> commands;

    @Test
    void commandNameTest() {
        for (var command : commands.entrySet()) {
            Assertions.assertNotNull(command.getValue().command());
            Assertions.assertFalse(command.getValue().command().isEmpty());
        }
    }

    @Test
    void commandDescriptionTest() {
        for (var command : commands.entrySet()) {
            Assertions.assertNotNull(command.getValue().description());
            Assertions.assertFalse(command.getValue().description().isEmpty());
        }

    }
}
