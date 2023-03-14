package com.example.user_web_service.service.impl;

import com.example.user_web_service.dto.*;
import com.example.user_web_service.entity.*;
import com.example.user_web_service.entity.Character;
import com.example.user_web_service.exception.NotFoundException;
import com.example.user_web_service.exception.ResourceNotFoundException;
import com.example.user_web_service.form.CreateCharacterForm;
import com.example.user_web_service.form.GameTokenForm;
import com.example.user_web_service.repository.*;
import com.example.user_web_service.security.jwt.GameTokenException;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
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
    @Autowired
    private AttributeGroupRepository attributeGroupRepository;
    @Autowired
    private CharacterAttributeRepository characterAttributeRepository;

    @Override
    public ResponseEntity<ResponseObject> creatCharacter(CreateCharacterForm createCharacterForm) {
        return  gameTokenProvider.findByToken(DigestUtils.sha3_256Hex(createCharacterForm.getGameToken()))
                .map(gameTokenProvider::verifyExpiration)
                .map(GameToken::getUser)
                .map(user -> {
                    Game game = gameRepository.findByName(createCharacterForm.getGameName()).orElseThrow(
                            ()-> new NotFoundException("Name of game not found")
                    );
                    GameServer gameServer = gameServerRepository.findByNameAndGame(createCharacterForm.getServerName(), game).orElseThrow(
                            ()-> new NotFoundException("Game server not found.")
                    );


                    List<CharacterStatus> activeAndDeletedStatuses = Arrays.asList(CharacterStatus.ACTIVE, CharacterStatus.DELETED);
                    List<Character> characters = characterRepository.findByUserAndGameServerAndStatusIn(user, gameServer, activeAndDeletedStatuses);

                    //kiem tra xem voi user va gameServer thi co character cos status Active + Deleted nao ton tai k
                    if (characters.size() == 0) {
                        if (!characterRepository.existsByName(createCharacterForm.getCharacterName())) {
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
                            character.setName(createCharacterForm.getCharacterName());
                            character.setBasicMaxHP(200.0f);
                            character.setCurrentHP(1.0f);
                            character.setBasicMaxMP(100.0f);
                            character.setCurrentMP(1.0f);
                            character.setBasicMaxStamina(100.0f);
                            character.setCurrentStamina(1.0f);
                            character.setBasicSpeed(10.0f);
                            character.setBasicRecuperateHP(2.0f);
                            character.setBasicRecuperateMP(1.0f);
                            character.setBasicRecuperateStamina(2.0f);
                            character.setFree_point(3);
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
                            //tao cac attribute_character
                            List<AttributeGroup> attributeGroups = attributeGroupRepository.findByGame(game).orElseThrow(
                                    ()-> new NotFoundException("Not found attribute group.")
                            );
                            for (AttributeGroup attributeGroup: attributeGroups
                            ) {
                                CharacterAttribute characterAttribute = new CharacterAttribute();
                                if(attributeGroup.getName().equalsIgnoreCase(AttributeGroupName.VIGOR.name())){
                                    characterAttribute = CharacterAttribute.builder()
                                            .character(character)
                                            .attributeGroup(attributeGroup)
                                            .value(0L)
                                            .build();
                                }else if(attributeGroup.getName().equalsIgnoreCase(AttributeGroupName.MIND.name())){
                                    characterAttribute = CharacterAttribute.builder()
                                            .character(character)
                                            .attributeGroup(attributeGroup)
                                            .value(0L)
                                            .build();
                                }else if(attributeGroup.getName().equalsIgnoreCase(AttributeGroupName.ENDURANCE.name())){
                                    characterAttribute = CharacterAttribute.builder()
                                            .character(character)
                                            .attributeGroup(attributeGroup)
                                            .value(0L)
                                            .build();
                                }else if(attributeGroup.getName().equalsIgnoreCase(AttributeGroupName.STRENGTH.name())){
                                    characterAttribute = CharacterAttribute.builder()
                                            .character(character)
                                            .attributeGroup(attributeGroup)
                                            .value(0L)
                                            .build();
                                }else if(attributeGroup.getName().equalsIgnoreCase(AttributeGroupName.INTELLIGENCE.name())){
                                    characterAttribute = CharacterAttribute.builder()
                                            .character(character)
                                            .attributeGroup(attributeGroup)
                                            .value(0L)
                                            .build();
                                }else if(attributeGroup.getName().equalsIgnoreCase(AttributeGroupName.FLEXIBLE.name())){
                                    characterAttribute = CharacterAttribute.builder()
                                            .character(character)
                                            .attributeGroup(attributeGroup)
                                            .value(0L)
                                            .build();
                                }else if(attributeGroup.getName().equalsIgnoreCase(AttributeGroupName.DEXTERITY.name())){
                                    characterAttribute = CharacterAttribute.builder()
                                            .character(character)
                                            .attributeGroup(attributeGroup)
                                            .value(0L)
                                            .build();
                                }
                                characterAttributeRepository.save(characterAttribute);
                            }

                            return ResponseEntity.status(HttpStatus.OK).body(
                                    new ResponseObject(HttpStatus.OK.toString(), "Create successfully", null, null)
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
                })
                .orElseThrow(() -> new GameTokenException("Game token is not in database!"));
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
                    //Lay level hien tai cua nhan vat
                    LevelProgress levelProgress1 = levelProgressRepository.findFirstByCharacterOrderByLevelUpDateDesc(character).orElseThrow(
                            () -> new NotFoundException("Level progress not found")
                    );

                    String subStr1 = levelProgress1.getLevel().getName().substring(6); // từ chỉ mục thứ 6 đến hết chuỗi
                    LevelProgressDTO levelProgressDTO = LevelProgressDTO.builder()
                            .currentLevel(Integer.parseInt(subStr1))
                            .levelPoint(levelProgress1.getExpPoint())
                            .build();

                    character1.setCurrentLevel(levelProgressDTO);
                    //Lay attribute cua nhan vat
                    List<CharacterAttribute> characterAttributes = character.getCharacterAttributes();
                    List<CharacterAttributeDTO> characterAttributeDTOs = new ArrayList<>();
                    for (CharacterAttribute characterAttribute : characterAttributes
                    ){
                        CharacterAttributeDTO characterAttributeDTO = new CharacterAttributeDTO();
                        if(characterAttribute.getAttributeGroup().getName().equalsIgnoreCase(AttributeGroupName.VIGOR.name())){
                            characterAttributeDTO = CharacterAttributeDTO.builder()
                                    .id(AttributeGroupName.VIGOR.ordinal())
                                    .pointValue(characterAttribute.getValue())
                                    .build();
                        } else if(characterAttribute.getAttributeGroup().getName().equalsIgnoreCase(AttributeGroupName.MIND.name())){
                            characterAttributeDTO = CharacterAttributeDTO.builder()
                                    .id(AttributeGroupName.MIND.ordinal())
                                    .pointValue(characterAttribute.getValue())
                                    .build();
                        }else if(characterAttribute.getAttributeGroup().getName().equalsIgnoreCase(AttributeGroupName.ENDURANCE.name())){
                            characterAttributeDTO = CharacterAttributeDTO.builder()
                                    .id(AttributeGroupName.ENDURANCE.ordinal())
                                    .pointValue(characterAttribute.getValue())
                                    .build();
                        }else if(characterAttribute.getAttributeGroup().getName().equalsIgnoreCase(AttributeGroupName.STRENGTH.name())){
                            characterAttributeDTO = CharacterAttributeDTO.builder()
                                    .id(AttributeGroupName.STRENGTH.ordinal())
                                    .pointValue(characterAttribute.getValue())
                                    .build();
                        }else if(characterAttribute.getAttributeGroup().getName().equalsIgnoreCase(AttributeGroupName.INTELLIGENCE.name())){
                            characterAttributeDTO = CharacterAttributeDTO.builder()
                                    .id(AttributeGroupName.INTELLIGENCE.ordinal())
                                    .pointValue(characterAttribute.getValue())
                                    .build();
                        }else if(characterAttribute.getAttributeGroup().getName().equalsIgnoreCase(AttributeGroupName.FLEXIBLE.name())){
                            characterAttributeDTO = CharacterAttributeDTO.builder()
                                    .id(AttributeGroupName.FLEXIBLE.ordinal())
                                    .pointValue(characterAttribute.getValue())
                                    .build();
                        }else if(characterAttribute.getAttributeGroup().getName().equalsIgnoreCase(AttributeGroupName.DEXTERITY.name())){
                            characterAttributeDTO = CharacterAttributeDTO.builder()
                                    .id(AttributeGroupName.DEXTERITY.ordinal())
                                    .pointValue(characterAttribute.getValue())
                                    .build();
                        }
                        characterAttributeDTOs.add(characterAttributeDTO);
                    }
                    character1.setCharacterAttributes(characterAttributeDTOs);
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObject(HttpStatus.ACCEPTED.toString(), "Get character successfully!", null, character1));
                })
                .orElseThrow(() -> new RefreshTokenException("Game token is not in database!"));
    }

    @Override
    public ResponseEntity<ResponseObject> getAttributeEffect(GameTokenForm gameTokenForm, String gameName) {
        return  gameTokenProvider.findByToken(DigestUtils.sha3_256Hex(gameTokenForm.getGameToken()))
                .map(gameTokenProvider::verifyExpiration)
                .map(GameToken::getUser)
                .map(user -> {
                    Game game = gameRepository.findByName(gameName).orElseThrow(
                            ()-> new NotFoundException("Game not found")
                    );
                    List<AttributeGroup> attributeGroups = attributeGroupRepository.findByGame(game).orElseThrow(
                            ()-> new NotFoundException("Attribute group not found")
                    );


                    List<AttributeEffectDTO> attributeEffectDTOS = new ArrayList<>();
                    for (AttributeGroup attributeGroup: attributeGroups
                         ) {
                        AttributeEffectDTO attributeEffectDTO = new AttributeEffectDTO();
                        if(attributeGroup.getName().equalsIgnoreCase(AttributeGroupName.VIGOR.name())){
                            attributeEffectDTO = AttributeEffectDTO.builder()
                                    .id(AttributeGroupName.VIGOR.ordinal())
                                    .effect(attributeGroup.getEffect())
                                    .build();
                        }else if(attributeGroup.getName().equalsIgnoreCase(AttributeGroupName.MIND.name())){
                            attributeEffectDTO = AttributeEffectDTO.builder()
                                    .id(AttributeGroupName.MIND.ordinal())
                                    .effect(attributeGroup.getEffect())
                                    .build();
                        }else if(attributeGroup.getName().equalsIgnoreCase(AttributeGroupName.ENDURANCE.name())){
                            attributeEffectDTO = AttributeEffectDTO.builder()
                                    .id(AttributeGroupName.ENDURANCE.ordinal())
                                    .effect(attributeGroup.getEffect())
                                    .build();
                        }else if(attributeGroup.getName().equalsIgnoreCase(AttributeGroupName.STRENGTH.name())){
                            attributeEffectDTO = AttributeEffectDTO.builder()
                                    .id(AttributeGroupName.STRENGTH.ordinal())
                                    .effect(attributeGroup.getEffect())
                                    .build();
                        }else if(attributeGroup.getName().equalsIgnoreCase(AttributeGroupName.INTELLIGENCE.name())){
                            attributeEffectDTO = AttributeEffectDTO.builder()
                                    .id(AttributeGroupName.INTELLIGENCE.ordinal())
                                    .effect(attributeGroup.getEffect())
                                    .build();
                        }else if(attributeGroup.getName().equalsIgnoreCase(AttributeGroupName.FLEXIBLE.name())){
                            attributeEffectDTO = AttributeEffectDTO.builder()
                                    .id(AttributeGroupName.FLEXIBLE.ordinal())
                                    .effect(attributeGroup.getEffect())
                                    .build();
                        }else if(attributeGroup.getName().equalsIgnoreCase(AttributeGroupName.DEXTERITY.name())){
                            attributeEffectDTO = AttributeEffectDTO.builder()
                                    .id(AttributeGroupName.DEXTERITY.ordinal())
                                    .effect(attributeGroup.getEffect())
                                    .build();
                        }
                        attributeEffectDTOS.add(attributeEffectDTO);
                    }
                    if(attributeEffectDTOS != null){
                        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(
                                HttpStatus.OK.toString(),
                                "Get effect of attribute successfully", null, attributeEffectDTOS
                        ));
                    }else{
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(
                                HttpStatus.BAD_REQUEST.toString(),
                                "Dont have any effect of attribute", null, null
                        ));
                    }
                })
                .orElseThrow(() -> new RefreshTokenException("Game token is not in database!"));
    }

    @Override
    public ResponseEntity<ResponseObject> getAllLevelOfGame(GameTokenForm gameTokenForm, String gameName) {
        return  gameTokenProvider.findByToken(DigestUtils.sha3_256Hex(gameTokenForm.getGameToken()))
                .map(gameTokenProvider::verifyExpiration)
                .map(GameToken::getUser)
                .map(user -> {
                    Game game = gameRepository.findByName(gameName).orElseThrow(
                            ()-> new NotFoundException("Game not found")
                    );
                    List<Level> levels = levelRepository.findAllByGame(game).orElseThrow(
                            ()-> new NotFoundException("Not found any level of game")
                    );
                    List<LevelDTO> levelDTOS = new ArrayList<>();
                    for (Level level:levels
                         ) {
                        String subStr = level.getName().substring(6);
                        LevelDTO levelDTO = LevelDTO.builder()
                                .levelValue(Integer.parseInt(subStr))
                                .levelRequire(level.getLevel_up_point())
                                .build();
                        levelDTOS.add(levelDTO);
                    }
                   return ResponseEntity.status(HttpStatus.OK).body(
                           new ResponseObject(HttpStatus.OK.toString(),
                                   "Get list level of game successfully",null, levelDTOS)
                   );
                })
                .orElseThrow(() -> new RefreshTokenException("Game token is not in database!"));
    }

}
