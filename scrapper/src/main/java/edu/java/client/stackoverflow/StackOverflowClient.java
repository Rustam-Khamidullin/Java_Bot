package edu.java.client.stackoverflow;

import edu.java.dto.stackoverflow.Question;
import org.springframework.web.client.RestClient;

public interface StackOverflowClient {
    Question getQuestion(int id);
}
