package com.example.user_web_service.config;

import com.google.auth.oauth2.GoogleCredentials;
//import com.google.cloud.storage.Storage;
//import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;


import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class FirebaseConfig {
    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException {
        GoogleCredentials googleCredentials=GoogleCredentials
                .fromStream( new ClassPathResource("firebase-service-account.json").getInputStream());
        FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                .setCredentials(googleCredentials)
                .setStorageBucket("new-game-rpg.appspot.com")
                .build();
        FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, "new-game-rpg");
        return FirebaseMessaging.getInstance(app);
    }

    @Bean
    Storage storage() throws IOException {
        return StorageOptions.getDefaultInstance().getService();
    }
}
