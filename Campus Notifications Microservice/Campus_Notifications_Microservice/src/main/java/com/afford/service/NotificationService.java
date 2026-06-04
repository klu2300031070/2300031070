package com.afford.service;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afford.controller.ApiService;
import com.afford.entity.NotificationDTO;
import com.afford.entity.NotificationResponse;
import com.afford.entity.NotificationType;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class NotificationService {

    @Autowired
    private ApiService apiService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<NotificationDTO> getNotifications() throws Exception {

       String json = apiService.fetchData();

        JsonNode root = objectMapper.readTree(json);

        if (root.has("message")) {

            String message = root.get("message").asText();

            if ("invalid authorization token".equalsIgnoreCase(message)) {
                throw new RuntimeException("Invalid Authorization Token");
            }
        }
        PriorityQueue<NotificationDTO> pq =
                new PriorityQueue<>((a, b) ->
                        Integer.compare(
                                getPriority(b.getType()),
                                getPriority(a.getType())));
        
        NotificationResponse response =objectMapper.readValue(json, NotificationResponse.class);
        pq.addAll(response.getNotifications());
       // pq.addAll(notifications);
        List<NotificationDTO> result = new ArrayList<>();

        while (!pq.isEmpty()) {
            result.add(pq.poll());
        }

        return result;
    }

    private int getPriority(String type) {

        NotificationType notificationType =
                NotificationType.valueOf(type.toUpperCase());

        return switch (notificationType) {
            case EVENT -> 1;
            case RESULT -> 2;
            case PLACEMENT -> 3;
        };
    }
}