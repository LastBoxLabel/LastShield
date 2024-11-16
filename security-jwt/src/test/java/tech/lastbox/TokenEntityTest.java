package tech.lastbox;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class TokenEntityTest {

    private TokenEntity tokenEntity;
    private final String tokenString = "sampleToken";
    private final Instant issuedAt = Instant.now();
    private final Instant expiresIn = issuedAt.plusSeconds(3600);
    private final String subject = "user123";

    @BeforeEach
    public void setUp() {
        tokenEntity = new TokenEntity(tokenString, issuedAt, expiresIn, subject);
    }

    @Test
    public void testConstructorAndGetters() {
        assertEquals(tokenString, tokenEntity.getToken());
        assertEquals(issuedAt, tokenEntity.getIssuedAt());
        assertEquals(expiresIn, tokenEntity.getExpiresIn());
        assertEquals(subject, tokenEntity.getSubject());
    }

    @Test
    public void testRevokedFlagInitiallyFalse() {
        assertFalse(tokenEntity.isRevoked());
    }

    @Test
    public void testSetRevoked() {
        tokenEntity.setRevoked(true);
        assertTrue(tokenEntity.isRevoked());
    }

    @Test
    public void testSetRevokedBackToFalse() {
        tokenEntity.setRevoked(false);
        assertFalse(tokenEntity.isRevoked());
    }
}
