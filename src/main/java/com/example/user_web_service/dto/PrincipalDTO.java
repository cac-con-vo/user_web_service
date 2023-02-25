package com.example.user_web_service.dto;

import com.example.user_web_service.entity.Role;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.text.WordUtils;

@NoArgsConstructor
@AllArgsConstructor
@Setter
public class PrincipalDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String avatar;

    private String phone;

    private String status;
    private Role role;

    public Role getRole() {
        return role;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() {
        return avatar;
    }



    public String getStatus() {
        return status.toString() ;
    }


}
