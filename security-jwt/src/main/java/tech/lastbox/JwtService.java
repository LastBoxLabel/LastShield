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

package tech.lastbox;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

import static tech.lastbox.DateUtil.*;
import static tech.lastbox.TokenUtil.*;

/**
 * Service class responsible for managing JWT creation, validation, and revocation.
 * It generates JWTs, validates them, and can revoke existing tokens.
 */
public class JwtService {
    private final Algorithm algorithm;
    private final ExpirationTimeUnit expirationTimeUnit;
    private final String issuer;
    private final long expirationAmount;
    private final TokenStore tokenStore;
    private final Logger logger = LoggerFactory.getLogger(JwtService.class);

    /**
     * Constructs a JwtService instance with the provided JWT configuration.
     *
     * @param jwtConfig The configuration object that contains JWT settings.
     */
    public JwtService(JwtConfig jwtConfig) {
        this.algorithm = jwtConfig.getJwtAlgorithm().getAlgorithm(jwtConfig.getSecretKey());
        this.issuer = jwtConfig.getIssuer();
        this.expirationTimeUnit = jwtConfig.getExpirationTimeUnit();
        this.expirationAmount = jwtConfig.getExpirationAmount();
        this.tokenStore = jwtConfig.getTokenStore();
    }

    /**
     * Generates a new JWT for a given subject.
     * The token is created with an expiration time and issuer.
     * If a token store is provided, the token is also saved.
     *
     * @param subject The subject (typically the user) for whom the token is generated.
     * @return A {@link Token} object containing the generated token details.
     * @throws TokenCreationException If the subject is null or empty.
     */
    @Transactional
    public Token generateToken(String subject) {
        if (subject == null || subject.isEmpty()) {
            throw new TokenCreationException("Subject must not be null or empty.");
        }
        Instant now = Instant.now();
        Instant expiresIn = DateUtil.getExpirationDate(now, expirationAmount, expirationTimeUnit);

        String token = JWT.create()
                .withSubject(subject)
                .withExpiresAt(expiresIn)
                .withIssuedAt(now)
                .withIssuer(issuer)
                .sign(algorithm);

        if (tokenStore != null) {
            TokenEntity tokenEntity = new TokenEntity(token, now, expiresIn, subject);
            tokenStore.save(tokenEntity);
        }
        logger.info("Generating token with subject: {}", subject);
        return new Token(token, subject, instantToLocalDateTime(now), instantToLocalDateTime(expiresIn), false);
    }

    /**
     * Revokes an existing token by marking it as revoked in the token store.
     * If the token does not exist in the repository, an exception is thrown.
     *
     * @param token The token to revoke.
     * @throws TokenRevocationException If the token is not found in the repository or cannot be revoked.
     */
    @Transactional
    public void revokeToken(String token) {
        if (tokenStore != null) {
            tokenStore.findById(token).ifPresentOrElse(tokenEntity -> {
                if (tokenEntity.isRevoked()) {
                    logger.info("Token is already revoked: {}", token);
                } else {
                    tokenEntity.setRevoked(true);
                    tokenStore.save(tokenEntity);
                    logger.info("Token revoked successfully: {}", token);
                }
            }, () -> {
                logger.error("Cannot revoke non-existent token: {}", token);
                throw new TokenRevocationException("Token not found in the repository.");
            });
        } else {
            logger.error("Cannot revoke token without a configured store.");
            throw new TokenRevocationException("Token Store not configured.");
        }
    }

    /**
     * Validates a token by checking its existence and validity.
     *
     * @param token The token to validate.
     * @return A {@link TokenValidation} object containing the validation result.
     */
    public TokenValidation validateToken(String token) {
        Optional<Token> tokenOptional = getToken(token);
        return new TokenValidation(tokenOptional, tokenOptional.isPresent());
    }

    /**
     * Retrieves and validates a token from the token store or decodes it if the store is not available.
     * <p>
     * If the token is found in the store and is valid, it is returned as a {@link Token}. If not found
     * or invalid, an empty {@link Optional} is returned. If the token store is not available, the token
     * is decoded and validated using the configured algorithm and issuer.
     *
     * @param token The token to retrieve and validate.
     * @return An {@link Optional} containing the valid {@link Token}, or an empty {@link Optional} if invalid or not found.
     */
    public Optional<Token> getToken(String token) {
        if (token == null || token.isEmpty()) {
            logger.error("Token is null or empty");
            return Optional.empty();
        }

        if (tokenStore != null) {
            Optional<TokenEntity> tokenEntity = tokenStore.findById(token);
            if (tokenEntity.isEmpty() || !TokenUtil.isTokenValid(tokenEntity.get())) {
                return Optional.empty();
            }
            return tokenEntity.map(TokenUtil::convertEntityToToken);
        }

        return validateDecodedToken(algorithm, issuer, token);
    }
}
