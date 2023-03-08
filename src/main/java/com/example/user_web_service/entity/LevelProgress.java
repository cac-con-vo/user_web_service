package com.example.user_web_service.entity;

import com.example.user_web_service.helper.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "level_progress")

@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class LevelProgress implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Long expPoint;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATETIME_FORMAT)
    private Date levelUpDate;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "character_id", nullable = false, referencedColumnName = "id")
    private Character character;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "level_id", nullable = false, referencedColumnName = "id")
    private Level level;


}
