package com.example.user_web_service.controller;

import com.example.user_web_service.dto.ResponseObject;
import com.example.user_web_service.form.CreateCharacterForm;
import com.example.user_web_service.form.GameTokenForm;
import com.example.user_web_service.service.impl.CharacterServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.hibernate.criterion.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/character")
@CrossOrigin
public class CharacterController {
    @Autowired
    private CharacterServiceImpl characterService;


    @Operation(summary = "For create a character")
    @PostMapping("/createCharacter")
    @SecurityRequirements
    public ResponseEntity<ResponseObject> createCharacter(
            @RequestBody @Valid CreateCharacterForm createCharacterForm
    ) {
        if(createCharacterForm.getGameToken() == null || createCharacterForm.getGameToken().isBlank() || createCharacterForm.getGameToken().isEmpty() ||
                createCharacterForm.getCharacterName() == null || createCharacterForm.getCharacterName().isEmpty() || createCharacterForm.getCharacterName().isBlank() ||
                createCharacterForm.getGameName() == null || createCharacterForm.getGameName().isEmpty() || createCharacterForm.getGameName().isBlank() ||
                createCharacterForm.getCharacterName() == null || createCharacterForm.getCharacterName().isEmpty() || createCharacterForm.getCharacterName().isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(HttpStatus.BAD_REQUEST.toString(),
                            "Please input data.", null, null)
            );
        }
        return characterService.creatCharacter(createCharacterForm);
    }

    @Operation(summary = "For get a character for user")
    @GetMapping("/getCharacter")
    @SecurityRequirements
    public ResponseEntity<ResponseObject> getCharacter(
           GameTokenForm gameTokenForm,
           @Parameter(description = "Input name of game", example = "Dead of souls") @RequestParam(name = "gameName") String gameName,
           @Parameter(description = "Input name of server", example = "server1") @RequestParam(name = "serverName") String serverName
    ) {
        if(gameTokenForm.getGameToken() == null || gameTokenForm.getGameToken().isBlank() || gameTokenForm.getGameToken().isEmpty() ||
        serverName == null || serverName.isEmpty() || serverName.isBlank() ||
                gameName == null || gameName.isEmpty() || gameName.isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(HttpStatus.BAD_REQUEST.toString(),
                            "Please input data.", null, null)
            );
        }
        return characterService.getCharacter(gameTokenForm, gameName.trim() ,serverName.trim());
    }

    @Operation(summary = "For get effects of attribute for user")
    @GetMapping("/getAttributeEffect")
    @SecurityRequirements
    public ResponseEntity<ResponseObject> getAttributeEffect(
            GameTokenForm gameTokenForm,
            @Parameter(description = "Input name of game", example = "Dead of souls") @RequestParam(name = "gameName") String gameName
            ) {
        if(gameTokenForm.getGameToken() == null || gameTokenForm.getGameToken().isBlank() ||
                gameTokenForm.getGameToken().isEmpty()||
                gameName == null || gameName.isEmpty() || gameName.isBlank()
               ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(HttpStatus.BAD_REQUEST.toString(),
                            "Please input data.", null, null)
            );
        }
        return characterService.getAttributeEffect(gameTokenForm, gameName);
    }

    @Operation(summary = "For get levels of game for user")
    @GetMapping("/getAllLevelOfGame")
    @SecurityRequirements
    public ResponseEntity<ResponseObject> getAllLevelOfGame(
            GameTokenForm gameTokenForm,
            @Parameter(description = "Input name of game", example = "Dead of souls") @RequestParam(name = "gameName") String gameName
    ) {
        if(gameTokenForm.getGameToken() == null || gameTokenForm.getGameToken().isBlank() ||
                gameTokenForm.getGameToken().isEmpty()||
                gameName == null || gameName.isEmpty() || gameName.isBlank()
        ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(HttpStatus.BAD_REQUEST.toString(),
                            "Please input data.", null, null)
            );
        }
        return characterService.getAllLevelOfGame(gameTokenForm, gameName.trim());
    }
}
