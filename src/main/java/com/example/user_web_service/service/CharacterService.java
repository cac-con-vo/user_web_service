package com.example.user_web_service.service;

import com.example.user_web_service.dto.ResponseObject;
import com.example.user_web_service.form.GameTokenForm;
import org.springframework.http.ResponseEntity;

public interface CharacterService {
    ResponseEntity<ResponseObject> creatCharacter(String name, String serverName);
    ResponseEntity<ResponseObject> getCharacter(GameTokenForm gameTokenForm, String serverName);
}
