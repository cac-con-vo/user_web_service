package com.example.user_web_service.controller;

import com.example.user_web_service.dto.ResponseObject;
import com.example.user_web_service.form.CreateGameServerForm;
import com.example.user_web_service.form.GameTokenForm;
import com.example.user_web_service.form.UpdateServerStatusForm;
import com.example.user_web_service.payload.response.GetGameServerOfUserResponse;
import com.example.user_web_service.service.impl.GameServerServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    @PostMapping(value = "/createGameServer", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirements
    public ResponseEntity<ResponseObject> createGameServer(
            @Valid @RequestBody CreateGameServerForm createGameServerForm
           ) {
        if(createGameServerForm.getGameTokenOfRoomMaster() == null || createGameServerForm.getGameTokenOfRoomMaster().isEmpty() || createGameServerForm.getGameTokenOfRoomMaster().isBlank()||
        createGameServerForm.getServerName() == null || createGameServerForm.getServerName().isEmpty() || createGameServerForm.getServerName().isBlank() ||
                createGameServerForm.getGameName() == null || createGameServerForm.getGameName().isEmpty() || createGameServerForm.getGameName().isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(HttpStatus.BAD_REQUEST.toString(),
                            "Please input data.", null, null)
            );
        }
        return gameServerService.createGameServer(createGameServerForm);
    }
    @Operation(summary = "For get all game server")
    @GetMapping(value = "/getAllGameServer", produces = MediaType.APPLICATION_JSON_VALUE)
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
    @PostMapping(value = "/getAllGameServerOfUser", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirements
    public ResponseEntity<GetGameServerOfUserResponse> getAllGameServerOfUser(
            @Valid @RequestBody GameTokenForm gameTokenForm,
            @Parameter(description = "Input name of game (EX: Dead of souls)") @RequestParam(name = "gameName") String gameName
    ) {
        if(gameTokenForm.getGameToken() == null || gameTokenForm.getGameToken().isEmpty() || gameTokenForm.getGameToken().isBlank()||
                gameName == null || gameName.isEmpty() || gameName.isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new GetGameServerOfUserResponse(HttpStatus.BAD_REQUEST.toString(),
                            "Please input data.", null)
            );
        }
        return gameServerService.getAllGameServerOfUser(gameTokenForm,gameName);
    }
    @PutMapping(value = "/updateStatus" , produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "For update server status")
    public ResponseEntity<ResponseObject> changeUserStatus(@Valid @RequestBody UpdateServerStatusForm updateServerStatusForm
    ) {
        return gameServerService.updateStatusGameServer(updateServerStatusForm);
    }
}
