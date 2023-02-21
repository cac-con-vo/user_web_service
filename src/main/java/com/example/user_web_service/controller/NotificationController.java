//package com.example.user_web_service.controller;
//
//import com.example.user_web_service.entity.Notice;
//
//import com.example.user_web_service.service.NotificationService;
//import com.google.firebase.messaging.BatchResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/firebase")
//public class NotificationController {
//    @Autowired
//    NotificationService notificationService;
//
//    @PostMapping("/send-notification")
//    public BatchResponse sendNotification(@RequestBody Notice notice){
//        return notificationService.sendNotification(notice);
//    }
//}
