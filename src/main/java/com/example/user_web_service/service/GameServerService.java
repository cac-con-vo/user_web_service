package com.example.user_web_service.service;

import com.example.user_web_service.dto.ResponseObject;
import com.example.user_web_service.entity.User;
import com.example.user_web_service.form.GameTokenForm;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GameServerService {
    ResponseEntity<ResponseObject> createGameServer(GameTokenForm gameTokenForm,String serverName, String gameName, List<String> usernames);
    ResponseEntity<ResponseObject> getAllGameServer(String gameName);
    ResponseEntity<ResponseObject> getAllGameServerOfUser(GameTokenForm gameTokenForm,String gameName);

}
