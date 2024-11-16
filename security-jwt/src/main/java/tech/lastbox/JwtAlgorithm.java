package tech.lastbox;

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
