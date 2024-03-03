package edu.java.bot.service.command;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.util.List;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

public class HelpTest {
    @Test
    void generateAnswerTest() {
        try {
            Method method = Help.class.getDeclaredMethod("generateAnswer");
            method.setAccessible(true);
            mockStatic(Command.class);
            when(Command.availableCommands()).thenReturn(
                List.of(
                    new Help(),
                    new Help(),
                    new Help()
                )
            );

            Help help = new Help();
            String result = (String) method.invoke(help);

            Assertions.assertEquals(
                "List of commands:\n"
                    + help.command() + " - " + help.description() + "\n"
                    + help.command() + " - " + help.description() + "\n"
                    + help.command() + " - " + help.description() + "\n"
                , result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
