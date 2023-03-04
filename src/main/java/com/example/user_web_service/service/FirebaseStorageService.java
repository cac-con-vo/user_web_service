package com.example.user_web_service.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;

import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class FirebaseStorageService {
    @Autowired
    private Storage storage;


    public String uploadFile(MultipartFile file) throws IOException {
        // Tạo tên tệp ngẫu nhiên
        String fileName = UUID.randomUUID().toString();
        // Lấy tên gốc của tệp
        String originalFileName = file.getOriginalFilename();
        // Lấy phần mở rộng của tệp
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        // Tạo tên tệp mới với phần mở rộng
        String newFileName = fileName + extension;

        // Tạo đường dẫn đến tệp trong Firebase Storage
        BlobId blobId = BlobId.of("web-game-rpg.appspot.com", newFileName);

        // Tạo đối tượng Blob từ tệp
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
        Blob blob = storage.create(blobInfo, file.getBytes());

        // Trả về URL của tệp đã tải lên
        return blob.getMediaLink();
    }

    public void deleteFile(String fileUrl) {
        // Tạo đường dẫn đến tệp trong Firebase Storage
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        BlobId blobId = BlobId.of("web-game-rpg.appspot.com", fileName);

        // Xóa tệp
        boolean deleted = storage.delete(blobId);
        if (!deleted) {
            throw new RuntimeException("Failed to delete file: " + fileUrl);
        }
    }
}
