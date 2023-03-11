package com.example.user_web_service.form;

import com.example.user_web_service.entity.GameToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateGameServerForm {

    private String gameName;
    private String serverName;
    private List<String> gameTokenOfUsers;
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
    private String gameTokenOfRoomMaster;
}
