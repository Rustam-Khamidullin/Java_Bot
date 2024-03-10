package edu.java.client.stackoverflow;

import edu.java.dto.stackoverflow.Questions;
import org.springframework.http.ResponseEntity;

public interface StackOverflowClient {
    ResponseEntity<Questions> getQuestions(int id);
}
