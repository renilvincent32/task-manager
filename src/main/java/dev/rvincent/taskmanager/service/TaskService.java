package dev.rvincent.taskmanager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class TaskService {

    private final RestClient restClient;

    public TaskService(RestClient restClient) {
        this.restClient = restClient;
    }

    public void getTasks() throws JsonProcessingException {
        String body = restClient.get()
                .uri("https://api.atlassian.com/ex/jira/a93dbe37-32bb-451a-abdc-a44f426e5eca/rest/api/3/search")
                .retrieve()
                .body(String.class);
        JsonNode jsonResponse = new ObjectMapper().readTree(body);
        System.out.println(jsonResponse);
    }
}
