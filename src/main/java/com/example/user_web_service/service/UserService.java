package com.example.user_web_service.service;

import com.example.user_web_service.controller.BaseController;
import com.example.user_web_service.entity.Role;
import com.example.user_web_service.entity.User;
import com.example.user_web_service.entity.UserStatus;
import com.example.user_web_service.exception.AuthenticateException;
import com.example.user_web_service.exception.DuplicateException;
import com.example.user_web_service.exception.NotFoundException;
import com.example.user_web_service.exception.UserNotFoundException;
import com.example.user_web_service.form.*;
import com.example.user_web_service.helper.Constant;
import com.example.user_web_service.helper.EmailService;
import com.example.user_web_service.repository.RoleRepository;
import com.example.user_web_service.repository.UserRepository;
import com.example.user_web_service.security.MyAuthentication;
import com.example.user_web_service.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService extends BaseController {

    @Autowired
    private RedisTemplate redisTemplate;
    private static final String KEY="USER";
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    SecurityUtils securityUtils;

    @Autowired
    EmailService emailService;

    private boolean verify(String raw, String hashed) {
        return passwordEncoder.matches(raw, hashed);
    }

    private String generateHash(String raw) {
        return passwordEncoder.encode(raw);
    }

    public ResponseEntity<?> authenticateUser(LoginForm loginForm) {
        User user = this.getUserByUserName(loginForm.getUsername());

        boolean valid = verify(loginForm.getPassword(), user.getPassword());

        if (valid) {
            String token = securityUtils.GenerateJwt(user);

            MyAuthentication myAuthentication = new MyAuthentication(token);

            myAuthentication.setAuthenticated(true);

            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(myAuthentication);

            Map<String, String> response = new HashMap<>();
            response.put("Token", token);
            response.put("userName", user.getUsername());
            response.put("Role", user.getRole().getName());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new AuthenticateException("password is not match for " + loginForm.getUsername());
        }
    }

    public Boolean checkDuplicate(String phone, String email) {
        List<User> checkUsers = userRepository.findAllByEmailOrPhone(email, phone);

        for (User user : checkUsers) {
            if (user.getEmail().equalsIgnoreCase(email) && user.getId() != getCurrentUserId()) {
                throw new DuplicateException("Email " + email + " has been taken");
            }
            if (user.getPhone().equalsIgnoreCase(phone) && user.getId() != getCurrentUserId()) {
                throw new DuplicateException("Phone " + phone + " has been taken");
            }
        }

        return true;
    }

    public ResponseEntity<?> createUser(@Valid SignUpForm signUpForm) throws ParseException {
        this.checkDuplicate(signUpForm.getPhone(), signUpForm.getEmail());

        User regUser = new User();

        Role roleUser = roleRepository.findFirstByName("USER");

        regUser.setRole(roleUser);

        regUser.setUsername(signUpForm.getUsername());
        regUser.setPassword(this.generateHash(signUpForm.getPassword()));
        regUser.setFirstName(signUpForm.getFirstName());
        regUser.setLastName(signUpForm.getLastName());
        regUser.setEmail(signUpForm.getEmail());
        regUser.setPhone(signUpForm.getPhone());
        regUser.setCreateAt(Constant.getCurrentDateTime());
        regUser.setUpdateAt(null);
        regUser.setStatus(UserStatus.ACTIVE);

        userRepository.save(regUser);

        return new ResponseEntity<>(new ResponseForm<>("Account Created", true), HttpStatus.OK);
    }

    public ResponseEntity<?> updateUser(UpdateUserForm updateUserForm) throws ParseException {
        this.checkDuplicate(updateUserForm.getPhone(), updateUserForm.getEmail());

        User updateUser = this.getUserById(getCurrentUserId());
        updateUser.setPassword(this.generateHash(updateUserForm.getPassword()));
        updateUser.setFirstName(updateUserForm.getFirstname());
        updateUser.setLastName(updateUserForm.getLastname());
        updateUser.setEmail(updateUserForm.getEmail());
        updateUser.setPhone(updateUserForm.getPhone());
        updateUser.setUpdateAt(Constant.getCurrentDateTime());
        updateUser.setUpdateAt(null);

        userRepository.save(updateUser);

        return new ResponseEntity<>(new ResponseForm<>("Account Updated", true), HttpStatus.OK);
    }

    public ResponseEntity<?> changePassword(String oldPassword, String newPassword) {
        User updatePasswordUser = this.getUserById(getCurrentUserId());

        boolean valid = this.verify(oldPassword, updatePasswordUser.getPassword());
        if (!valid) {
            throw new AuthenticateException("old password is not match");
        }
        updatePasswordUser.setPassword(this.generateHash(newPassword));

        userRepository.save(updatePasswordUser);

        return new ResponseEntity<>(new ResponseForm<>("Password Updated", true), HttpStatus.OK);
    }

    public List<User> getAllUser() {
        return userRepository.findAllByRoleName(Constant.USER_ROLE);
    }

    public User getUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new NotFoundException("no user with id = " + id);
        }
        return user;
    }
    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new AuthenticateException("Email " + email + " is invalid");
        }
        return user;
    }
    public User getUserByUserName(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username," not found!"));
        if (user == null) {
            throw new AuthenticateException("username " + username + " is invalid");
        }
        return user;
    }

    public ResponseEntity<?> resetPassword(Long id) {
        User user = this.getUserById(id);

        String defaultPass = "123456";

        String encryp = this.generateHash(defaultPass);

        user.setPassword(encryp);

        userRepository.save(user);

        return new ResponseEntity<>(new ResponseForm<>("reset password success", true), HttpStatus.OK);
    }

    public ResponseEntity<?> anaylyzeForgetPassword(String email) {
        User user = this.getUserByEmail(email);

        String code = emailService.getOTP();

        user.setCode(code);

        userRepository.save(user);

        emailService.sendEmail(user);

        return new ResponseEntity<>(new ResponseForm<>("check your email", true), HttpStatus.OK);
    }

    public ResponseEntity<?> anaylyzeRetypePassword(RetypePasswordForm retypePasswordForm) {
        System.out.println(retypePasswordForm);

        User user = this.getUserByEmail(retypePasswordForm.getEmail());

        if (!retypePasswordForm.getCode().equalsIgnoreCase(user.getCode())) {
            throw new AuthenticateException("invalid OTP code");
        }

        user.setCode(null);

        user.setPassword(this.generateHash(retypePasswordForm.getPassword()));

        userRepository.save(user);

        return new ResponseEntity<>(new ResponseForm<>("change password successfully", true), HttpStatus.OK);
    }
}
