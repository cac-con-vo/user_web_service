package com.example.user_web_service.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdateServerStatusForm {
    @Schema(description = "game name", required = true)
    @NotBlank(message = "game name not blank")
    private String gameName;

    @Schema(description = "server name", required = true)
    @NotBlank(message = "server name not blank")
    private String serverName;

    @Schema(description = "status name to change",example = "ACTIVE, DELETED", required = true)
    @NotBlank(message = "status name not blank")
    private String statusName;
}
