package com.example.user_web_service.service.impl;

import com.example.user_web_service.dto.ResponseObject;
import com.example.user_web_service.entity.*;
import com.example.user_web_service.entity.Character;
import com.example.user_web_service.exception.DuplicateException;
import com.example.user_web_service.exception.NotFoundException;
import com.example.user_web_service.exception.ResourceNotFoundException;
import com.example.user_web_service.form.GameTokenForm;
import com.example.user_web_service.helper.Constant;
import com.example.user_web_service.redis.RedisValueCache;
import com.example.user_web_service.redis.locker.DistributedLocker;
import com.example.user_web_service.redis.locker.LockExecutionResult;
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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    @Autowired
    private RedisValueCache redisValueCache;
    @Autowired
    private DistributedLocker distributedLocker;

//    @Override
//    public ResponseEntity<ResponseObject> createGameServer(GameTokenForm gameTokenForm, String serverName, String gameName, List<String> usernames) {
//        return  gameTokenProvider.findByToken(DigestUtils.sha3_256Hex(gameTokenForm.getGameToken()))
//                .map(gameTokenProvider::verifyExpiration)
//                .map(GameToken::getUser)
//                .map(user -> {
//                    //kiem tra xem game co ton tai k
//                    Game game = gameRepository.findByName(gameName).orElseThrow(
//                            ()-> new NotFoundException("Game not found")
//                    );
//                    //kiem tra server co trung ten k
//                    this.checkDuplicate(serverName, game);
//                    GameServer gameServer;
//                    //add user tao server vao list
//                    List<User> users = new ArrayList<>();
//                    users.add(user);
//                    //add cac user tham gia vao server vao list
//                    if(usernames.size() > 0){
//                        for (String username: usernames
//                        ) {
//                            User user_join = userRepository.findByUsername(username).orElseThrow(
//                                    ()-> new UsernameNotFoundException("Username: " + username + " not found")
//                            );
//                            users.add(user_join);
//                        }
//                    }
//                    gameServer = GameServer.builder()
//                            .name(serverName)
//                            .status(GameServerStatus.ACTIVE)
//                            .create_at(Constant.getCurrentDateTime())
//                            .createBy(userRepository.getByUsername(user.getUsername()))
//                            .users(users)
//                            .game(game)
//                            .build();
//
//                    gameServerRepository.save(gameServer);
//                    //tao cac nhan vat cho cac user tham gia vao game server voi trang thai INACTIVE
//                    CharacterType characterType = characterTypeRepository.findByName("Hunter").orElseThrow(
//                            ()-> new ResourceNotFoundException("Hunter", null ," not found")
//                    );
//                    for (User user_join: gameServer.getUsers()
//                    ) {
//                        Character character = Character.builder()
//                                .gameServer(gameServer)
//                                .user(user_join)
//                                .position(new CharacterPosition(0, 0 ,0))
//                                .characterType(characterType)
//                                .status(CharacterStatus.INACTIVE)
//                                .build();
//                        characterRepository.save(character);
//                    }
//                    return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject(
//                            HttpStatus.CREATED.toString(),
//                            "Create game server successfully!", null, gameServer));
//                })
//                .orElseThrow(() -> new GameTokenException("Game token is not in database!"));
//
//    }

    @Override
    public ResponseEntity<ResponseObject> createGameServer(GameTokenForm gameTokenForm, String serverName, String gameName, List<String> usernames) {
        return gameTokenProvider.findByToken(DigestUtils.sha3_256Hex(gameTokenForm.getGameToken()))
                .map(gameTokenProvider::verifyExpiration)
                .map(GameToken::getUser)
                .map(user -> {
// kiểm tra xem game có tồn tại không
                    Game game = gameRepository.findByName(gameName).orElseThrow(
                            () -> new NotFoundException("Game not found")
                    );
// kiểm tra server có trùng tên không
                    this.checkDuplicate(serverName, game);
                    GameServer gameServer;
                    // add user tạo server vào danh sách
                    List<User> users = new ArrayList<>();
                    users.add(user);

                    // add các user tham gia vào server vào danh sách
                    if (usernames.size() > 0) {
                        for (String username : usernames) {
                            User user_join = userRepository.findByUsername(username).orElseThrow(
                                    () -> new UsernameNotFoundException("Username: " + username + " not found")
                            );
                            users.add(user_join);
                        }
                    }

                    // tạo game server mới
                    gameServer = GameServer.builder()
                            .name(serverName)
                            .status(GameServerStatus.ACTIVE)
                            .game(game)
                            .users(users)
                            .build();

                    // cache game server vào Redis
                    String gameServerKey = String.format("game_server_%s", gameServer.getId());
                    distributedLocker.lock(gameServerKey, 10, 5, () -> {
                        redisValueCache.cache(gameServerKey, gameServer);
                        return null;
                    });

                    return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject(
                            HttpStatus.CREATED.toString(),
                            "Create game server successfully!", null, gameServer));
                })
                .orElseThrow(() -> new GameTokenException("Game token is not in database!"));
    }


    //    @Override
//    public ResponseEntity<ResponseObject> getAllGameServer(String gameName) {
//        List<GameServer> list;
//        if (gameRepository.existsByName(gameName)) {
//            list = gameServerRepository.findAllByGame(gameRepository.findByName(gameName).orElseThrow(
//                    ()-> new NotFoundException("Game not found")
//            ));
//        }else{
//            throw new NotFoundException("Game " + gameName +" not found.");
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(
//                HttpStatus.OK.toString(),
//                "Get all server of "+ gameName + "successfully!",
//                null,
//                list
//        ));
//    }
    @Override
    public ResponseEntity<ResponseObject> getAllGameServer(String gameName) {
        String cacheKey = "allGameServer:" + gameName;
        List<GameServer> list = null;

        // Attempt to get data from cache
        Object cachedData = redisValueCache.getCacheValue(cacheKey);
        if (cachedData != null) {
            list = (List<GameServer>) cachedData;
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(
                    HttpStatus.OK.toString(),
                    "Get all server of " + gameName + " successfully from cache!",
                    null,
                    list
            ));
        }

        // If not in cache, attempt to acquire a lock to retrieve data from database
        LockExecutionResult<List<GameServer>> result = distributedLocker.lock(
                cacheKey,
                10,  // lock timeout in seconds
                30,  // maximum time to acquire lock in seconds
                () -> {
                    if (gameRepository.existsByName(gameName)) {
                        List<GameServer> servers = gameServerRepository.findAllByGame(gameRepository.findByName(gameName).orElseThrow(
                                () -> new NotFoundException("Game not found")
                        ));
                        redisValueCache.cache(cacheKey, servers);
                        return servers;
                    } else {
                        throw new NotFoundException("Game " + gameName + " not found.");
                    }
                }
        );

        if (result.isLockAcquired()) {
            list = result.getResultIfLockAcquired();
        }

        // Check if there was an exception during lock acquisition or task execution
        if (result.hasException()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject(
                    HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                    "Failed to retrieve data for game " + gameName + ": " + result.getException().getMessage(),
                    null,
                    null
            ));
        }

        // If data was retrieved, return success response
        if (list != null) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(
                    HttpStatus.OK.toString(),
                    "Get all server of " + gameName + " successfully!",
                    null,
                    list
            ));
        }

        // If lock could not be acquired, return error response
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject(
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                "Failed to retrieve data for game " + gameName,
                null,
                null
        ));
    }

    //    @Override
//    public ResponseEntity<ResponseObject> getAllGameServerOfUser(GameTokenForm gameTokenForm, String gameName) {
//        return gameTokenProvider.findByToken(DigestUtils.sha3_256Hex(gameTokenForm.getGameToken()))
//                .map(gameTokenProvider::verifyExpiration)
//                .map(GameToken::getUser)
//                .map(user -> {
//                    Game game = gameRepository.findByName(gameName).orElseThrow(
//                            () -> new NotFoundException("Game not found")
//                    );
//                    return ResponseEntity.status(HttpStatus.OK).body(
//                            new ResponseObject(HttpStatus.OK.toString(),
//                                    "Get all server of" + user.getUsername() + "successfully!",
//                                    null,
//                                    gameServerRepository.findAllByUsersAndGame(userRepository.getByUsername(user.getUsername()), game)
//                            )
//                    );
//                })
//                .orElseThrow(() -> new GameTokenException("Game token is not in database!"));
//    }
    @Override
    public ResponseEntity<ResponseObject> getAllGameServerOfUser(GameTokenForm gameTokenForm, String gameName) {
        String cacheKey = String.format("gameServers:%s:%s", gameTokenForm.getGameToken(), gameName);
        Object cacheValue = redisValueCache.getCacheValue(cacheKey);
        if (cacheValue != null) {
            List<GameServer> gameServers = (List<GameServer>) cacheValue;
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(HttpStatus.OK.toString(),
                            "Get all server successfully from cache!",
                            null,
                            gameServers
                    )
            );
        }

        LockExecutionResult<List<GameServer>> lockExecutionResult = distributedLocker.lock(
                cacheKey,
                30, // how long should the lock be acquired in seconds
                30, // lock timeout in seconds
                () -> {
                    return gameTokenProvider.findByToken(DigestUtils.sha3_256Hex(gameTokenForm.getGameToken()))
                            .map(gameTokenProvider::verifyExpiration)
                            .map(GameToken::getUser)
                            .map(user -> {
                                Game game = gameRepository.findByName(gameName).orElseThrow(
                                        () -> new NotFoundException("Game not found")
                                );
                                List<GameServer> gameServers = gameServerRepository.findAllByUsersAndGame(
                                        userRepository.getByUsername(user.getUsername()), game);
                                redisValueCache.cache(cacheKey, gameServers);
                                return gameServers;
                            })
                            .orElseThrow(() -> new GameTokenException("Game token is not in database!"));
                });

        if (lockExecutionResult.isLockAcquired()) {
            if (lockExecutionResult.hasException()) {
                throw new RuntimeException(lockExecutionResult.getException());
            } else {
                List<GameServer> gameServers = lockExecutionResult.getResultIfLockAcquired();
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(HttpStatus.OK.toString(),
                                "Get all server successfully!",
                                null,
                                gameServers
                        )
                );
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                            "Failed to acquire lock",
                            null,
                            null
                    )
            );
        }
    }


    public boolean checkDuplicate(String name, Game game) {
        boolean checkGameServer = gameServerRepository.existsByNameAndGame(name, game);
        if (checkGameServer) {
            throw new DuplicateException("Game Server: " + name + " already exists.");
        }
        return true;
    }
}
