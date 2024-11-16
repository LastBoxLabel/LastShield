package tech.lastbox;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TokenStoreTest {

    private TokenStore tokenStoreMock;
    private TokenEntity tokenEntity;
    private final String tokenString = "sampleToken";

    @BeforeEach
    public void setUp() {
        tokenStoreMock = mock(TokenStore.class);
        tokenEntity = new TokenEntity(tokenString, Instant.now(), Instant.now().plusSeconds(3600), "user123");
    }

    @Test
    public void testSaveToken() {
        doNothing().when(tokenStoreMock).save(tokenEntity);

        tokenStoreMock.save(tokenEntity);

        verify(tokenStoreMock, times(1)).save(tokenEntity);
    }

    @Test
    public void testFindByIdToken() {
        when(tokenStoreMock.findById(tokenString)).thenReturn(Optional.of(tokenEntity));

        Optional<TokenEntity> result = tokenStoreMock.findById(tokenString);

        assertTrue(result.isPresent());
        assertEquals(tokenString, result.get().getToken());
    }

    @Test
    public void testFindByIdTokenNotFound() {
        when(tokenStoreMock.findById(tokenString)).thenReturn(Optional.empty());

        Optional<TokenEntity> result = tokenStoreMock.findById(tokenString);

        assertFalse(result.isPresent());
    }
}
