package com.example.user_web_service.payload.response;

import com.fasterxml.jackson.databind.JsonNode;
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
    private JsonNode jsonString;
    private JsonNode dataSharing;
}
