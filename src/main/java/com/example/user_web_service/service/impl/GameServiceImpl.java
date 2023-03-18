package com.example.user_web_service.service.impl;

import com.example.user_web_service.dto.*;
import com.example.user_web_service.entity.*;
import com.example.user_web_service.entity.Character;
import com.example.user_web_service.exception.NotFoundException;
import com.example.user_web_service.form.GameTokenForm;
import com.example.user_web_service.form.LoadGameForm;
import com.example.user_web_service.form.SaveGameForm;
import com.example.user_web_service.payload.response.LoadGameResponse;
import com.example.user_web_service.repository.*;
import com.example.user_web_service.security.jwt.GameTokenException;
import com.example.user_web_service.security.jwt.GameTokenProvider;
import com.example.user_web_service.service.GameService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameTokenProvider gameTokenProvider;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GameServerRepository gameServerRepository;
    @Autowired
    private CharacterRepository characterRepository;
    @Autowired
    private CharacterAttributeRepository characterAttributeRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private WalletCategoryRepository walletCategoryRepository;
    @Autowired
    private AttributeGroupRepository attributeGroupRepository;
    @Autowired
    private AssetRepository assetRepository;
    @Autowired
    private AssetTypeRepository assetTypeRepository;
    @Autowired
    private AssetAttributeRepository assetAttributeRepository;
    @Autowired
    private CharacterDataRepository characterDataRepository;
    @Autowired
    private GameServerDataRepository gameServerDataRepository;
    @Autowired
    private LevelProgressRepository levelProgressRepository;
    @Autowired
    private LevelRepository levelRepository;

    @Override
    public ResponseEntity<ResponseObject> saveGame(SaveGameForm saveGameForm) {
        return gameTokenProvider.findByToken(DigestUtils.sha3_256Hex(saveGameForm.getGameToken()))
                .map(gameTokenProvider::verifyExpiration)
                .map(GameToken::getUser)
                .map(user -> {
                    //Kiem tra game, game server, character có tồn tại k
                    Game game = gameRepository.findByName(saveGameForm.getGameName()).orElseThrow(
                            () -> new NotFoundException("Game not found")
                    );
                    GameServer gameServer = gameServerRepository.findByNameAndGame(saveGameForm.getServerName(), game).orElseThrow(
                            () -> new NotFoundException("Server not found")
                    );
                    Character character = characterRepository.findByUserAndGameServer(user, gameServer).orElseThrow(
                            () -> new NotFoundException("Character not found")
                    );
                    characterRepository.findByUserAndGameServerAndStatus(user, gameServer, CharacterStatus.ACTIVE).orElseThrow(
                            () -> new RuntimeException("Character is not valid")
                    );
                    //Luu gia tri
                    ObjectMapper objectMapper = new ObjectMapper();
                    GameDataDTO gameData;
                    try {
                        gameData = objectMapper.readValue(saveGameForm.getJsonString().toString(), GameDataDTO.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                    if (gameData == null || gameData.getCharacterS() == null|| gameData.getWeaponS().size() == 0 ||
                    gameData.getGoldS() == null || gameData.getPcharS() == null || gameData.getPnameS() == null
                    ){
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                                new ResponseObject(HttpStatus.BAD_REQUEST.toString(),
                                        "Data of character is not valid",
                                        null, null)
                        );
                    }
                    //update character
                    character.setBasicMaxHP(gameData.getCharacterS().getBasicMaxHP());
                    character.setCurrentHP(gameData.getCharacterS().getCurrentHP());
                    character.setBasicMaxMP(gameData.getCharacterS().getBasicMaxMP());
                    character.setCurrentMP(gameData.getCharacterS().getCurrentMP());
                    character.setBasicMaxStamina(gameData.getCharacterS().getBasicMaxStamina());
                    character.setCurrentStamina(gameData.getCharacterS().getCurrentStamina());
                    character.setBasicSpeed(gameData.getCharacterS().getBasicSpeed());
                    character.setBasicRecuperateHP(gameData.getCharacterS().getBasicRecuperateHP());
                    character.setBasicRecuperateMP(gameData.getCharacterS().getBasicRecuperateMP());
                    character.setBasicRecuperateStamina(gameData.getCharacterS().getBasicRecuperateStamina());
                    character.setPosition(new CharacterPosition(gameData.getX(), gameData.getY(), gameData.getZ()));
                    character.setFree_point(gameData.getCharacterS().getFreePoint());
                    character.setUpdate_at(new Date());
                    characterRepository.save(character);
                    //update character attribute
                    List<CharacterAttribute> characterAttributes = characterAttributeRepository.findAllByCharacter(character);
                    for (CharacterAttribute characterAttribute : characterAttributes
                         ) {
                        for (CharacterAttributeDTO c : gameData.getCharacterS().getAttribute()
                                ) {
                            if(characterAttribute.getId()==c.getId()){
                                characterAttribute.setValue(c.getPointValue());
                                characterAttributeRepository.save(characterAttribute);
                                break;
                            }
                        }
                    }
                    //update level
                    Level level = levelRepository.findByNameAndGame("Level " + gameData.getCharacterS().getCharacterLevel().getCurrentLevel(), game).orElseThrow(
                            ()-> new NotFoundException("Level not found")
                    );
                    LevelProgress levelProgress = levelProgressRepository.findByCharacterAndLevel(character, level);
                    if(levelProgress == null){
                        levelProgress = LevelProgress.builder()
                                .level(level)
                                .name(level.getName())
                                .expPoint(gameData.getCharacterS().getCharacterLevel().getLevelPoint())
                                .character(character)
                                .levelUpDate(new Date())
                                .build();
                    }else {
                        levelProgress.setExpPoint(gameData.getCharacterS().getCharacterLevel().getLevelPoint());
                        levelProgress.setLevelUpDate(new Date());
                    }
                    levelProgressRepository.save(levelProgress);

                    //Update wallet
                    WalletCategory walletCategory = walletCategoryRepository.findByName("InGame").orElseThrow(
                            ()-> new NotFoundException("WalletCategory not found")
                    );
                    Wallet wallet = walletRepository.findByCharacter(character);
                    if(wallet == null){
                        wallet = Wallet.builder()
                                .walletCategory(walletCategory)
                                .name("Gold")
                                .character(character)
                                .totalMoney(gameData.getGoldS())
                                .update_at(new Date())
                                .build();
                    }else{
                        wallet.setTotalMoney(gameData.getGoldS());
                        wallet.setUpdate_at(new Date());
                    }
                    walletRepository.save(wallet);
                    //update asset
                    AssetType assetType = assetTypeRepository.findByName("Weapon").orElseThrow(
                            ()-> new NotFoundException("Asset type not found")
                    );
                    List<Asset> assets = assetRepository.findAllByCharacterAndAndAssetType(character, assetType);
                    if(gameData.getWeaponS().size() > 0){
                        for (AssetDTO assetDTO : gameData.getWeaponS()
                        ) {
                            String id = assetDTO.getId().substring(2);
                            Asset asset = null;
                            if (assets.size() == 0) {
                                asset = Asset.builder()
                                        .name(assetDTO.getName())
                                        .id(Long.parseLong(id))
                                        .character(character)
                                        .assetType(assetType)
                                        .cost(assetDTO.getCost())
                                        .description(assetDTO.getDescription())
                                        .build();
                            } else {
                                asset.setCost(assetDTO.getCost());
                            }
                            assetRepository.save(asset);

                            //update asset attribute
                            List<AttributeGroup> attributeGroups =  attributeGroupRepository.findByGame(game).orElseThrow(
                                    ()-> new NotFoundException("AttributeGroup not found")
                            );
                            List<AssetAttribute> assetAttributes = assetAttributeRepository.findAllByAsset(asset);
                            if(assetAttributes.size() == 0){
                                if(assetDTO.getCharacterAttribute().size() > 0){
                                    for (AssetAttributeDTO assetAttributeDTO: assetDTO.getCharacterAttribute()
                                    ) {
                                        for (AttributeGroup attributeGroup: attributeGroups
                                        ) {
                                            if (attributeGroup.getId() == assetAttributeDTO.getId()){
                                                AssetAttribute assetAttribute = AssetAttribute.builder()
                                                        .asset(asset)
                                                        .attributeGroup(attributeGroup)
                                                        .value(assetAttributeDTO.getPointValue())
                                                        .build();
                                                assetAttributes.add(assetAttribute);
                                                assetAttributeRepository.save(assetAttribute);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                //update json data of character and sharing data of server
                CharacterData characterData = characterDataRepository.findByCharacter(character);
                    if (characterData == null){
                        characterData = CharacterData.builder()
                                .character(character)
                                .jsonString(saveGameForm.getJsonString().toString())
                                .build();
                    }else{
                        characterData.setJsonString(saveGameForm.getJsonString().toString());
                    }
                    characterDataRepository.save(characterData);
                GameServerData gameServerData = gameServerDataRepository.findByGameServer(gameServer);
                if(gameServerData == null){
                    gameServerData = GameServerData.builder()
                            .jsonDataSharing(saveGameForm.getDataSharing().toString())
                            .gameServer(gameServer)
                            .build();
                }else{
                    gameServerData.setJsonDataSharing(saveGameForm.getDataSharing().toString());
                }
                gameServerDataRepository.save(gameServerData);
                return ResponseEntity.status(HttpStatus.OK).body(
                  new ResponseObject(HttpStatus.OK.toString(),"Save game successfully!", null, null)
                );
                })
                .orElseThrow(() -> new GameTokenException("Game token is not in database!"));
    }

    @Override
    public ResponseEntity<LoadGameResponse> loadGame(LoadGameForm loadGameForm) {
        return gameTokenProvider.findByToken(DigestUtils.sha3_256Hex(loadGameForm.getGameToken()))
                .map(gameTokenProvider::verifyExpiration)
                .map(GameToken::getUser)
                .map(user -> {
                    Game game = gameRepository.findByName(loadGameForm.getGameName()).orElseThrow(
                            ()-> new NotFoundException("Game not found")
                    );
                    GameServer gameServer = gameServerRepository.findByNameAndGame(loadGameForm.getServerName(), game).orElseThrow(
                            ()-> new NotFoundException("Game server not found")
                    );
                    Character character = characterRepository.findByUserAndGameServer(user, gameServer).orElseThrow(
                            ()-> new NotFoundException("Character not found")
                    );
                    characterRepository.findByUserAndGameServerAndStatus(user,gameServer, CharacterStatus.ACTIVE).orElseThrow(
                            ()-> new RuntimeException("Character is not valid")
                    );

                    CharacterData characterData = characterDataRepository.findByCharacter(character);
                    if(characterData == null){
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                                new LoadGameResponse(HttpStatus.NOT_FOUND.toString(), "Data of character not found", null, null));
                    }
                    GameServerData gameServerData = gameServerDataRepository.findByGameServer(gameServer);
                    if(gameServerData == null){
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                                new LoadGameResponse(HttpStatus.NOT_FOUND.toString(), "Data of game server not found", null, null));
                    }
                    return ResponseEntity.status(HttpStatus.OK).body(
                            new LoadGameResponse(HttpStatus.OK.toString(),
                                    "Data of game server not found",
                                     characterData.getJsonString(), gameServerData.getJsonDataSharing()));
                })
                .orElseThrow(() -> new GameTokenException("Game token is not in database!"));
    }
}
