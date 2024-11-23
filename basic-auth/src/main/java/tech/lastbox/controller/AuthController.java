package tech.lastbox.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@ConditionalOnProperty(name = "lastshield.basicauth", havingValue = "true")
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
    @Operation(
            summary = "User Login",
            description = "Authenticate a user by validating their credentials. If successful, returns a JWT token to be used for further authenticated requests.",
            operationId = "loginUser",
            tags = {"Authentication"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful. Returns user details and a JWT token for authentication.",
                    content = @Content(schema = @Schema(implementation = AuthResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication failed. Incorrect username or password.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
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
    @Operation(
            summary = "User Registration",
            description = "Registers a new user in the database. If successful, returns a JWT token for immediate authentication.",
            operationId = "registerUser",
            tags = {"Authentication"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User registration successful. Returns user details and a JWT token for authentication.",
                    content = @Content(schema = @Schema(implementation = AuthResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Registration failed. The username is already in use.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
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
