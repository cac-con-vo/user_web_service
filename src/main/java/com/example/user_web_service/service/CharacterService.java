package com.example.user_web_service.service;

import com.example.user_web_service.dto.ResponseObject;
import com.example.user_web_service.form.GameTokenForm;
import org.springframework.http.ResponseEntity;

public interface CharacterService {
    ResponseEntity<ResponseObject> creatCharacter(GameTokenForm gameTokenForm, String name, String gameName, String serverName);
    ResponseEntity<ResponseObject> getCharacter(GameTokenForm gameTokenForm, String gameName ,String serverName);
    ResponseEntity<ResponseObject> getAttributeEffect(GameTokenForm gameTokenForm, String gameName);
    ResponseEntity<ResponseObject> getAllLevelOfGame(GameTokenForm gameTokenForm, String gameName);

}
