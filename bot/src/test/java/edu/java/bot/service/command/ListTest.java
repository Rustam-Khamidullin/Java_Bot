package edu.java.bot.service.command;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class ListTest {
    @Test
    void generateAnswerTest() {
        try {
            Method method = List.class.getDeclaredMethod("generateAnswer", java.util.List.class);
            method.setAccessible(true);

            List list = new List();

            java.util.List<String> links = new ArrayList<>();

            String result = (String) method.invoke(list, links);
            Assertions.assertEquals("There is no tracking links.", result);

            links.add("first-link");
            links.add("second-link");

            result = (String) method.invoke(list, links);
            Assertions.assertEquals(
                "Tracking links:\n"
                    + "first-link\n"
                    + "second-link\n", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
