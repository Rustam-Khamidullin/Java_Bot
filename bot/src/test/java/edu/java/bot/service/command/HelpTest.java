package edu.java.bot.service.command;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HelpTest {
    @Mock
    private Command mockedCommand1;
    @Mock
    private Command mockedCommand2;

    @SneakyThrows @Test
    public void testGenerateAnswer() {
        Map<String, Command> commands = new HashMap<>();
        commands.put("command1", mockedCommand1);
        commands.put("command2", mockedCommand2);

        when(mockedCommand1.description()).thenReturn("Description for command1");
        when(mockedCommand2.description()).thenReturn("Description for command2");

        Method method = Help.class.getDeclaredMethod("generateAnswer");
        method.setAccessible(true);
        Help help = new Help(commands);

        String answer = (String) method.invoke(help);

        String expectedAnswer = "List of commands:\n" +
            "command1 - Description for command1\n" +
            "command2 - Description for command2\n";
        Assertions.assertEquals(expectedAnswer, answer);
    }
}

