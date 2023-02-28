package com.example.user_web_service.controller;

import com.example.user_web_service.dto.ResponseObject;
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
            @Parameter(description = "Input name of character ",example = "concamap") @RequestParam(name = "characterName") String characterName,
            @Parameter(description = "Input name of server", example = "server1") @RequestParam(name = "serverName") String serverName
    ) {
        if(characterName == null || characterName.isEmpty() || characterName.isBlank() ||
                serverName == null || serverName.isEmpty() || serverName.isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(HttpStatus.BAD_REQUEST.toString(),
                            "Please input data.", null, null)
            );
        }
        return characterService.creatCharacter(characterName.trim(), serverName.trim());
    }

    @Operation(summary = "For get a character for user")
    @GetMapping("/getCharacter")
    @SecurityRequirements
    public ResponseEntity<ResponseObject> getCharacter(
           GameTokenForm gameTokenForm,
            @Parameter(description = "Input name of server", example = "server1") @RequestParam(name = "serverName") String serverName
    ) {
        if(gameTokenForm.getGameToken() == null || gameTokenForm.getGameToken().isBlank() || gameTokenForm.getGameToken().isEmpty() ||
        serverName == null || serverName.isEmpty() || serverName.isBlank() ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(HttpStatus.BAD_REQUEST.toString(),
                            "Please input data.", null, null)
            );
        }
        return characterService.getCharacter(gameTokenForm, serverName.trim());
    }
}
