package dev.rvincent.taskmanager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.Map;

@RestController("/api/v1/tasks")
public class TaskController {

    private final RestClient restClient;

    public TaskController(RestClient restClient) {
        this.restClient = restClient;
    }

    @GetMapping
    public ResponseEntity<Void> tasks() throws JsonProcessingException {
        String body = restClient.get()
                .uri("https://api.atlassian.com/ex/jira/a93dbe37-32bb-451a-abdc-a44f426e5eca/rest/api/3/search")
                .retrieve()
                .body(String.class);
        JsonNode jsonResponse = new ObjectMapper().readTree(body);
        System.out.println(jsonResponse);
        return ResponseEntity.noContent().build();
    }
}
