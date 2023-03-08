package com.example.user_web_service.service;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;

//import com.google.cloud.storage.Storage;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FirebaseStorageService {
//    @Autowired
//    private Storage storage;

//    public String uploadFile(MultipartFile file) throws IOException {
//        // Tạo tên tệp ngẫu nhiên
//        String fileName = UUID.randomUUID().toString();
//        // Lấy tên gốc của tệp
//        String originalFileName = file.getOriginalFilename();
//        // Lấy phần mở rộng của tệp
//        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
//        // Tạo tên tệp mới với phần mở rộng
//        String newFileName = fileName + extension;
//
//        // Tạo đường dẫn đến tệp trong Firebase Storage
//        BlobId blobId = BlobId.of("new-game-rpg.appspot.com", newFileName);
//
//        // Tạo đối tượng Blob từ tệp
//        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
//        Blob blob = storage.create(blobInfo, file.getBytes());
//
//        // Trả về URL của tệp đã tải lên
//        return blob.getMediaLink();
//    }
//
//    public void deleteFile(String fileUrl) {
//        // Tạo đường dẫn đến tệp trong Firebase Storage
//        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
//        BlobId blobId = BlobId.of("new-game-rpg.appspot.com", fileName);
//
//        // Xóa tệp
//        boolean deleted = storage.delete(blobId);
//        if (!deleted) {
//            throw new RuntimeException("Failed to delete file: " + fileUrl);
//        }
//    }
private static final Logger LOG = LoggerFactory.getLogger(FirebaseStorageService.class);
@Autowired
    private  Storage storage;
@Autowired
    private  FirebaseMessaging firebaseMessaging;

    @Value("${firebase.storage.bucket}")
    private String bucketName;

    @Autowired
    public FirebaseStorageService(Storage storage, FirebaseMessaging firebaseMessaging) {
        this.storage = storage;
        this.firebaseMessaging = firebaseMessaging;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        String newFileName = UUID.randomUUID().toString() + "." + fileExtension;
        BlobId blobId = BlobId.of(bucketName, newFileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
        byte[] fileBytes = file.getBytes();
        Blob blob = storage.create(blobInfo, fileBytes);
        LOG.info("File {} uploaded successfully", newFileName);
        return blob.getMediaLink();
    }

    public void deleteFile(String fileUrl) {
        try {
            BlobId blobId = BlobId.of(bucketName, getFileNameFromUrl(fileUrl));
            boolean deleted = storage.delete(blobId);
            if (deleted) {
                LOG.info("File {} deleted successfully", fileUrl);
            } else {
                throw new RuntimeException("Failed to delete file " + fileUrl);
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid file URL " + fileUrl, e);
        }
    }

    private String getFileNameFromUrl(String fileUrl) {
        URI uri = URI.create(fileUrl);
        String path = uri.getPath();
        return Paths.get(path).getFileName().toString();
    }

    public void sendNotification(String title, String body, String imageUrl, String topic) {
        try {
            Message message = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .setImage(imageUrl)
                            .build())
                    .setTopic(topic)
                    .build();
            String response = firebaseMessaging.send(message);
            LOG.info("Notification sent to topic {}: {}", topic, response);
        } catch (FirebaseMessagingException e) {
            LOG.error("Failed to send notification to topic " + topic, e);
        }
    }
}
