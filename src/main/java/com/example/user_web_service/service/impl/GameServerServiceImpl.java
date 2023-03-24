package com.example.user_web_service.service.impl;

import com.example.user_web_service.dto.*;
import com.example.user_web_service.entity.*;
import com.example.user_web_service.entity.Character;
import com.example.user_web_service.exception.DuplicateException;
import com.example.user_web_service.exception.NotFoundException;
import com.example.user_web_service.exception.ResourceNotFoundException;
import com.example.user_web_service.form.CreateGameServerForm;
import com.example.user_web_service.form.GameTokenForm;
import com.example.user_web_service.form.UpdateServerStatusForm;
import com.example.user_web_service.helper.Constant;
import com.example.user_web_service.payload.response.GetAllLevelOfGameResponse;
import com.example.user_web_service.payload.response.GetGameServerOfUserResponse;
import com.example.user_web_service.redis.RedisValueCache;
import com.example.user_web_service.redis.locker.DistributedLocker;
import com.example.user_web_service.redis.locker.LockExecutionResult;
import com.example.user_web_service.repository.*;
import com.example.user_web_service.security.jwt.GameTokenException;
import com.example.user_web_service.security.jwt.GameTokenProvider;
import com.example.user_web_service.service.GameServerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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

import java.util.*;


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
    private LevelProgressRepository levelProgressRepository;
    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private GameTokenProvider gameTokenProvider;
    @Autowired
    private RedisValueCache redisValueCache;
    @Autowired
    private DistributedLocker distributedLocker;
    @Autowired
    private CharacterDataRepository characterDataRepository;

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
    public ResponseEntity<ResponseObject> createGameServer(CreateGameServerForm createGameServerForm) {
        return gameTokenProvider.findByToken(DigestUtils.sha3_256Hex(createGameServerForm.getGameTokenOfRoomMaster()))
                .map(gameTokenProvider::verifyExpiration)
                .map(GameToken::getUser)
                .map(user -> {
// kiểm tra xem game có tồn tại không
                    Game game = gameRepository.findByName(createGameServerForm.getGameName()).orElseThrow(
                            () -> new NotFoundException("Game not found")
                    );
// kiểm tra server có trùng tên không
                    this.checkDuplicate(createGameServerForm.getServerName(), game);
                    GameServer gameServer;
                    // add user tạo server vào danh sách
                    List<User> users = new ArrayList<>();
                    users.add(user);
                    if (createGameServerForm.getGameTokenOfUsers() != null) {
                        for (String gameTokenUser : createGameServerForm.getGameTokenOfUsers()
                        ) {
                            GameToken gameToken = gameTokenProvider.findByToken(DigestUtils.sha3_256Hex(gameTokenUser)).orElseThrow(
                                    () -> new NotFoundException("Token of user not found!")
                            );
                            User user_join = userRepository.findByUsername(gameToken.getUser()
                                    .getUsername()).orElseThrow(
                                    () -> new UsernameNotFoundException(gameToken.getUser().getUsername() + " not found")
                            );
                            users.add(user_join);
                        }
                    }


                    gameServer = GameServer.builder()
                            .name(createGameServerForm.getServerName())
                            .status(GameServerStatus.ACTIVE)
                            .create_at(Constant.getCurrentDateTime())
                            .createBy(user)
                            .users(users)
                            .game(game)
                            .build();

                    gameServerRepository.save(gameServer);


                    return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject(
                            HttpStatus.CREATED.toString(),
                            "Create game server successfully!", null, gameServer));
                })
                .orElseThrow(() -> new GameTokenException("Game token is not in database!"));
    }
//    @Override
//    public ResponseEntity<ResponseObject> createGameServer(CreateGameServerForm createGameServerForm) {
//        return gameTokenProvider.findByToken(DigestUtils.sha3_256Hex(createGameServerForm.getGameTokenOfRoomMaster()))
//                .map(gameTokenProvider::verifyExpiration)
//                .map(GameToken::getUser)
//                .map(user -> {
//// kiểm tra xem game có tồn tại không
//                    Game game = gameRepository.findByName(createGameServerForm.getGameName()).orElseThrow(
//                            () -> new NotFoundException("Game not found")
//                    );
//// kiểm tra server có trùng tên không
//                    this.checkDuplicate(createGameServerForm.getServerName(), game);
//                    GameServer gameServer;
//                    // add user tạo server vào danh sách
//                    List<User> users = new ArrayList<>();
//                    users.add(user);
//                    for (String gameTokenUser : createGameServerForm.getGameTokenOfUsers()
//                         ) {
//                        GameToken gameToken = gameTokenProvider.findByToken(DigestUtils.sha3_256Hex(gameTokenUser)).orElseThrow(
//                                ()-> new NotFoundException("Token of user not found!")
//                        );
//                        User user_join = userRepository.findByUsername(gameToken.getUser()
//                                .getUsername()).orElseThrow(
//                                ()-> new UsernameNotFoundException(gameToken.getUser().getUsername()+ " not found")
//                        );
//                        users.add(user_join);
//                    }
//
//
//                    // tạo game server mới
//                    gameServer = GameServer.builder()
//                            .name(createGameServerForm.getServerName())
//                            .status(GameServerStatus.ACTIVE)
//                            .game(game)
//                            .users(users)
//                            .build();
//
//                    // cache game server vào Redis
//                    String gameServerKey = String.format("game_server_%s", gameServer.getId());
//                    distributedLocker.lock(gameServerKey, 10, 5, () -> {
//                        redisValueCache.cache(gameServerKey, gameServer);
//                        return null;
//                    });
//
//                    return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject(
//                            HttpStatus.CREATED.toString(),
//                            "Create game server successfully!", null, gameServer));
//                })
//                .orElseThrow(() -> new GameTokenException("Game token is not in database!"));
//    }


    @Override
    public ResponseEntity<ResponseObject> getAllGameServer(String gameName) {
        List<GameServer> list;
        List<GetGameServerDTO> gameServerDTOS;
        if (gameRepository.existsByName(gameName)) {
            list = gameServerRepository.findAllByGame(gameRepository.findByName(gameName).orElseThrow(
                    () -> new NotFoundException("Game not found")
            ));
            gameServerDTOS = new ArrayList<>();
            for (GameServer gameServer : list
            ) {
                GetGameServerDTO getGameServerDTO = GetGameServerDTO.builder()
                        .id(gameServer.getId())
                        .name(gameServer.getName())
                        .createdBy(gameServer.getCreateBy().getUsername())
                        .createdDate(gameServer.getCreate_at())
                        .updateDate(gameServer.getUpdate_at())
                        .status(gameServer.getStatus().name())
                        .build();
                gameServerDTOS.add(getGameServerDTO);
            }
        } else {
            throw new NotFoundException("Game " + gameName + " not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(
                HttpStatus.OK.toString(),
                "Get all server of " + gameName + "successfully!",
                null,
                gameServerDTOS
        ));
    }
//    @Override
//    public ResponseEntity<ResponseObject> getAllGameServer(String gameName) {
//        String cacheKey = "allGameServer:" + gameName;
//        List<GameServer> list = null;
//
//        // Attempt to get data from cache
//        Object cachedData = redisValueCache.getCacheValue(cacheKey);
//        if (cachedData != null) {
//            list = (List<GameServer>) cachedData;
//            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(
//                    HttpStatus.OK.toString(),
//                    "Get all server of " + gameName + " successfully from cache!",
//                    null,
//                    list
//            ));
//        }
//
//        // If not in cache, attempt to acquire a lock to retrieve data from database
//        LockExecutionResult<List<GameServer>> result = distributedLocker.lock(
//                cacheKey,
//                10,  // lock timeout in seconds
//                30,  // maximum time to acquire lock in seconds
//                () -> {
//                    if (gameRepository.existsByName(gameName)) {
//                        List<GameServer> servers = gameServerRepository.findAllByGame(gameRepository.findByName(gameName).orElseThrow(
//                                () -> new NotFoundException("Game not found")
//                        ));
//                        redisValueCache.cache(cacheKey, servers);
//                        return servers;
//                    } else {
//                        throw new NotFoundException("Game " + gameName + " not found.");
//                    }
//                }
//        );
//
//        if (result.isLockAcquired()) {
//            list = result.getResultIfLockAcquired();
//        }
//
//        // Check if there was an exception during lock acquisition or task execution
//        if (result.hasException()) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject(
//                    HttpStatus.INTERNAL_SERVER_ERROR.toString(),
//                    "Failed to retrieve data for game " + gameName + ": " + result.getException().getMessage(),
//                    null,
//                    null
//            ));
//        }
//
//        // If data was retrieved, return success response
//        if (list != null) {
//            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(
//                    HttpStatus.OK.toString(),
//                    "Get all server of " + gameName + " successfully!",
//                    null,
//                    list
//            ));
//        }
//
//        // If lock could not be acquired, return error response
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject(
//                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
//                "Failed to retrieve data for game " + gameName,
//                null,
//                null
//        ));
//    }

    @Override
    public ResponseEntity<GetGameServerOfUserResponse> getAllGameServerOfUser(GameTokenForm gameTokenForm, String gameName) {
        return gameTokenProvider.findByToken(DigestUtils.sha3_256Hex(gameTokenForm.getGameToken()))
                .map(gameTokenProvider::verifyExpiration)
                .map(GameToken::getUser)
                .map(user -> {
                    Game game = gameRepository.findByName(gameName).orElseThrow(
                            () -> new NotFoundException("Game not found")
                    );
                    List<GameServer> gameServers = gameServerRepository.findAllByUsersAndGameAndStatus(userRepository.getByUsername(user.getUsername()), game, GameServerStatus.ACTIVE);
                    if(gameServers.size() == 0){
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                                new GetGameServerOfUserResponse(HttpStatus.NOT_FOUND.toString(),
                                "Not found game server", null));
                    }
                    Set<GameServer> uniqueServers = new HashSet<>(gameServers);
                    List<GameServer> uniqueServerList = new ArrayList<>(uniqueServers);
                    ModelMapper modelMapper = new ModelMapper();
                    List<GameServerInfoDTO> gameServerInfoDTOS = new ArrayList<>();
                    for (GameServer gameServer : uniqueServerList
                    ) {
                        GameServerInfoDTO gameServerDTOS = new GameServerInfoDTO();
                        Character character = characterRepository.findByUserAndGameServer(user, gameServer);
                        if (character != null) {
                            int timePlay = 0;
                            //lay play time
                            CharacterData characterData = characterDataRepository.findByCharacter(character);
                            if (characterData != null) {
                                ObjectMapper objectMapper = new ObjectMapper();
                                GameDataDTO gameData;
                                try {
                                    gameData = objectMapper.readValue(characterData.getJsonString(), GameDataDTO.class);
                                } catch (JsonProcessingException e) {
                                    throw new RuntimeException(e);
                                }
                                timePlay = gameData.getPlayGameTimeSeconds();
                            }

                            LevelProgress levelProgress1 = levelProgressRepository.findFirstByCharacterOrderByLevelUpDateDesc(character).orElseThrow(
                                    () -> new NotFoundException("Level progress not found")
                            );

                            String subStr1 = levelProgress1.getLevel().getName().substring(6);
                            List<User> users = gameServer.getUsers();
                            List<User> users1 = new ArrayList<>();
                            for (User user1 : users
                            ) {
                                if (user1.getId() != user.getId()) {
                                    users1.add(user1);
                                }
                            }
                            List<UserInGameDTO> userInGameDTOS = new ArrayList<>();
                            for (User user1 : users1
                            ) {
                                UserInGameDTO userInGameDTO = UserInGameDTO.builder()
                                        .id(user1.getId())
                                        .username(user1.getUsername())
                                        .build();
                                userInGameDTOS.add(userInGameDTO);
                            }

                            gameServerDTOS.setServerName(gameServer.getName());
                            gameServerDTOS.setCharacterName(character.getName());
                            gameServerDTOS.setCurrentLevel(Integer.parseInt(subStr1));
                            gameServerDTOS.setUsers(userInGameDTOS);
                            gameServerDTOS.setPlayTime(timePlay);
                            gameServerInfoDTOS.add(gameServerDTOS);
                        }
                    }
                    return ResponseEntity.status(HttpStatus.OK).body(
                            new GetGameServerOfUserResponse(HttpStatus.OK.toString(),
                                    "Get all server of" + user.getUsername() + "successfully!", gameServerInfoDTOS
                            )
                    );
                })
                .orElseThrow(() -> new GameTokenException("Game token is not in database!"));
    }


//    @Override
//    public ResponseEntity<ResponseObject> getAllGameServerOfUser(GameTokenForm gameTokenForm, String gameName) {
//        String cacheKey = String.format("gameServers:%s:%s", gameTokenForm.getGameToken(), gameName);
//        Object cacheValue = redisValueCache.getCacheValue(cacheKey);
//        if (cacheValue != null) {
//            List<GameServer> gameServers = (List<GameServer>) cacheValue;
//            return ResponseEntity.status(HttpStatus.OK).body(
//                    new ResponseObject(HttpStatus.OK.toString(),
//                            "Get all server successfully from cache!",
//                            null,
//                            gameServers
//                    )
//            );
//        }
//
//        LockExecutionResult<List<GameServer>> lockExecutionResult = distributedLocker.lock(
//                cacheKey,
//                30, // how long should the lock be acquired in seconds
//                30, // lock timeout in seconds
//                () -> {
//                    return gameTokenProvider.findByToken(DigestUtils.sha3_256Hex(gameTokenForm.getGameToken()))
//                            .map(gameTokenProvider::verifyExpiration)
//                            .map(GameToken::getUser)
//                            .map(user -> {
//                                Game game = gameRepository.findByName(gameName).orElseThrow(
//                                        () -> new NotFoundException("Game not found")
//                                );
//                                List<GameServer> gameServers = gameServerRepository.findAllByUsersAndGame(
//                                        userRepository.getByUsername(user.getUsername()), game);
//                                redisValueCache.cache(cacheKey, gameServers);
//                                return gameServers;
//                            })
//                            .orElseThrow(() -> new GameTokenException("Game token is not in database!"));
//                });
//
//        if (lockExecutionResult.isLockAcquired()) {
//            if (lockExecutionResult.hasException()) {
//                throw new RuntimeException(lockExecutionResult.getException());
//            } else {
//                List<GameServer> gameServers = lockExecutionResult.getResultIfLockAcquired();
//                return ResponseEntity.status(HttpStatus.OK).body(
//                        new ResponseObject(HttpStatus.OK.toString(),
//                                "Get all server successfully!",
//                                null,
//                                gameServers
//                        )
//                );
//            }
//        } else {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
//                    new ResponseObject(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
//                            "Failed to acquire lock",
//                            null,
//                            null
//                    )
//            );
//        }
//    }

    @Override
    public ResponseEntity<ResponseObject> updateStatusGameServer(UpdateServerStatusForm updateServerStatusForm) {
        GameServer gameServer = gameServerRepository.findById(updateServerStatusForm.getId()).orElseThrow(
                () -> new NotFoundException("Server not found")
        );
        if (updateServerStatusForm.getStatusName().equalsIgnoreCase(GameServerStatus.ACTIVE.name())) {
            gameServer.setStatus(GameServerStatus.ACTIVE);
        } else if (updateServerStatusForm.getStatusName().equalsIgnoreCase(GameServerStatus.DELETED.name())) {
            gameServer.setStatus(GameServerStatus.DELETED);
        }
        gameServer.setUpdate_at(new Date());
        gameServerRepository.save(gameServer);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(HttpStatus.OK.toString(), "Status of server update successfully!", null, null)
        );
    }

    public boolean checkDuplicate(String name, Game game) {
        boolean checkGameServer = gameServerRepository.existsByNameAndGame(name, game);
        if (checkGameServer) {
            throw new DuplicateException("Game Server: " + name + " already exists.");
        }
        return true;
    }
}
