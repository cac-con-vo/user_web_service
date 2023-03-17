package com.example.user_web_service.service;

import com.example.user_web_service.dto.ResponseObject;
import com.example.user_web_service.form.LoadGameForm;
import com.example.user_web_service.form.SaveGameForm;
import com.example.user_web_service.payload.response.LoadGameResponse;
import org.springframework.http.ResponseEntity;

public interface GameService {
    ResponseEntity<ResponseObject> saveGame(SaveGameForm saveGameForm);
    ResponseEntity<LoadGameResponse> loadGame(LoadGameForm loadGameForm);
}
