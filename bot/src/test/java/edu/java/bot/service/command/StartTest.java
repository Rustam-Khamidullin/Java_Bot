package edu.java.bot.service.command;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

public class StartTest {
    @Test
    void generateAnswerTest() {
        try {
            Method method = Start.class.getDeclaredMethod("generateAnswer", boolean.class);
            method.setAccessible(true);

            Start start = new Start();

            String result = (String) method.invoke(start, true);
            Assertions.assertEquals("The user has been successfully registered.", result);


            result = (String) method.invoke(start, false);
            Assertions.assertEquals("User registration failed.", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
