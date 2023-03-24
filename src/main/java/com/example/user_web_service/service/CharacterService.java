package com.example.user_web_service.service;

import com.example.user_web_service.dto.ResponseObject;
import com.example.user_web_service.form.CreateCharacterForm;
import com.example.user_web_service.form.GameTokenForm;
import com.example.user_web_service.payload.response.GetAllLevelOfGameResponse;
import com.example.user_web_service.payload.response.GetAttributeEffectResponse;
import com.example.user_web_service.payload.response.GetCharacterResponse;
import org.springframework.http.ResponseEntity;

public interface CharacterService {
    ResponseEntity<ResponseObject> creatCharacter(CreateCharacterForm createCharacterForm);
    ResponseEntity<GetCharacterResponse> getCharacter(GameTokenForm gameTokenForm, String gameName , String serverName);
    ResponseEntity<GetAttributeEffectResponse> getAttributeEffect(String gameName);
    ResponseEntity<GetAllLevelOfGameResponse> getAllLevelOfGame(String gameName);
    ResponseEntity<GetCharacterResponse> getCharacterInfo(String gameName , String serverName);
}
