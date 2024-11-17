package tech.lastbox;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;

/**
 * Entity class representing a token in the system.
 * This class is mapped to a database table for storing token-related information,
 * such as token string, issue time, expiration time, subject, and revocation status.
 *<p>
 * The entity is typically used in conjunction with a repository or data access layer
 * for persistence and retrieval of token data.
 */
@Entity
public class TokenEntity {

    @Id
    private String token;

    private Instant issuedAt;
    private Instant expiresIn;
    private String subject;
    private String issuer;
    @ElementCollection
    private List<String> scope;
    private boolean isRevoked;

    /**
     * Default constructor.
     * Used by JPA for entity instantiation.
     */
    public TokenEntity() {}

    /**
     * Constructor to initialize a token entity with specified values.
     *
     * @param token the token string.
     * @param issuedAt the issue timestamp of the token.
     * @param expiresIn the expiration timestamp of the token.
     * @param subject the subject associated with the token.
     */
    public TokenEntity(String token, Instant issuedAt, Instant expiresIn, String subject, String issuer, List<String> scope) {
        this.token = token;
        this.issuedAt = issuedAt;
        this.expiresIn = expiresIn;
        this.subject = subject;
        this.issuer = issuer;
        this.scope = scope;
    }

    /**
     * Gets the token string.
     *
     * @return the token string.
     */
    public String getToken() {
        return token;
    }

    /**
     * Gets the issued time of the token.
     *
     * @return the timestamp when the token was issued.
     */
    public Instant getIssuedAt() {
        return issuedAt;
    }

    /**
     * Gets the expiration time of the token.
     *
     * @return the timestamp when the token expires.
     */
    public Instant getExpiresIn() {
        return expiresIn;
    }

    /**
     * Gets the subject associated with the token.
     *
     * @return the subject (typically the user or entity) associated with the token.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Checks if the token is revoked.
     *
     * @return true if the token is revoked, false otherwise.
     */
    public boolean isRevoked() {
        return isRevoked;
    }

    /**
     * Sets the revocation status of the token.
     *
     * @param isRevoked the new revocation status.
     */
    public void setRevoked(boolean isRevoked) {
        this.isRevoked = isRevoked;
    }

    /**
     * Retrieves the issuer of the token.
     *
     * @return The issuer (e.g., system or service that generated the token).
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * Retrieves the scope associated with the token.
     * <p>
     * The scope defines the permissions or roles that the token grants to the subject.
     *
     * @return A list of scope strings.
     */
    public List<String> getScope() {
        return scope;
    }

    /**
     * Checks if the token is expired.
     * A token is considered expired if its expiration time is null or before the current time.
     *
     * @return true if the token is expired, false otherwise.
     */
    public boolean isExpired() {
        return expiresIn == null || expiresIn.isBefore(Instant.now());
    }

    /**
     * Checks if the token is valid.
     * A token is valid if it is neither expired nor revoked.
     *
     * @return true if the token is valid, false otherwise.
     */
    public boolean isValid() {
        return !isExpired() && !isRevoked;
    }
}
