package com.example.user_web_service.helper;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StorageService {

	Environment environment;

	public String uploadImage(MultipartFile file, String proName) {
		try {
			Path UPLOAD_PATH = Paths.get("./public/").normalize();

			if (!Files.exists(UPLOAD_PATH)) {
				Files.createDirectories(UPLOAD_PATH);
			}

			String fileName = proName + ".png";

			Path destination = Paths.get(UPLOAD_PATH + "/" + fileName);

			System.out.println(destination.toAbsolutePath());

			Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

			return Constant.SERVER_PUBLIC_FOLDER_LINK + fileName;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void deleteImage(String imgLink) {
		Runnable deleteTask = () -> {
			String UPLOAD_PATH = Paths.get("./public/").normalize().toString();

			String[] linkIndfo = imgLink.split("/");

			String fileName = linkIndfo[linkIndfo.length - 1];

			String destination = UPLOAD_PATH + "/" + fileName;

			File myObj = new File(destination);
			myObj.delete();
			return;
		};

		Thread deleteThread = new Thread(deleteTask);
		deleteThread.start();
	}
}
