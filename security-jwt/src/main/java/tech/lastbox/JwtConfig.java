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

import java.util.HashSet;
import java.util.List;

/**
 * Configuration class for JWT (JSON Web Token) settings.
 * <p>
 * This class provides the configurations required for JWT creation and validation.
 * It supports customization of algorithms, secret keys, issuers, expiration settings,
 * and an optional token store for persistence.
 * The preset of trusted issuers contains: "Google", "GitHub", "Facebook", "Microsoft", "Twitter", "LinkedIn" and "Spotify"
 */
public class JwtConfig {
    private final JwtAlgorithm jwtAlgorithm;
    private final String secretKey;
    private final HashSet<String> trustedIssuers;
    private final long expirationAmount;
    private final ExpirationTimeUnit expirationTimeUnit;
    private final TokenStore tokenStore;
    private final List<String> DEFAULT_ISSUERS = List.of("google", "github", "facebook", "microsoft", "twitter", "linkedin", "spotify");

    /**
     * Constructs a JwtConfig instance with all configurations, including a token store.
     *
     * @param jwtAlgorithm         Algorithm used for JWT signing.
     * @param secretKey            Secret key for JWT signing.
     * @param issuer               The main issuer for JWTs.
     * @param expirationAmount     Duration before token expiration.
     * @param expirationTimeUnit   Unit of time for expiration duration.
     * @param tokenStore           Store for saving and retrieving tokens.
     * @throws AlgorithmException If the algorithm or secretKey is invalid.
     */
    public JwtConfig(
            JwtAlgorithm jwtAlgorithm,
            String secretKey,
            String issuer,
            long expirationAmount,
            ExpirationTimeUnit expirationTimeUnit,
            TokenStore tokenStore
    ) {
        validateAlgorithmAndKey(jwtAlgorithm, secretKey);
        this.jwtAlgorithm = jwtAlgorithm;
        this.secretKey = secretKey;
        this.trustedIssuers = generateTrustedIssuers(issuer);
        this.expirationAmount = expirationAmount;
        this.expirationTimeUnit = expirationTimeUnit;
        this.tokenStore = tokenStore;
    }

    /**
     * Constructs a JwtConfig without a token store.
     *
     * @param jwtAlgorithm         Algorithm used for JWT signing.
     * @param secretKey            Secret key for JWT signing.
     * @param issuer               The main issuer for JWTs.
     * @param expirationAmount     Duration before token expiration.
     * @param expirationTimeUnit   Unit of time for expiration duration.
     * @throws AlgorithmException If the algorithm or secretKey is invalid.
     */
    public JwtConfig(
            JwtAlgorithm jwtAlgorithm,
            String secretKey,
            String issuer,
            long expirationAmount,
            ExpirationTimeUnit expirationTimeUnit
    ) {
        validateAlgorithmAndKey(jwtAlgorithm, secretKey);
        this.jwtAlgorithm = jwtAlgorithm;
        this.secretKey = secretKey;
        this.trustedIssuers = generateTrustedIssuers(issuer);
        this.expirationAmount = expirationAmount;
        this.expirationTimeUnit = expirationTimeUnit;
        this.tokenStore = null;
    }

    /**
     * Constructs a JwtConfig with multiple issuers and a token store.
     *
     * @param jwtAlgorithm         Algorithm used for JWT signing.
     * @param secretKey            Secret key for JWT signing.
     * @param issuers              List of trusted issuers for JWTs.
     * @param expirationAmount     Duration before token expiration.
     * @param expirationTimeUnit   Unit of time for expiration duration.
     * @param tokenStore           Store for saving and retrieving tokens.
     * @throws AlgorithmException If the algorithm or secretKey is invalid.
     */
    public JwtConfig(
            JwtAlgorithm jwtAlgorithm,
            String secretKey,
            List<String> issuers,
            long expirationAmount,
            ExpirationTimeUnit expirationTimeUnit,
            TokenStore tokenStore
    ) {
        validateAlgorithmAndKey(jwtAlgorithm, secretKey);
        this.jwtAlgorithm = jwtAlgorithm;
        this.secretKey = secretKey;
        this.trustedIssuers = generateTrustedIssuers(issuers);
        this.expirationAmount = expirationAmount;
        this.expirationTimeUnit = expirationTimeUnit;
        this.tokenStore = tokenStore;
    }

    /**
     * Constructs a JwtConfig with multiple issuers and no token store.
     *
     * @param jwtAlgorithm         Algorithm used for JWT signing.
     * @param secretKey            Secret key for JWT signing.
     * @param issuers              List of trusted issuers for JWTs.
     * @param expirationAmount     Duration before token expiration.
     * @param expirationTimeUnit   Unit of time for expiration duration.
     * @throws AlgorithmException If the algorithm or secretKey is invalid.
     */
    public JwtConfig(
            JwtAlgorithm jwtAlgorithm,
            String secretKey,
            List<String> issuers,
            long expirationAmount,
            ExpirationTimeUnit expirationTimeUnit
    ) {
        validateAlgorithmAndKey(jwtAlgorithm, secretKey);
        this.jwtAlgorithm = jwtAlgorithm;
        this.secretKey = secretKey;
        this.trustedIssuers = generateTrustedIssuers(issuers);
        this.expirationAmount = expirationAmount;
        this.expirationTimeUnit = expirationTimeUnit;
        this.tokenStore = null;
    }

    /**
     * Validates that the provided JWT algorithm and secret key are not null or empty.
     * This ensures that the necessary cryptographic components are correctly configured.
     *
     * @param jwtAlgorithm The algorithm used for JWT signing.
     * @param secretKey    The secret key used for JWT signing.
     * @throws AlgorithmException If the algorithm is null or the secret key is null/empty.
     */
    private void validateAlgorithmAndKey(JwtAlgorithm jwtAlgorithm, String secretKey) {
        if (jwtAlgorithm == null || secretKey == null || secretKey.isEmpty()) {
            throw new AlgorithmException("JWT Algorithm and secretKey must not be null or empty");
        }
    }

    /**
     * Generates a set of trusted issuers by combining default issuers with a provided list.
     * All issuer names are converted to lowercase to ensure case-insensitivity.
     *
     * @param issuers A list of issuer names to be included as trusted.
     * @return A HashSet containing all trusted issuers.
     */
    private HashSet<String> generateTrustedIssuers(List<String> issuers) {
        HashSet<String> trustedIssuers = new HashSet<>(DEFAULT_ISSUERS);
        issuers.stream().map(String::toLowerCase).forEach(trustedIssuers::add);
        return trustedIssuers;
    }

    /**
     * Generates a set of trusted issuers by combining default issuers with a single provided issuer.
     * The issuer name is converted to lowercase to ensure case-insensitivity.
     *
     * @param issuer A single issuer name to be included as trusted.
     * @return A HashSet containing all trusted issuers.
     */
    private HashSet<String> generateTrustedIssuers(String issuer) {
        HashSet<String> trustedIssuers = new HashSet<>(DEFAULT_ISSUERS);
        trustedIssuers.add(issuer.toLowerCase());
        return trustedIssuers;
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
     * Retrieves the set of trusted issuers allowed for validating JWTs.
     *
     * @return A HashSet of trusted issuer names.
     */
    public HashSet<String> getTrustedIssuers() {
        return trustedIssuers;
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
