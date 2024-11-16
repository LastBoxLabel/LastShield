package tech.lastbox;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Optional;

import static tech.lastbox.DateUtil.instantToLocalDateTime;

class TokenUtil {
    private static final Logger logger = LoggerFactory.getLogger(TokenUtil.class);

    public static boolean isTokenValid(TokenEntity tokenEntity) {
        if (tokenEntity == null) {
            return false;
        }
        Instant now = Instant.now();
        return Boolean.FALSE.equals(tokenEntity.isRevoked()) &&
                tokenEntity.getExpiresIn() != null &&
                tokenEntity.getExpiresIn().isAfter(now);
    }

    public static Token convertDecodedJwtToToken(DecodedJWT decodedJWT) {
        return new Token(decodedJWT.getToken(), decodedJWT.getSubject(),
                instantToLocalDateTime(decodedJWT.getIssuedAtAsInstant()),
                instantToLocalDateTime(decodedJWT.getExpiresAtAsInstant()),
                false);
    }

    public static Token convertEntityToToken(TokenEntity tokenEntity) {
        return new Token(tokenEntity.getToken(), tokenEntity.getSubject(),
                instantToLocalDateTime(tokenEntity.getIssuedAt()),
                instantToLocalDateTime(tokenEntity.getExpiresIn()),
                tokenEntity.isRevoked());
    }

    public static Optional<Token> validateDecodedToken(Algorithm algorithm, String issuer, String token) {
        try {
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(token);
            return Optional.of(convertDecodedJwtToToken(decodedJWT));
        } catch (Exception e) {
            logger.error("Error validating token: {}. Exception: {}", token, e, e);
            return Optional.empty();
        }
    }
}
