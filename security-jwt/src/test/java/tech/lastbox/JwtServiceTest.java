package tech.lastbox;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @Mock
    private TokenStore tokenStoreMock;

    private JwtService jwtService;

    private final String subject = "testSubject";
    private final String issuer = "issuer";
    private final String token = "sampleToken";
    private final List<String> scope = List.of("admin", "user");

    private JwtConfig jwtConfig;

    @BeforeEach
    public void setUp() {
        JwtAlgorithm jwtAlgorithm = JwtAlgorithm.HMAC256;
        String secretKey = "secretKey";
        long expirationAmount = 1L;
        ExpirationTimeUnit expirationTimeUnit = ExpirationTimeUnit.DAYS;
        List<String> trustedIssuers = new ArrayList<>();
        trustedIssuers.add("issuer");

        jwtConfig = new JwtConfig(jwtAlgorithm, secretKey, trustedIssuers, expirationAmount, expirationTimeUnit, tokenStoreMock);
        jwtService = new JwtService(jwtConfig);
    }

    @Test
    public void testGenerateToken_Success() {
        Instant now = Instant.now();
        Instant expiresIn = DateUtil.getExpirationDate(now, jwtConfig.getExpirationAmount(), jwtConfig.getExpirationTimeUnit());
        TokenEntity tokenEntity = new TokenEntity(token, now, expiresIn, subject, issuer, scope);
        doNothing().when(tokenStoreMock).save(any(TokenEntity.class));

        Token generatedToken = jwtService.generateToken(subject, issuer, scope);

        assertNotNull(generatedToken);
        assertEquals(subject, generatedToken.subject());
        assertEquals(issuer, generatedToken.issuer());
        assertTrue(generatedToken.scope().contains("admin"));
        assertNotNull(generatedToken.token());
        assertFalse(generatedToken.isRevoked());
        verify(tokenStoreMock, times(1)).save(any(TokenEntity.class));
    }

    @Test
    public void testGenerateToken_InvalidSubject() {
        TokenCreationException exception = assertThrows(TokenCreationException.class, () -> {
            jwtService.generateToken("", issuer, scope);
        });
        assertEquals("Subject must not be null or empty.", exception.getMessage());
    }

    @Test
    public void testGenerateToken_InvalidScope() {
        TokenCreationException exception = assertThrows(TokenCreationException.class, () -> {
            jwtService.generateToken(subject, issuer, null);
        });
        assertEquals("Scope must not be null or empty.", exception.getMessage());
    }

    @Test
    public void testGenerateToken_InvalidIssuer() {
        TokenCreationException exception = assertThrows(TokenCreationException.class, () -> {
            jwtService.generateToken(subject, "untrustedIssuer", scope);
        });
        assertEquals("Issuer must be in issuers trusted list.", exception.getMessage());
    }

    @Test
    public void testRevokeToken_Success() {
        TokenEntity tokenEntity = new TokenEntity(token, Instant.now(), Instant.now().plusSeconds(3600), subject, issuer, scope);
        when(tokenStoreMock.findById(token)).thenReturn(Optional.of(tokenEntity));
        doNothing().when(tokenStoreMock).save(any(TokenEntity.class));

        jwtService.revokeToken(token);

        verify(tokenStoreMock, times(1)).save(tokenEntity);
        assertTrue(tokenEntity.isRevoked());
    }

    @Test
    public void testRevokeToken_NotFound() {
        when(tokenStoreMock.findById(token)).thenReturn(Optional.empty());

        TokenRevocationException exception = assertThrows(TokenRevocationException.class, () -> {
            jwtService.revokeToken(token);
        });

        assertEquals("Token not found in the repository.", exception.getMessage());
    }

    @Test
    public void testRevokeToken_AlreadyRevoked() {
        TokenEntity tokenEntity = new TokenEntity(token, Instant.now(), Instant.now().plusSeconds(3600), subject, issuer, scope);
        tokenEntity.setRevoked(true);
        when(tokenStoreMock.findById(token)).thenReturn(Optional.of(tokenEntity));

        jwtService.revokeToken(token);

        verify(tokenStoreMock, never()).save(tokenEntity);
        assertTrue(tokenEntity.isRevoked());
    }

    @Test
    public void testValidateToken_ValidToken() {
        TokenEntity tokenEntity = new TokenEntity(token, Instant.now(), Instant.now().plusSeconds(3600), subject, issuer, scope);
        when(tokenStoreMock.findById(token)).thenReturn(Optional.of(tokenEntity));

        TokenValidation validation = jwtService.validateToken(token);

        assertTrue(validation.isValid());
        assertTrue(validation.tokenOptional().isPresent());
        assertEquals(token, validation.tokenOptional().get().token());
    }

    @Test
    public void testValidateToken_InvalidToken() {
        when(tokenStoreMock.findById(token)).thenReturn(Optional.empty());

        TokenValidation validation = jwtService.validateToken(token);

        assertFalse(validation.isValid());
        assertFalse(validation.tokenOptional().isPresent());
    }

    @Test
    public void testGetToken_ValidToken() {
        TokenEntity tokenEntity = new TokenEntity(token, Instant.now(), Instant.now().plusSeconds(3600), subject, issuer, scope);
        when(tokenStoreMock.findById(token)).thenReturn(Optional.of(tokenEntity));

        Optional<Token> retrievedToken = jwtService.getToken(token);

        assertTrue(retrievedToken.isPresent());
        assertEquals(token, retrievedToken.get().token());
    }

    @Test
    public void testGetToken_InvalidToken() {
        when(tokenStoreMock.findById(token)).thenReturn(Optional.empty());

        Optional<Token> retrievedToken = jwtService.getToken(token);

        assertFalse(retrievedToken.isPresent());
    }

    @Test
    public void testGetToken_TokenStoreIsNull() {
        JwtService jwtServiceNoStore = new JwtService(jwtConfig);
        when(tokenStoreMock.findById(token)).thenReturn(Optional.empty());

        Optional<Token> retrievedToken = jwtServiceNoStore.getToken(token);

        assertFalse(retrievedToken.isPresent());
    }
}
