package com.example.user_web_service.controller;


import com.example.user_web_service.form.NotificationMessage;
import com.example.user_web_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    @Autowired
    NotificationService notificationService;

    @PostMapping("/send-notification")
    public String sendNotificationByToken(@RequestBody NotificationMessage notificationMessage){
        return notificationService.sendNotificationByToken(notificationMessage);
    }
}

