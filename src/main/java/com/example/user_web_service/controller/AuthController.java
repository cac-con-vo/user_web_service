package com.example.user_web_service.controller;

import com.example.user_web_service.dto.ResponseObject;
import com.example.user_web_service.entity.User;
import com.example.user_web_service.form.*;
import com.example.user_web_service.payload.response.LoginGameResponse;

import com.example.user_web_service.repository.UserRepository;
import com.example.user_web_service.security.jwt.JwtProvider;
import com.example.user_web_service.security.jwt.JwtResponse;
import com.example.user_web_service.security.jwt.RefreshTokenProvider;

import com.example.user_web_service.security.userprincipal.Principal;
import com.example.user_web_service.service.impl.AuthServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin
public class AuthController {
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;
    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private RefreshTokenProvider refreshTokenProvider;

    @Operation(summary = "For login", description = "test")
    @PostMapping(value="/auth/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirements
    public ResponseEntity<ResponseObject> login(@Valid @RequestBody LoginForm loginForm) {
        if(loginForm.getUsername().isEmpty() || loginForm.getUsername().isBlank() || loginForm.getUsername() == null){
            return new ResponseEntity<ResponseObject>(new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Please input username", null, null), HttpStatus.BAD_REQUEST);
        }
        if(loginForm.getPassword().isEmpty() || loginForm.getPassword().isBlank() || loginForm.getPassword() == null){
            return new ResponseEntity<ResponseObject>(new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Please input password", null, null), HttpStatus.BAD_REQUEST);
        }
        return authService.login(loginForm);
    }
    @PostMapping(value = "/auth/validation", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "For getting user information after login")
    public ResponseEntity<ResponseObject> reloadUserByJWT() {
        return authService.validateAccessToken();
    }

    @PostMapping(value = "/auth/accessToken", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "For getting new access token by refresh token after it expired")
    public ResponseEntity<ResponseObject> refreshAccessToken(HttpServletRequest request, @Valid @RequestBody RefreshTokenForm refreshTokenForm) {
        if(refreshTokenForm.getRefreshToken() == null || refreshTokenForm.getRefreshToken().isEmpty() || refreshTokenForm.getRefreshToken().isBlank()){
            return new ResponseEntity<ResponseObject>(new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Please input refresh token", null, null), HttpStatus.BAD_REQUEST);
        }
        return authService.refreshAccessToken(request, refreshTokenForm);
    }

    @PostMapping(value = "/auth/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "For logout")
    public ResponseEntity<ResponseObject> logout(HttpServletRequest request, @Valid @RequestBody LogoutForm logoutForm) {
        if(logoutForm.getRefreshToken() == null || logoutForm.getRefreshToken().isEmpty() || logoutForm.getRefreshToken().isBlank()){
            return new ResponseEntity<ResponseObject>(new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Please input refresh token", null, null), HttpStatus.BAD_REQUEST);
        }
        return authService.logout(request, logoutForm);
    }
    @PostMapping(value = "/auth/loginGame", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "For login game")
    public ResponseEntity<LoginGameResponse> loginGame(HttpServletRequest request,@RequestBody AccessTokenForm accessTokenForm) {
        if(accessTokenForm.getAccessToken() == null || accessTokenForm.getAccessToken()
                .isEmpty() || accessTokenForm.getAccessToken().isBlank()){
            return new ResponseEntity<LoginGameResponse>(new LoginGameResponse(HttpStatus.BAD_REQUEST.toString(), "Please input refresh token",  null, null, null), HttpStatus.BAD_REQUEST);
        }
        return authService.validateAccessTokenForLoginGame(request, accessTokenForm);
    }

    @GetMapping("/google/login")
    public RedirectView googleLogin() {
        return new RedirectView("/oauth2/authorization/google");
    }

    @GetMapping("/google/callback")
    public ResponseEntity<ResponseObject> googleCallback(OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());
        String accessToken = client.getAccessToken().getTokenValue();
        String refreshToken = client.getRefreshToken().getTokenValue();

        // Kiểm tra xem tài khoản Google có trùng khớp với email được lưu trong cơ sở dữ liệu hay không
        String email = getEmailFromGoogleAccount(accessToken);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            // Nếu không tìm thấy tài khoản người dùng trong cơ sở dữ liệu, trả về mã trạng thái 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(HttpStatus.UNAUTHORIZED.toString(), "Google account does not match any user account!", null, null));
        }

        // Nếu tìm thấy tài khoản người dùng trong cơ sở dữ liệu, trả về mã trạng thái 200 OK và thông tin người dùng
        // Lưu thông tin đăng nhập vào phiên làm việc của người dùng
        Authentication authentications = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentications);
        Principal userPrinciple = (Principal) authentications.getPrincipal();
        String newAccessToken = jwtProvider.createToken(userPrinciple);
        String newRefreshToken = refreshTokenProvider.createRefreshToken(user.getEmail()).getToken();
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.toString(), "Login success!", null, new JwtResponse(newAccessToken, newRefreshToken)));
    }


    private String getEmailFromGoogleAccount(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange("https://www.googleapis.com/oauth2/v3/userinfo", HttpMethod.GET, entity, Map.class);
        return responseEntity.getBody().get("email").toString();
    }




}
