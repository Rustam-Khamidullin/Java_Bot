package edu.java.client.stackoverflow;

import edu.java.dto.stackoverflow.Questions;

public interface StackOverflowClient {
    Questions getQuestions(int id);
}
