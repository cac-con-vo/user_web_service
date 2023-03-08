package com.example.user_web_service.entity;

import com.example.user_web_service.helper.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "wallet")

public class Wallet implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long totalMoney;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATETIME_FORMAT)
    private Date update_at;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "wallet_category_id", nullable = false, referencedColumnName = "id")
    private WalletCategory walletCategory;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "character_id", nullable = false, referencedColumnName = "id")
    private Character character;

    @OneToMany(mappedBy = "wallet")
    private List<Transaction> transactions;
}
