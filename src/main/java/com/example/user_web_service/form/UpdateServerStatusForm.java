package com.example.user_web_service.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdateServerStatusForm {
    @Schema(description = "server id", required = true)
    @NotBlank(message = "id not blank")
    private Long id;

    @Schema(description = "status name to change",example = "ACTIVE, DELETED", required = true)
    @NotBlank(message = "status name not blank")
    private String statusName;
}
