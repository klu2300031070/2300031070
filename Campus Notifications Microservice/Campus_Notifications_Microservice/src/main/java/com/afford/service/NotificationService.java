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

        /*String json = apiService.fetchData();

        JsonNode root = objectMapper.readTree(json);

        if (root.has("message")) {

            String message = root.get("message").asText();

            if ("invalid authorization token".equalsIgnoreCase(message)) {
                throw new RuntimeException("Invalid Authorization Token");
            }
        }*/

        List<NotificationDTO> notifications = new ArrayList<>();

        NotificationDTO n1 = new NotificationDTO();
        n1.setID("55fa4c29-f44a-4462-8549-3d09b0ccb60f");
        n1.setType("PLACEMENT");
        n1.setMessage("TSMC Hiring Drive");
        n1.setTimestamp("2026-05-29 19:14:38");
        notifications.add(n1);

        NotificationDTO n2 = new NotificationDTO();
        n2.setID("f93952d9-e978-45b3-8396-ce0bde89a92c");
        n2.setType("RESULT");
        n2.setMessage("Mid-Sem Results Published");
        n2.setTimestamp("2026-05-30 07:14:33");
        notifications.add(n2);

        NotificationDTO n3 = new NotificationDTO();
        n3.setID("ce4ec4c9-026d-4940-a727-9ecc5e891b6a");
        n3.setType("PLACEMENT");
        n3.setMessage("Microsoft Corporation Hiring");
        n3.setTimestamp("2026-05-30 09:45:10");
        notifications.add(n3);

        NotificationDTO n4 = new NotificationDTO();
        n4.setID("a1b2c3d4-e5f6-7890-abcd-ef1234567890");
        n4.setType("EVENT");
        n4.setMessage("Annual Technical Fest Registration Open");
        n4.setTimestamp("2026-05-31 10:00:00");
        notifications.add(n4);

        NotificationDTO n5 = new NotificationDTO();
        n5.setID("b2c3d4e5-f6a7-8901-bcde-f23456789012");
        n5.setType("PLACEMENT");
        n5.setMessage("Amazon SDE Recruitment");
        n5.setTimestamp("2026-05-31 11:30:00");
        notifications.add(n5);

        NotificationDTO n6 = new NotificationDTO();
        n6.setID("c3d4e5f6-a7b8-9012-cdef-345678901234");
        n6.setType("RESULT");
        n6.setMessage("End Semester Timetable Released");
        n6.setTimestamp("2026-05-31 14:15:00");
        notifications.add(n6);

        NotificationDTO n7 = new NotificationDTO();
        n7.setID("d4e5f6a7-b8c9-0123-def0-456789012345");
        n7.setType("EVENT");
        n7.setMessage("Hackathon 2026 Registration Started");
        n7.setTimestamp("2026-06-01 09:00:00");
        notifications.add(n7);

        NotificationDTO n8 = new NotificationDTO();
        n8.setID("e5f6a7b8-c9d0-1234-ef01-567890123456");
        n8.setType("PLACEMENT");
        n8.setMessage("Google Internship Opportunities");
        n8.setTimestamp("2026-06-01 12:45:00");
        notifications.add(n8);

        NotificationDTO n9 = new NotificationDTO();
        n9.setID("f6a7b8c9-d0e1-2345-f012-678901234567");
        n9.setType("RESULT");
        n9.setMessage("Lab Internal Marks Uploaded");
        n9.setTimestamp("2026-06-02 08:20:00");
        notifications.add(n9);

        NotificationDTO n10 = new NotificationDTO();
        n10.setID("a7b8c9d0-e1f2-3456-0123-789012345678");
        n10.setType("EVENT");
        n10.setMessage("Guest Lecture on AI and Cloud Computing");
        n10.setTimestamp("2026-06-02 16:00:00");
        notifications.add(n10);

        PriorityQueue<NotificationDTO> pq =
                new PriorityQueue<>((a, b) ->
                        Integer.compare(
                                getPriority(b.getType()),
                                getPriority(a.getType())));
        
        //NotificationResponse response =objectMapper.readValue(json, NotificationResponse.class);
        //pq.addAll(response.getNotifications());
        pq.addAll(notifications);
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