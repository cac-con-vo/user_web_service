package com.example.user_web_service.controller;

import com.example.user_web_service.dto.ResponseObject;
import com.example.user_web_service.dto.UserDTO;
import com.example.user_web_service.entity.User;
import com.example.user_web_service.exception.UserNotFoundException;
import com.example.user_web_service.form.*;
import com.example.user_web_service.repository.UserRepository;
import com.example.user_web_service.security.userprincipal.Principal;
import com.example.user_web_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController extends BaseController {

    @Autowired
    UserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll() {
        List<User> allUser = userService.getAllUser();
        ResponseForm<List<User>> responseForm = new ResponseForm<>();
        responseForm.setData(allUser);
        responseForm.setMessage("get Users successfully");
        responseForm.setResult(true);

        return new ResponseEntity<>(responseForm, HttpStatus.OK);
    }

    @PostMapping(value = "/resetPassword/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> resetPassword(@PathVariable("id") Long id) {

        return userService.resetPassword(id);
    }

    @PostMapping(value = "/forget-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> forgetPassword(@RequestBody String email) {
        return userService.anaylyzeForgetPassword(email);
    }

    @PostMapping(value = "/retype-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> retypePassword(@RequestBody RetypePasswordForm retypePasswordForm) {
        return userService.anaylyzeRetypePassword(retypePasswordForm);
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginForm loginForm) {
        return userService.authenticateUser(loginForm);
    }

    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> profile() {
        Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(principal.getUsername()).orElseThrow(
                ()-> new UserNotFoundException(principal.getUsername(),"User not found")
        );
        ModelMapper modelMapper = new ModelMapper();
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        ResponseForm<UserDTO> responseForm = new ResponseForm<>();
        responseForm.setData(userDTO);
        responseForm.setMessage("get profile successfully");
        responseForm.setResult(true);
        return new ResponseEntity<>(responseForm, HttpStatus.OK);
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@RequestBody UpdateUserForm updateUserForm) throws ParseException {
        return userService.updateUser(updateUserForm);
    }

    @PutMapping(value = "/change-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePassword(@RequestBody UpdatePasswordForm updatePasswordForm) {
        return userService.changePassword(updatePasswordForm.getOldPassword(), updatePasswordForm.getNewPassword());
    }

    @GetMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> logout() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(null);

        return new ResponseEntity<>(new ResponseForm<>("logout complete", true), HttpStatus.OK);
    }

    @PostMapping(value = "/signUp", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> add(@RequestBody SignUpForm signUpForm) throws ParseException {
        return userService.createUser(signUpForm);
    }


}
