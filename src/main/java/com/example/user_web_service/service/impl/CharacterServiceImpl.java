package com.example.user_web_service.service.impl;

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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
    private AttributeGroupRepository attributeGroupRepository;
    @Override
    public ResponseEntity<ResponseObject> creatCharacter(String name, String serverName) {
        Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.getByUsername(principal.getUsername());
        GameServer gameServer = gameServerRepository.findByName(serverName).orElseThrow(
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

                character.setCreate_at(new Date());
                character.setStatus(CharacterStatus.ACTIVE);
                character.setName(name);
                characterRepository.save(character);
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
    public ResponseEntity<ResponseObject> getCharacter(GameTokenForm gameTokenForm, String serverName) {
        return  gameTokenProvider.findByToken(DigestUtils.sha3_256Hex(gameTokenForm.getGameToken()))
                .map(gameTokenProvider::verifyExpiration)
                .map(GameToken::getUser)
                .map(user -> {
                    GameServer server = gameServerRepository.findByName(serverName).orElseThrow(
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
//                    List<AttributeGroup> attributes = attributeGroupRepository.findAllByCharacter(character).orElseThrow(
//                            ()-> new NotFoundException("Attribute of character not found.")
//                    );
                    ModelMapper modelMapper = new ModelMapper();
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObject(HttpStatus.ACCEPTED.toString(), "Get list game server  success!", null, character));
                })
                .orElseThrow(() -> new RefreshTokenException("Game token is not in database!"));
    }

}
