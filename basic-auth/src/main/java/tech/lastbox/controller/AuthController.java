package tech.lastbox.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.lastbox.JwtService;
import tech.lastbox.Token;
import tech.lastbox.dto.*;
import tech.lastbox.entity.User;
import tech.lastbox.enviroment.BasicAuthProperties;
import tech.lastbox.exception.DuplicatedUserException;
import tech.lastbox.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;
    private final BasicAuthProperties basicAuthProperties;

    public AuthController(UserService userService, JwtService jwtService, BasicAuthProperties basicAuthProperties) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.basicAuthProperties = basicAuthProperties;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        Optional<User> userOptional = userService.login(loginRequest.username(), loginRequest.password());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Invalid username or password.", HttpStatus.UNAUTHORIZED.toString(), LocalDateTime.now()));
        }
        User user = userOptional.get();
        Token token = jwtService.generateToken(loginRequest.username(), basicAuthProperties.getIssuer());
        return ResponseEntity.status(HttpStatus.OK).body(new AuthResponseDTO(user.getId(), token.token(), "Login successful.", LocalDateTime.now()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO registerRequest) {
        try {
            User user = userService.createUser(registerRequest.name(), registerRequest.username(), registerRequest.password());
            Token token = jwtService.generateToken(user.getUsername(), basicAuthProperties.getIssuer());
            return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponseDTO(user.getId(), token.token(), "User created sucessfully.", LocalDateTime.now()));
        } catch (DuplicatedUserException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(e.getMessage(), HttpStatus.CONFLICT.toString(), LocalDateTime.now()));
        }
    }
}
