package com.example.user_web_service.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoadGameResponse {
    private String status;
    private String message;
    private String jsonString;
    private String dataSharing;
}
