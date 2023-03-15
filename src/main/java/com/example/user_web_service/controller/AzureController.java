package com.example.user_web_service.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import com.example.user_web_service.service.AzureBlobService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/files")
public class AzureController {

    @Autowired
    private AzureBlobService azureBlobAdapter;
    @Operation(summary = "Upload files to Azure Storage")
    @PostMapping( consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> upload
            (@RequestParam MultipartFile file)
            throws IOException {

        String fileName = azureBlobAdapter.upload(file);
        return ResponseEntity.ok(fileName);
    }
    @Operation(summary = "Get all files to Azure Storage")
    @GetMapping
    public ResponseEntity<List<String>> getAllBlobs() {

        List<String> items = azureBlobAdapter.listBlobs();
        return ResponseEntity.ok(items);
    }
    @Operation(summary = "Delete file by name of file from Azure Storage")
    @DeleteMapping(value = "/{filename}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> delete
            (@RequestParam String fileName) {

        azureBlobAdapter.deleteBlob(fileName);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "Dowload file by name of file from Azure Storage")
    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getFile
            (@RequestParam String fileName)
            throws URISyntaxException {

        ByteArrayResource resource =
                new ByteArrayResource(azureBlobAdapter
                        .getFile(fileName));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\""
                        + fileName + "\"");

        return ResponseEntity.ok().contentType(MediaType
                        .APPLICATION_OCTET_STREAM)
                .headers(headers).body(resource);
    }
}