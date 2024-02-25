package edu.java.bot.service.command;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

public class TrackTest {
    @Test
    void generateAnswerTest() {
        try {
            Method method = Track.class.getDeclaredMethod("generateAnswer", boolean.class);
            method.setAccessible(true);

            Track track = new Track();

            String result = (String) method.invoke(track, true);
            Assertions.assertEquals("The link has been added to the tracked.", result);


            result = (String) method.invoke(track, false);
            Assertions.assertEquals("It is not possible to track this link.", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
