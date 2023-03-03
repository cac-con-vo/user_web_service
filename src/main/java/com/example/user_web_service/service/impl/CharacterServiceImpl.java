package com.example.user_web_service.service.impl;

import com.example.user_web_service.dto.CharacterDTO;
import com.example.user_web_service.dto.ResponseObject;
import com.example.user_web_service.entity.*;
import com.example.user_web_service.entity.Character;
import com.example.user_web_service.exception.NotFoundException;
import com.example.user_web_service.exception.ResourceNotFoundException;
import com.example.user_web_service.form.GameTokenForm;
import com.example.user_web_service.repository.*;
import com.example.user_web_service.security.jwt.GameTokenProvider;
import com.example.user_web_service.security.jwt.RefreshTokenException;
import com.example.user_web_service.security.userprincipal.Principal;
import com.example.user_web_service.service.CharacterService;
import org.apache.commons.codec.digest.DigestUtils;
import org.aspectj.asm.IModelFilter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class CharacterServiceImpl implements CharacterService {
    @Autowired
    private CharacterRepository characterRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GameServerRepository gameServerRepository;
    @Autowired
    private GameTokenProvider gameTokenProvider;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private LevelRepository levelRepository;
    @Autowired
    private LevelProgressRepository levelProgressRepository;

    @Override
    public ResponseEntity<ResponseObject> creatCharacter(String name, String gameName, String serverName) {
        Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.getByUsername(principal.getUsername());
        Game game = gameRepository.findByName(gameName).orElseThrow(
                ()-> new NotFoundException("Name of game not found")
        );
        GameServer gameServer = gameServerRepository.findByNameAndGame(serverName, game).orElseThrow(
                ()-> new NotFoundException("Game server not found.")
        );


        List<CharacterStatus> activeAndDeletedStatuses = Arrays.asList(CharacterStatus.ACTIVE, CharacterStatus.DELETED);
        List<Character> characters = characterRepository.findByUserAndGameServerAndStatusIn(user, gameServer, activeAndDeletedStatuses);

        //kiem tra xem voi user va gameServer thi co character cos status Active + Deleted nao ton tai k
        if (characters.size() == 0) {
            if (!characterRepository.existsByName(name)) {
                if(!characterRepository.existsByGameServer(gameServer)){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                            new ResponseObject(HttpStatus.NOT_FOUND.toString(), "Not found character.", null, null)
                    );
                }
                Character character = characterRepository.findByUserAndGameServerAndStatus(user, gameServer, CharacterStatus.INACTIVE).orElseThrow(
                        ()-> new ResourceNotFoundException("Character" , null , " not found")
                );
                Date date = new Date();
                //tao nhan vat
                character.setCreate_at(date);
                character.setStatus(CharacterStatus.ACTIVE);
                character.setName(name);
                characterRepository.save(character);
                //tao nhan vat thi nhan vat bat dau tu lv 0
                Level level = levelRepository.findByNameAndGame("Level 0", game).orElseThrow(
                        ()-> new NotFoundException("Level not found")
                );
                LevelProgress levelProgress = LevelProgress.builder()
                        .level(level)
                        .name(level.getName())
                        .character(character)
                        .expPoint(0L)
                        .levelUpDate(date)
                        .build();
                levelProgressRepository.save(levelProgress);


                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(HttpStatus.OK.toString(), "Create successfully", null, character)
                );
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                        new ResponseObject(HttpStatus.CONFLICT.toString(), "Character name is duplicate", null, null)
                );
            }

        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    new ResponseObject(HttpStatus.CONFLICT.toString(), "Character has already.", null, null)
            );
        }
    }


    @Override
    public ResponseEntity<ResponseObject> getCharacter(GameTokenForm gameTokenForm, String gameName,String serverName) {
        return  gameTokenProvider.findByToken(DigestUtils.sha3_256Hex(gameTokenForm.getGameToken()))
                .map(gameTokenProvider::verifyExpiration)
                .map(GameToken::getUser)
                .map(user -> {
                    Game game = gameRepository.findByName(gameName).orElseThrow(
                            ()-> new NotFoundException("Name of game not found")
                    );
                    GameServer server = gameServerRepository.findByNameAndGame(serverName, game).orElseThrow(
                            ()-> new NotFoundException("Game server not found.")
                    );
                    if(characterRepository.findByUserAndGameServerAndStatus(user, server,CharacterStatus.INACTIVE) == null) {
                      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                              new ResponseObject(HttpStatus.BAD_REQUEST.toString(),
                                      "Character does not active. Please create an charater.", null,null)
                      );
                    }else if(characterRepository.findByUserAndGameServerAndStatus(user, server,CharacterStatus.DELETED) == null) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                                new ResponseObject(HttpStatus.FORBIDDEN.toString(),
                                        "Character has been deleted can't enter the game.", null,null)
                        );
                    }
                    Character character = characterRepository.findByUserAndGameServer(user, server).orElseThrow(
                            ()-> new NotFoundException("Character not found.")
                    );
                    ModelMapper modelMapper = new ModelMapper();
                    CharacterDTO character1 = modelMapper.map(character, (Type) CharacterDTO.class);
                    LevelProgress levelProgress1 = levelProgressRepository.findFirstByCharacterOrderByLevelUpDateDesc(character).orElseThrow(
                            () -> new NotFoundException("Level progress not found")
                    );
                    character1.setCurrentLevel(levelProgress1);
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObject(HttpStatus.ACCEPTED.toString(), "Get character successfully!", null, character1));
                })
                .orElseThrow(() -> new RefreshTokenException("Game token is not in database!"));
    }

}
