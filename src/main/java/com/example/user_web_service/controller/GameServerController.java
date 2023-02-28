package com.example.user_web_service.controller;

import com.example.user_web_service.dto.ResponseObject;
import com.example.user_web_service.service.impl.GameServerServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
            @Parameter(description = "Input name of game server (EX: server1, server2,..)") @RequestParam(name = "serverName") String serverName,
            @Parameter(description = "Input name of game (EX: Dead of souls)") @RequestParam(name = "gameName") String gameName
            ) {
        return gameServerService.createGameServer(serverName.trim(), gameName.trim());
    }
    @Operation(summary = "For get all game server")
    @GetMapping("/getAllGameServer")
    @SecurityRequirements
    public ResponseEntity<ResponseObject> getAllGameServer(
            @Parameter(description = "Input name of game (EX: Dead of souls)") @RequestParam(name = "gameName") String gameName
    ) {
        return gameServerService.getAllGameServer(gameName.trim());
    }

    @Operation(summary = "For get all game server of an user")
    @GetMapping("/getAllGameServerOfUser")
    @SecurityRequirements
    public ResponseEntity<ResponseObject> getAllGameServerOfUser() {
        return gameServerService.getAllGameServerOfUser();
    }

    @Operation(summary = "For add an user to server")
    @PostMapping("/addUserToGame")
    @SecurityRequirements
    public ResponseEntity<ResponseObject> addUserToGameServer(
            @Parameter(description = "Input username of user") @RequestParam(name = "username") String username,
            @Parameter(description = "Input name of server game (EX: server1, server2)") @RequestParam(name = "serverName") String serverName
    ) {
        if(username.isEmpty() || username.isBlank() || username == null || serverName.isEmpty() || serverName.isBlank() || serverName == null ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(HttpStatus.BAD_REQUEST.toString(),
                            "Please input username or server name.",
                            null, null)
            );
        }
        return gameServerService.addUserToGameServer(username, serverName);
    }

}
