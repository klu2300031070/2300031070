package com.afford.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afford.entity.NotificationDTO;
import com.afford.service.NotificationService;

@RestController
@RequestMapping("/api")
public class NotificationController {
	@Autowired
    private  NotificationService notificationService;

    @GetMapping("/notifications")
    public List<NotificationDTO> getNotifications() throws Exception {
        return notificationService.getNotifications();
    }
}