package edu.java.bot.service.command;

import edu.java.bot.service.ScrapperService;
import java.lang.reflect.Method;
import java.util.ArrayList;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListTest {
    @Mock
    private ScrapperService mockedScrapperService;

    @Test
    @SneakyThrows
    public void testGenerateAnswerWithEmptyList() {
        java.util.List<String> emptyList = new ArrayList<>();

        Method method = List.class.getDeclaredMethod("generateAnswer", java.util.List.class);
        method.setAccessible(true);
        List listCommand = new List(mockedScrapperService);

        String answer = (String) method.invoke(listCommand, emptyList);

        Assertions.assertEquals("There is no tracking links.", answer);
    }

    @Test
    @SneakyThrows
    public void testGenerateAnswerWithNonEmptyList() {
        java.util.List<String> links = java.util.List.of("link1", "link2", "link3");

        Method method = List.class.getDeclaredMethod("generateAnswer", java.util.List.class);
        method.setAccessible(true);
        List listCommand = new List(mockedScrapperService);

        String answer = (String) method.invoke(listCommand, links);

        String expectedAnswer = """
            Tracking links:
            link1
            link2
            link3
            """;
        Assertions.assertEquals(expectedAnswer, answer);
    }
}
