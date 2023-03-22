package com.example.user_web_service.dto;

import com.example.user_web_service.helper.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
@Getter
public class GetGameServerDTO {
    private String name;
    private String createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATE_FORMAT)
    private Date createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATE_FORMAT)
    private Date updateDate;
    private String status;
}
