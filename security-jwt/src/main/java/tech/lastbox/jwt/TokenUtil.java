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

package tech.lastbox.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Optional;

import static tech.lastbox.jwt.DateUtil.instantToLocalDateTime;

/**
 * Utility class for handling JWT (JSON Web Token) operations such as converting JWT objects to
 * the internal Token representation, validating tokens, and managing JWT-specific data.
 * <p>
 * This class is primarily responsible for transforming data between JWT format and application
 * entities, as well as validating the authenticity of a token using the specified algorithm
 * and trusted issuers.
 * </p>
 */
class TokenUtil {
    private static final Logger logger = LoggerFactory.getLogger(TokenUtil.class);

    /**
     * Converts a decoded JWT (JSON Web Token) into a {@link Token} object.
     * <p>
     * This method extracts the necessary information from the decoded JWT (e.g., token string,
     * subject, issuer, expiration time, etc.) and maps it into a {@link Token} object.
     * </p>
     *
     * @param decodedJWT The decoded JWT object.
     * @return A {@link Token} representation of the decoded JWT.
     */
    public static Token convertDecodedJwtToToken(DecodedJWT decodedJWT) {
        return new Token(decodedJWT.getToken(), decodedJWT.getSubject(),
                instantToLocalDateTime(decodedJWT.getIssuedAtAsInstant()),
                instantToLocalDateTime(decodedJWT.getExpiresAtAsInstant()),
                decodedJWT.getIssuer(), decodedJWT.getClaim("scope").asList(String.class), false);
    }

    /**
     * Converts a {@link TokenEntity} object into a {@link Token}.
     * <p>
     * This method maps the properties of the {@link TokenEntity} (such as token string, issued
     * at time, expiration time, subject, scope, and revocation status) into a {@link Token} object.
     * </p>
     *
     * @param tokenEntity The {@link TokenEntity} object to be converted.
     * @return A {@link Token} object created from the {@link TokenEntity}.
     */
    public static Token convertEntityToToken(TokenEntity tokenEntity) {
        return new Token(tokenEntity.getToken(), tokenEntity.getSubject(),
                instantToLocalDateTime(tokenEntity.getIssuedAt()),
                instantToLocalDateTime(tokenEntity.getExpiresIn()),
                tokenEntity.getIssuer(), tokenEntity.getScope(), tokenEntity.isRevoked());
    }

    /**
     * Validates a JWT by checking its issuer and verifying it with the provided algorithm.
     * <p>
     * This method ensures that the token's issuer is in the list of trusted issuers and that the
     * token is properly signed using the provided algorithm. If valid, it returns an {@link Optional}
     * containing the corresponding {@link Token}. Otherwise, it returns an empty {@link Optional}.
     * </p>
     *
     * @param algorithm The algorithm to be used for token verification.
     * @param trustedIssuers A set of trusted issuers to validate the token's issuer.
     * @param token The JWT to be validated.
     * @return An {@link Optional} containing a valid {@link Token} if the token is valid, otherwise an empty {@link Optional}.
     */
    public static Optional<Token> validateDecodedToken(Algorithm algorithm, HashSet<String> trustedIssuers, String token) {
        try {
            String issuer = JWT.decode(token).getIssuer();

            if (trustedIssuers.contains(issuer.toLowerCase())) {
                DecodedJWT decodedJWT = JWT.require(algorithm)
                        .withIssuer(issuer)
                        .build()
                        .verify(token);
                return Optional.of(convertDecodedJwtToToken(decodedJWT));
            } else {
                logger.error("Token validation failed: Issuer '{}' is not trusted. Token: {}", issuer, token);
                return Optional.empty();
            }
        } catch (Exception e) {
            logger.error("Token validation failed for token '{}'. Exception: {}. Possible causes: 1. Invalid signature or 2. Issuer mismatch. Error details: {}",
                    token, e.getLocalizedMessage(), e.getMessage(), e);
            return Optional.empty();
        }
    }
}
