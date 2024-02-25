package edu.java.bot.service.command;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

public class CommandTest {
    @ParameterizedTest
    @MethodSource("generateCommands")
    void commandNameTest(Command command) {
        Assertions.assertNotNull(command.command());
        Assertions.assertFalse(command.command().isEmpty());
    }

    @ParameterizedTest
    @MethodSource("generateCommands")
    void commandDescriptionTest(Command command) {
        Assertions.assertNotNull(command.description());
        Assertions.assertFalse(command.description().isEmpty());
    }

    static Stream<Arguments> generateCommands() {
        return Command.availableCommands().stream().map(Arguments::arguments);
    }
}
