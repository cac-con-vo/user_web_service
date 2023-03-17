package com.example.user_web_service.controller;

import com.example.user_web_service.dto.ResponseObject;
import com.example.user_web_service.form.LoadGameForm;
import com.example.user_web_service.form.LoginForm;
import com.example.user_web_service.form.SaveGameForm;
import com.example.user_web_service.payload.response.LoadGameResponse;
import com.example.user_web_service.service.impl.GameServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/game")
@CrossOrigin
public class GameController {
    @Autowired
    private GameServiceImpl gameService;

    @Operation(summary = "For save game", description = "test")
    @PostMapping(value="/saveGame", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject> saveGame(@Valid @RequestBody SaveGameForm saveGameForm) {
        if(saveGameForm.getGameName().isEmpty() || saveGameForm.getGameName().isBlank() || saveGameForm.getGameName() == null ||
                saveGameForm.getServerName().isEmpty() || saveGameForm.getServerName().isBlank() || saveGameForm.getServerName() == null ||
                saveGameForm.getGameToken().isEmpty() || saveGameForm.getGameToken().isBlank() || saveGameForm.getGameToken() == null ||
                saveGameForm.getJsonString().isEmpty() || saveGameForm.getJsonString().isBlank() || saveGameForm.getJsonString() == null ||
                saveGameForm.getDataSharing().isEmpty() || saveGameForm.getDataSharing().isBlank() || saveGameForm.getDataSharing() == null ){
            return new ResponseEntity<ResponseObject>(new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Please input data", null, null), HttpStatus.BAD_REQUEST);
        }
        return gameService.saveGame(saveGameForm);
    }

    @Operation(summary = "For load game", description = "test")
    @GetMapping(value="/loadGame", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoadGameResponse> loadGame(@Valid LoadGameForm loadGameForm) {
        if(loadGameForm.getGameName().isEmpty() || loadGameForm.getGameName().isBlank() || loadGameForm.getGameName() == null ||
                loadGameForm.getServerName().isEmpty() || loadGameForm.getServerName().isBlank() || loadGameForm.getServerName() == null ||
                loadGameForm.getGameToken().isEmpty() || loadGameForm.getGameToken().isBlank() || loadGameForm.getGameToken() == null
                 ){
            return new ResponseEntity<LoadGameResponse>(new LoadGameResponse(HttpStatus.BAD_REQUEST.toString(), "Please input data", null, null), HttpStatus.BAD_REQUEST);
        }
        return gameService.loadGame(loadGameForm);
    }
}
