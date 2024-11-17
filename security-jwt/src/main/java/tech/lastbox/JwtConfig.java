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

/**
 * Configuration class for JWT (JSON Web Token) settings.
 * This class holds the configuration details required for JWT creation,
 * such as the algorithm, secret key, expiration time, and token store.
 * It provides the necessary methods to retrieve these configurations.
 */
public class JwtConfig {
    private final JwtAlgorithm jwtAlgorithm;
    private final String secretKey;
    private final String issuer;
    private final long expirationAmount;
    private final ExpirationTimeUnit expirationTimeUnit;
    private final TokenStore tokenStore;

    /**
     * Constructs a JwtConfig with all necessary configurations including token store.
     *
     * @param jwtAlgorithm         The algorithm used for signing the JWT.
     * @param secretKey            The secret key used for JWT signing.
     * @param issuer               The issuer of the JWT.
     * @param expirationAmount     The amount of time before the JWT expires.
     * @param expirationTimeUnit   The unit of time for expiration (e.g., minutes, hours).
     * @param tokenStore           The TokenStore for saving and retrieving token entities.
     * @throws AlgorithmException If jwtAlgorithm or secretKey is null or empty.
     */
    public JwtConfig(JwtAlgorithm jwtAlgorithm, String secretKey, String issuer, long expirationAmount, ExpirationTimeUnit expirationTimeUnit, TokenStore tokenStore) {
        if (jwtAlgorithm == null || secretKey == null || secretKey.isEmpty()) {
            throw new AlgorithmException("JWT Algorithm and secretKey must not be null or empty");
        }
        this.jwtAlgorithm = jwtAlgorithm;
        this.secretKey = secretKey;
        this.issuer = issuer;
        this.expirationAmount = expirationAmount;
        this.expirationTimeUnit = expirationTimeUnit;
        this.tokenStore = tokenStore;
    }

    /**
     * Constructs a JwtConfig without a token store.
     *
     * @param jwtAlgorithm         The algorithm used for signing the JWT.
     * @param secretKey            The secret key used for JWT signing.
     * @param issuer               The issuer of the JWT.
     * @param expirationAmount     The amount of time before the JWT expires.
     * @param expirationTimeUnit   The unit of time for expiration (e.g., minutes, hours).
     * @throws AlgorithmException If jwtAlgorithm or secretKey is null or empty.
     */
    public JwtConfig(JwtAlgorithm jwtAlgorithm, String secretKey, String issuer, long expirationAmount, ExpirationTimeUnit expirationTimeUnit) {
        if (jwtAlgorithm == null || secretKey == null || secretKey.isEmpty()) {
            throw new AlgorithmException("JWT Algorithm and secretKey must not be null or empty");
        }
        this.jwtAlgorithm = jwtAlgorithm;
        this.secretKey = secretKey;
        this.issuer = issuer;
        this.expirationAmount = expirationAmount;
        this.expirationTimeUnit = expirationTimeUnit;
        this.tokenStore = null;
    }

    /**
     * Gets the JWT signing algorithm.
     *
     * @return The JWT signing algorithm.
     */
    public JwtAlgorithm getJwtAlgorithm() {
        return jwtAlgorithm;
    }

    /**
     * Gets the secret key used for JWT signing.
     *
     * @return The secret key.
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * Gets the issuer of the JWT.
     *
     * @return The issuer of the JWT.
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * Gets the expiration time unit used for the JWT.
     *
     * @return The expiration time unit.
     */
    public long getExpirationAmount() {
        return expirationAmount;
    }

    /**
     * Gets the expiration time unit used for the JWT.
     *
     * @return The expiration time unit.
     */
    public ExpirationTimeUnit getExpirationTimeUnit() {
        return expirationTimeUnit;
    }

    /**
     * Gets the token store used for saving and retrieving token entities.
     * If no token store is configured, this returns null.
     *
     * @return The token store, or null if not configured.
     */
    public TokenStore getTokenStore() {
        return tokenStore;
    }
}
