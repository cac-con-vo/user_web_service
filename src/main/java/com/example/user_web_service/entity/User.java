package com.example.user_web_service.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),

})
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(name = "firstName", length = 20)
    private String firstName;
    @Column(name = "lastName", length = 20)
    private String lastName;
    @JsonIgnore

    private String password;

    @Lob
    private String avatar;

    @Email
    private String email;

    private String phone;
    private String code;

    private UserStatus status;

    private Date createAt;

    private Date updateAt;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

}