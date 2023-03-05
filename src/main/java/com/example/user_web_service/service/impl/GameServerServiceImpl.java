package com.example.user_web_service.service.impl;

import com.example.user_web_service.dto.ResponseObject;
import com.example.user_web_service.entity.*;
import com.example.user_web_service.entity.Character;
import com.example.user_web_service.exception.DuplicateException;
import com.example.user_web_service.exception.NotFoundException;
import com.example.user_web_service.exception.ResourceNotFoundException;
import com.example.user_web_service.form.GameTokenForm;
import com.example.user_web_service.helper.Constant;
import com.example.user_web_service.repository.*;
import com.example.user_web_service.security.jwt.GameTokenException;
import com.example.user_web_service.security.jwt.GameTokenProvider;
import com.example.user_web_service.service.GameServerService;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private GameTokenProvider gameTokenProvider;

    @Override
    public ResponseEntity<ResponseObject> createGameServer(GameTokenForm gameTokenForm, String serverName, String gameName, List<String> usernames) {
        return  gameTokenProvider.findByToken(DigestUtils.sha3_256Hex(gameTokenForm.getGameToken()))
                .map(gameTokenProvider::verifyExpiration)
                .map(GameToken::getUser)
                .map(user -> {
                    //kiem tra xem game co ton tai k
                    Game game = gameRepository.findByName(gameName).orElseThrow(
                            ()-> new NotFoundException("Game not found")
                    );
                    //kiem tra server co trung ten k
                    this.checkDuplicate(serverName, game);
                    GameServer gameServer;
                    //add user tao server vao list
                    List<User> users = new ArrayList<>();
                    users.add(user);
                    //add cac user tham gia vao server vao list
                    if(usernames.size() > 0){
                        for (String username: usernames
                        ) {
                            User user_join = userRepository.findByUsername(username).orElseThrow(
                                    ()-> new UsernameNotFoundException("Username: " + username + " not found")
                            );
                            users.add(user_join);
                        }
                    }
                    gameServer = GameServer.builder()
                            .name(serverName)
                            .status(GameServerStatus.ACTIVE)
                            .create_at(Constant.getCurrentDateTime())
                            .createBy(userRepository.getByUsername(user.getUsername()))
                            .users(users)
                            .game(game)
                            .build();

                    gameServerRepository.save(gameServer);
                    //tao cac nhan vat cho cac user tham gia vao game server voi trang thai INACTIVE
                    CharacterType characterType = characterTypeRepository.findByName("Hunter").orElseThrow(
                            ()-> new ResourceNotFoundException("Hunter", null ," not found")
                    );
                    for (User user_join: gameServer.getUsers()
                    ) {
                        Character character = Character.builder()
                                .gameServer(gameServer)
                                .user(user_join)
                                .position(new CharacterPosition(0, 0 ,0))
                                .characterType(characterType)
                                .status(CharacterStatus.INACTIVE)
                                .build();
                        characterRepository.save(character);
                    }
                    return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject(
                            HttpStatus.CREATED.toString(),
                            "Create game server successfully!", null, gameServer));
                })
                .orElseThrow(() -> new GameTokenException("Game token is not in database!"));

    }

    @Override
    public ResponseEntity<ResponseObject> getAllGameServer(String gameName) {
        List<GameServer> list;
        if (gameRepository.existsByName(gameName)) {
            list = gameServerRepository.findAllByGame(gameRepository.findByName(gameName).orElseThrow(
                    ()-> new NotFoundException("Game not found")
            ));
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
    public ResponseEntity<ResponseObject> getAllGameServerOfUser(GameTokenForm gameTokenForm, String gameName) {
        return  gameTokenProvider.findByToken(DigestUtils.sha3_256Hex(gameTokenForm.getGameToken()))
                .map(gameTokenProvider::verifyExpiration)
                .map(GameToken::getUser)
                .map(user -> {
                    Game game = gameRepository.findByName(gameName).orElseThrow(
                            ()-> new NotFoundException("Game not found")
                    );
                    return ResponseEntity.status(HttpStatus.OK).body(
                            new ResponseObject(HttpStatus.OK.toString(),
                                    "Get all server of" + user.getUsername() +"successfully!",
                                    null,
                                    gameServerRepository.findAllByUsersAndGame(userRepository.getByUsername(user.getUsername()),game)
                            )
                    );
                })
                .orElseThrow(() -> new GameTokenException("Game token is not in database!"));
    }

    public boolean checkDuplicate(String name, Game game) {
        boolean checkGameServer = gameServerRepository.existsByNameAndGame(name, game);
        if(checkGameServer){
            throw new DuplicateException("Game Server: " + name + " already exists.");
        }
        return true;
    }
}
