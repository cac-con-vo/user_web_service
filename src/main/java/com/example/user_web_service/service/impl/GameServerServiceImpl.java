package com.example.user_web_service.service.impl;

import com.example.user_web_service.dto.ResponseObject;
import com.example.user_web_service.dto.UserDTO;
import com.example.user_web_service.entity.*;
import com.example.user_web_service.entity.Character;
import com.example.user_web_service.exception.DuplicateException;
import com.example.user_web_service.exception.NotFoundException;
import com.example.user_web_service.exception.ResourceNotFoundException;
import com.example.user_web_service.helper.Constant;
import com.example.user_web_service.repository.*;
import com.example.user_web_service.security.userprincipal.Principal;
import com.example.user_web_service.service.GameServerService;
import io.grpc.lb.v1.ClientStats;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class GameServerServiceImpl implements GameServerService {
    private static final Logger logger = LoggerFactory.getLogger(GameServerServiceImpl.class);
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GameServerRepository gameServerRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CharacterTypeRepository characterTypeRepository;
    @Autowired
    private CharacterRepository characterRepository;



    @Override
    public ResponseEntity<ResponseObject> createGameServer(String serverName, String gameName) {
        this.checkDuplicate(serverName);
        GameServer gameServer;
        if (gameRepository.existsByName(gameName)) {
            Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userRepository.findByUsername(principal.getUsername()).orElseThrow(
                    ()-> new UsernameNotFoundException("Username not found")
            );
            List<User> users = new ArrayList<>();
            users.add(user);
            gameServer = GameServer.builder()
                    .name(serverName)
                    .status(GameServerStatus.ACTIVE)
                    .create_at(Constant.getCurrentDateTime())
                    .createBy(userRepository.getByUsername(principal.getUsername()))
                    .users(users)
                    .game(gameRepository.findByName(gameName))
                    .build();

            gameServerRepository.save(gameServer);
            CharacterType characterType = characterTypeRepository.findByName("Hunter").orElseThrow(
                    ()-> new ResourceNotFoundException("Hunter", null ," not found")
            );
            Character character = Character.builder()
                    .gameServer(gameServer)
                    .user(userRepository.getByUsername(principal.getUsername()))
                    .position(new CharacterPosition(0, 0 ,0))
                    .characterType(characterType)
                    .status(CharacterStatus.INACTIVE)
            .build();
            characterRepository.save(character);
        } else {
            throw new NotFoundException("Game:" + gameName + " not found.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject(HttpStatus.CREATED.toString(), "Create game server successfully!", null, gameServer));
    }

    @Override
    public ResponseEntity<ResponseObject> getAllGameServer(String gameName) {
        List<GameServer> list;
        if (gameRepository.existsByName(gameName)) {
            list = gameServerRepository.findAllByGame(gameRepository.findByName(gameName));
        }else{
            throw new NotFoundException("Game " + gameName +" not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(
                HttpStatus.OK.toString(),
                "Get all server of "+ gameName + "successfully!",
                null,
                list
        ));
    }

    @Override
    public ResponseEntity<ResponseObject> getAllGameServerOfUser() {
        Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(HttpStatus.OK.toString(),
                        "Get all server of" + principal.getUsername() +"successfully!",
                        null,
                        gameServerRepository.findAllByUsers(userRepository.getByUsername(principal.getUsername()))
                )
        );
    }

    @Override
    public ResponseEntity<ResponseObject> addUserToGameServer(String username, String serverName) {
        Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(username).orElseThrow(()->
                new UsernameNotFoundException("Username not found."));
        GameServer gameServer = gameServerRepository.findByName(serverName).orElseThrow(
                ()-> new NotFoundException("Game server not found.")
        );
        if(gameServer.getCreateBy().getUsername().equalsIgnoreCase(principal.getUsername())){
            if(!gameServerRepository.existsByUsers(user)){
                CharacterType characterType = characterTypeRepository.findByName("Hunter").orElseThrow(
                        ()-> new NotFoundException("Character type not found.")
                );

                //create an user with status is INACTIVE in server
                Character character = Character.builder()
                        .user(user)
                        .characterType(characterType)
                        .gameServer(gameServer)
                        .position(new CharacterPosition(0, 0 ,0))
                        .status(CharacterStatus.INACTIVE)
                        .build();
                characterRepository.save(character);
                List<User> users = gameServer.getUsers();
                users.add(user);
                gameServer.setUsers(users);
                gameServerRepository.save(gameServer);
                return ResponseEntity.status(HttpStatus.CREATED).body(
                        new ResponseObject(HttpStatus.CREATED.toString(),
                                "Add a user successfully. Wait for a friend's confirmation", null,
                                null)
                );
            }else{
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                        new ResponseObject(HttpStatus.CONFLICT.toString(),
                                "User has already in server.", null, null)
                );
            }
        }else{
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                    new ResponseObject(HttpStatus.NOT_ACCEPTABLE.toString(),
                            "User does not have the right to add others.", null, null)
            );
        }
    }

    public boolean checkDuplicate(String name) {
        boolean checkGameServer = gameServerRepository.existsByName(name);
        if(checkGameServer){
            throw new DuplicateException("Game Server: " + name + " already exists.");
        }
        return true;
    }
}
