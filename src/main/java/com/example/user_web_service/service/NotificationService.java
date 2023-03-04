package com.example.user_web_service.service;

import com.example.user_web_service.entity.Notice;
import com.example.user_web_service.form.NotificationMessage;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class NotificationService {
    @Autowired
    FirebaseMessaging firebaseMessaging;

    public String sendNotificationByToken(NotificationMessage notificationMessage){
        Notification notification = Notification
                .builder()
                .setTitle(notificationMessage.getTitle())
                .setBody(notificationMessage.getBody())
                .setImage(notificationMessage.getImage())
                .build();

        Message message = Message
                .builder()
                .setToken(notificationMessage.getRecipientToken())
                .setNotification(notification)
                .putAllData(notificationMessage.getData())
                .build();

        try{
            firebaseMessaging.send(message);
            return "Success Sending Notification";
        }catch (FirebaseMessagingException e){
            e.printStackTrace();
            return "Error Sending Notification";
        }
    }
}
