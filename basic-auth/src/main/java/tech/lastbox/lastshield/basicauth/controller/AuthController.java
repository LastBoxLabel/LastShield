/*
 * Copyright 2024 LastBox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tech.lastbox.lastshield.basicauth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.lastbox.jwt.JwtService;
import tech.lastbox.jwt.Token;
import tech.lastbox.lastshield.basicauth.dto.AuthResponseDTO;
import tech.lastbox.lastshield.basicauth.dto.ErrorResponse;
import tech.lastbox.lastshield.basicauth.dto.LoginRequest;
import tech.lastbox.lastshield.basicauth.dto.RegisterRequest;
import tech.lastbox.lastshield.basicauth.entity.User;
import tech.lastbox.lastshield.basicauth.enviroment.BasicAuthProperties;
import tech.lastbox.lastshield.basicauth.exception.DuplicatedUserException;
import tech.lastbox.lastshield.basicauth.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Controller responsible for authentication and user management, including login and registration.
 * This controller exposes endpoints to authenticate users and register new users, generating
 * JWT tokens for valid users.
 */
@RestController
@ConditionalOnProperty(name = "lastshield.basicauth", havingValue = "true")
@Schema(description = "Handles authentication and user registration for the application.")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final BasicAuthProperties basicAuthProperties;

    public AuthController(UserService userService, JwtService jwtService, BasicAuthProperties basicAuthProperties) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.basicAuthProperties = basicAuthProperties;
    }

    /**
     * Endpoint for user login. Validates credentials and returns a JWT token.
     *
     * @param loginRequest the login credentials containing the username and password
     * @return ResponseEntity with the login result and JWT token
     */
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
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> userOptional = userService.login(loginRequest.username(), loginRequest.password());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Invalid username or password.", HttpStatus.UNAUTHORIZED.toString(), LocalDateTime.now()));
        }
        User user = userOptional.get();
        Token token = jwtService.generateToken(loginRequest.username(), basicAuthProperties.getIssuer());
        return ResponseEntity.status(HttpStatus.OK).body(new AuthResponseDTO(user.getId(), token.token(), "Login successful.", LocalDateTime.now()));
    }

    /**
     * Endpoint for user registration. Registers a new user and returns a JWT token for authentication.
     *
     * @param registerRequest the user registration details
     * @return ResponseEntity with the registration result and JWT token
     */
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
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            User user = userService.createUser(registerRequest.name(), registerRequest.username(), registerRequest.password());
            Token token = jwtService.generateToken(user.getUsername(), basicAuthProperties.getIssuer());
            return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponseDTO(user.getId(), token.token(), "User created successfully.", LocalDateTime.now()));
        } catch (DuplicatedUserException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(e.getMessage(), HttpStatus.CONFLICT.toString(), LocalDateTime.now()));
        }
    }
}
