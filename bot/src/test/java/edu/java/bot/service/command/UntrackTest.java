package edu.java.bot.service.command;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

public class UntrackTest {
    @Test
    void generateAnswerTest() {
        try {
            Method method = Untrack.class.getDeclaredMethod("generateAnswer", boolean.class);
            method.setAccessible(true);

            Untrack untrack = new Untrack();

            String result = (String) method.invoke(untrack, true);
            Assertions.assertEquals("Link tracking has been discontinued.", result);


            result = (String) method.invoke(untrack, false);
            Assertions.assertEquals("Failed to stop tracking.", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
