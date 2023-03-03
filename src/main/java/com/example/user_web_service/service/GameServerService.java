package com.example.user_web_service.service;

import com.example.user_web_service.dto.ResponseObject;
import com.example.user_web_service.entity.User;
import org.springframework.http.ResponseEntity;

public interface GameServerService {
    ResponseEntity<ResponseObject> createGameServer(String serverName, String gameName);
    ResponseEntity<ResponseObject> getAllGameServer(String gameName);

    ResponseEntity<ResponseObject> getAllGameServerOfUser(String gameName);

    ResponseEntity<ResponseObject> addUserToGameServer(String username, String gameName ,String serverName);

}
