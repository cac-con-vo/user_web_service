package com.example.user_web_service.controller;

import com.example.user_web_service.dto.ResponseObject;
import com.example.user_web_service.form.GameTokenForm;
import com.example.user_web_service.service.impl.GameServerServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/v1/gameServer")
@CrossOrigin
public class GameServerController {

    @Autowired
    private GameServerServiceImpl gameServerService;


    @Operation(summary = "For create a game server")
    @PostMapping("/createGameServer")
    @SecurityRequirements
    public ResponseEntity<ResponseObject> createGameServer(
            @Valid @RequestBody GameTokenForm gameTokenForm,
            @RequestBody @Parameter(description = "Input name of game server (EX: server1, server2,..)") @RequestParam(name = "serverName") String serverName,
            @RequestBody @Parameter(description = "Input name of game (EX: Dead of souls)") @RequestParam(name = "gameName") String gameName,
            @RequestBody @Parameter(description = "Input name of game") @RequestParam(name = "usernames") List<String> usernames
            ) {
        if(gameTokenForm.getGameToken() == null || gameTokenForm.getGameToken().isEmpty() || gameTokenForm.getGameToken().isBlank()||
        serverName == null || serverName.isEmpty() || serverName.isBlank() ||
                gameName == null || gameName.isEmpty() || gameName.isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(HttpStatus.BAD_REQUEST.toString(),
                            "Please input data.", null, null)
            );
        }
        return gameServerService.createGameServer(gameTokenForm ,serverName.trim(), gameName.trim(), usernames);
    }
    @Operation(summary = "For get all game server")
    @GetMapping("/getAllGameServer")
    @SecurityRequirements
    public ResponseEntity<ResponseObject> getAllGameServer(
            @Parameter(description = "Input name of game (EX: Dead of souls)") @RequestParam(name = "gameName") String gameName
    ) {
        if(gameName == null || gameName.isEmpty() || gameName.isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(HttpStatus.BAD_REQUEST.toString(),
                            "Please input data.", null, null)
            );
        }
        return gameServerService.getAllGameServer(gameName.trim());
    }

    @Operation(summary = "For get all game server of an user")
    @PostMapping("/getAllGameServerOfUser")
    @SecurityRequirements
    public ResponseEntity<ResponseObject> getAllGameServerOfUser(
            @Valid @RequestBody GameTokenForm gameTokenForm,
            @Parameter(description = "Input name of game (EX: Dead of souls)") @RequestParam(name = "gameName") String gameName
    ) {
        if(gameTokenForm.getGameToken() == null || gameTokenForm.getGameToken().isEmpty() || gameTokenForm.getGameToken().isBlank()||
                gameName == null || gameName.isEmpty() || gameName.isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(HttpStatus.BAD_REQUEST.toString(),
                            "Please input data.", null, null)
            );
        }
        return gameServerService.getAllGameServerOfUser(gameTokenForm,gameName);
    }
}
