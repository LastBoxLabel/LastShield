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

import com.auth0.jwt.algorithms.Algorithm;

/**
 * Enum representing the different JWT signing algorithms.
 * This enum provides various HMAC (Hash-based Message Authentication Code) algorithms
 * for signing JSON Web Tokens (JWTs).
 * <p>
 * Each constant in this enum represents a different HMAC algorithm variant (HMAC256, HMAC384, HMAC512).
 * The corresponding algorithm can be obtained by providing a secret key, which will be used to create a JWT signature.
 */
public enum JwtAlgorithm {

    /** HMAC256 algorithm for signing JWTs using a 256-bit key. */
    HMAC256 {
        @Override
        public Algorithm getAlgorithm(String secretKey) {
            return Algorithm.HMAC256(secretKey);
        }
    },

    /** HMAC384 algorithm for signing JWTs using a 384-bit key. */
    HMAC384 {
        @Override
        public Algorithm getAlgorithm(String secretKey) {
            return Algorithm.HMAC384(secretKey);
        }
    },

    /** HMAC512 algorithm for signing JWTs using a 512-bit key. */
    HMAC512 {
        @Override
        public Algorithm getAlgorithm(String secretKey) {
            return Algorithm.HMAC512(secretKey);
        }
    };

    /**
     * Abstract method that must be implemented by each constant to return the
     * corresponding {@link Algorithm} instance using the provided secret key.
     *
     * @param secretKey the secret key to be used for signing the JWT.
     * @return the {@link Algorithm} instance corresponding to the selected HMAC variant.
     */
    public abstract Algorithm getAlgorithm(String secretKey);
}
