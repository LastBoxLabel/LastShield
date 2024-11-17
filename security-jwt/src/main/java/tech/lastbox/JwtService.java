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
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
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
    private final HashSet<String> trustedIssuers;
    private final long expirationAmount;
    private final TokenStore tokenStore;
    private final Logger logger = LoggerFactory.getLogger(JwtService.class);

    /**
     * Constructs a new {@code JwtService} instance with the provided configuration.
     *
     * @param jwtConfig the JWT configuration object containing algorithm, secret key, issuers, and expiration settings
     */
    public JwtService(JwtConfig jwtConfig) {
        this.algorithm = jwtConfig.getJwtAlgorithm().getAlgorithm(jwtConfig.getSecretKey());
        this.trustedIssuers = jwtConfig.getTrustedIssuers();
        this.expirationTimeUnit = jwtConfig.getExpirationTimeUnit();
        this.expirationAmount = jwtConfig.getExpirationAmount();
        this.tokenStore = jwtConfig.getTokenStore();
    }

    /**
     * Generates a JWT for the specified subject with a given issuer and scope.
     *
     * <p>The token includes claims for expiration, issued time, issuer, and scope. It is saved
     * in the configured token store if available.
     *
     * @param subject the subject (e.g., user identifier) for whom the token is generated
     * @param issuer the trusted issuer of the token
     * @param scope the list of permissions or roles associated with the token
     * @return a {@link Token} object containing the generated token and its metadata
     * @throws TokenCreationException if the subject, issuer, or scope is invalid
     */
    @Transactional
    public Token generateToken(String subject, String issuer, List<String> scope) {
        if (subject == null || subject.isEmpty()) {
            throw new TokenCreationException("Subject must not be null or empty.");
        }

        if (scope == null || scope.isEmpty()) {
            throw new TokenCreationException("Scope must not be null or empty.");
        }

        if (!trustedIssuers.contains(issuer)) {
            throw new TokenCreationException("Issuer must be in issuers trusted list.");
        }

        Instant now = Instant.now();
        Instant expiresIn = DateUtil.getExpirationDate(now, expirationAmount, expirationTimeUnit);

        String token = JWT.create()
                .withSubject(subject)
                .withExpiresAt(expiresIn)
                .withIssuedAt(now)
                .withIssuer(issuer)
                .withClaim("scope", scope)
                .sign(algorithm);

        if (tokenStore != null) {
            TokenEntity tokenEntity = new TokenEntity(token, now, expiresIn, subject, issuer, scope);
            tokenStore.save(tokenEntity);
        }
        logger.info("Generating token with subject: {}", subject);
        return new Token(token, subject, instantToLocalDateTime(now), instantToLocalDateTime(expiresIn), issuer, scope, false);
    }

    /**
     * Revokes the specified token, marking it as invalid in the token store.
     *
     * <p>If the token does not exist in the store or the store is not configured, an exception is thrown.
     *
     * @param token the token to revoke
     * @throws TokenRevocationException if the token cannot be revoked or is not found
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
     * Retrieves and validates a token either from the token store or by decoding it directly.
     *
     * <p>If a token store is configured, the token is fetched and validated from the store.
     * Otherwise, the token is decoded and validated using the configured algorithm and issuer list.
     *
     * @param token the token to retrieve and validate
     * @return an {@link Optional} containing the valid {@link Token}, or an empty {@link Optional} if invalid or not found
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
            if (tokenEntity.isEmpty() || !tokenEntity.get().isValid()) {
                return Optional.empty();
            }
            return tokenEntity.map(TokenUtil::convertEntityToToken);
        }

        return validateDecodedToken(algorithm, trustedIssuers, token);
    }
}
