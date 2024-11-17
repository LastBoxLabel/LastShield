package tech.lastbox;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @Mock
    private TokenStore tokenStoreMock;

    private JwtService jwtService;

    private final String subject = "testSubject";

    private JwtConfig jwtConfig;

    @BeforeEach
    public void setUp() {
        JwtAlgorithm jwtAlgorithm = JwtAlgorithm.HMAC256;
        String secretKey = "secretKey";
        String issuer = "issuer";
        long expirationAmount = 1L;
        ExpirationTimeUnit expirationTimeUnit = ExpirationTimeUnit.DAYS;

        jwtConfig = new JwtConfig(jwtAlgorithm, secretKey, issuer, expirationAmount, expirationTimeUnit, tokenStoreMock);

        jwtService = new JwtService(jwtConfig);
    }

    @Test
    public void testGenerateToken_Success() {
        TokenEntity tokenEntity = new TokenEntity("token", Instant.now(), Instant.now().plusSeconds(3600), subject);
        doNothing().when(tokenStoreMock).save(any(TokenEntity.class));

        Token generatedToken = jwtService.generateToken(subject);

        assertNotNull(generatedToken);
        assertEquals(subject, generatedToken.subject());
        assertNotNull(generatedToken.token());
        verify(tokenStoreMock, times(1)).save(any(TokenEntity.class));
    }

    @Test
    public void testGenerateToken_InvalidSubject() {
        String invalidSubject = "";

        TokenCreationException exception = assertThrows(TokenCreationException.class, () -> {
            jwtService.generateToken(invalidSubject);
        });

        assertEquals("Subject must not be null or empty.", exception.getMessage());
    }

    @Test
    public void testRevokeToken_Success() {
        String token = "sampleToken";
        TokenEntity tokenEntity = new TokenEntity(token, Instant.now(), Instant.now().plusSeconds(3600), subject);
        when(tokenStoreMock.findById(token)).thenReturn(Optional.of(tokenEntity));

        jwtService.revokeToken(token);

        verify(tokenStoreMock, times(1)).save(tokenEntity);
        assertTrue(tokenEntity.isRevoked());
    }

    @Test
    public void testRevokeToken_NotFound() {
        String token = "sampleToken";
        when(tokenStoreMock.findById(token)).thenReturn(Optional.empty());

        TokenRevocationException exception = assertThrows(TokenRevocationException.class, () -> {
            jwtService.revokeToken(token);
        });

        assertEquals("Token not found in the repository.", exception.getMessage());
    }

    @Test
    public void testRevokeToken_AlreadyRevoked() {
        String token = "sampleToken";
        TokenEntity tokenEntity = new TokenEntity(token, Instant.now(), Instant.now().plusSeconds(3600), subject);
        tokenEntity.setRevoked(true);
        when(tokenStoreMock.findById(token)).thenReturn(Optional.of(tokenEntity));

        jwtService.revokeToken(token);

        verify(tokenStoreMock, never()).save(tokenEntity);
    }

    @Test
    public void testValidateToken_ValidToken() {
        String token = "validToken";
        TokenEntity tokenEntity = new TokenEntity(token, Instant.now(), Instant.now().plusSeconds(3600), subject);
        when(tokenStoreMock.findById(token)).thenReturn(Optional.of(tokenEntity));

        TokenValidation validation = jwtService.validateToken(token);

        assertTrue(validation.isValid());
        assertTrue(validation.tokenOptional().isPresent());
    }

    @Test
    public void testValidateToken_InvalidToken() {
        String token = "invalidToken";
        when(tokenStoreMock.findById(token)).thenReturn(Optional.empty());

        TokenValidation validation = jwtService.validateToken(token);

        assertFalse(validation.isValid());
        assertFalse(validation.tokenOptional().isPresent());
    }

    @Test
    public void testGetToken_ValidToken() {
        String token = "validToken";
        TokenEntity tokenEntity = new TokenEntity(token, Instant.now(), Instant.now().plusSeconds(3600), subject);
        when(tokenStoreMock.findById(token)).thenReturn(Optional.of(tokenEntity));

        Optional<Token> retrievedToken = jwtService.getToken(token);

        assertTrue(retrievedToken.isPresent());
        assertEquals(token, retrievedToken.get().token());
    }

    @Test
    public void testGetToken_InvalidToken() {
        String token = "invalidToken";
        when(tokenStoreMock.findById(token)).thenReturn(Optional.empty());

        Optional<Token> retrievedToken = jwtService.getToken(token);

        assertFalse(retrievedToken.isPresent());
    }

    @Test
    public void testGetToken_TokenStoreIsNull() {
        JwtService jwtServiceNoStore = new JwtService(jwtConfig);
        String token = "validToken";

        Optional<Token> retrievedToken = jwtServiceNoStore.getToken(token);

        assertFalse(retrievedToken.isPresent());
    }
}
