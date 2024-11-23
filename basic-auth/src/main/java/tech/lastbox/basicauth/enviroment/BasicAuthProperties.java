package tech.lastbox.basicauth.enviroment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * This class holds the configuration properties for Basic Authentication.
 * The properties are loaded from the application's configuration (e.g., `application.yml` or `application.properties`)
 * and are conditionally enabled when the `lastshield.basicauth` property is set to `true`.
 * <p>
 * It validates the issuer and secret key properties and throws custom exceptions if they are null or empty.
 * </p>
 */
@Component
@ConfigurationProperties(prefix = "lastshield.basicauth")
@ConditionalOnProperty(name = "lastshield.basicauth", havingValue = "true")
@Schema(description = "Configuration properties for Basic Authentication in the application.")
public class BasicAuthProperties {

    @Schema(description = "Issuer of the token, typically the system generating the token.", example = "MyAuthSystem")
    private String issuer;

    @Schema(description = "Secret key used for signing and verifying tokens.", example = "SuperSecretKey12345")
    private String secretKey;

    @Schema(description = "Name of the administrator user.", example = "Admin")
    private String adminName = "Admin";

    @Schema(description = "Username of the administrator user.", example = "admin")
    private String adminUsername = "admin";

    @Schema(description = "Password of the administrator user.", example = "admin")
    private String adminPassword = "admin";

    private final Logger logger = LoggerFactory.getLogger(BasicAuthProperties.class);

    /**
     * Gets the issuer for the token.
     *
     * @return the issuer of the token.
     */
    @Schema(description = "Gets the issuer of the token.")
    public String getIssuer() {
        return issuer;
    }

    /**
     * Sets the issuer for the token.
     * <p>
     * Throws an exception if the issuer is null or empty.
     * </p>
     *
     * @param issuer the issuer of the token.
     * @throws InvalidIssuerException if the issuer is invalid.
     */
    @Schema(description = "Sets the issuer for the token.")
    public void setIssuer(String issuer) {
        if (issuer == null || issuer.isEmpty()) {
            logger.warn("Issuer cannot be null or empty, please set this in properties. \t\\u001B[33mProperty name: 'lastshield.basicauth.issuer'\\u001B[0m");
            throw new InvalidIssuerException("Issuer cannot be null or empty.");
        }
        this.issuer = issuer;
    }

    /**
     * Gets the secret key for token signing and verification.
     *
     * @return the secret key.
     */
    @Schema(description = "Gets the secret key used for signing and verifying tokens.")
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * Sets the secret key for token signing and verification.
     * <p>
     * Throws an exception if the secret key is null or empty.
     * </p>
     *
     * @param secretKey the secret key.
     * @throws InvalidSecretKeyException if the secret key is invalid.
     */
    @Schema(description = "Sets the secret key used for signing and verifying tokens.")
    public void setSecretKey(String secretKey) {
        if (secretKey == null || secretKey.isEmpty()) {
            logger.warn("SecretKey cannot be null or empty, please set this in properties. \t\\u001B[33mProperty name: 'lastshield.basicauth.secret_key'\\u001B[0m");
            throw new InvalidSecretKeyException("Secret Key cannot be null or empty.");
        }
        this.secretKey = secretKey;
    }

    /**
     * Gets the administrator's username.
     *
     * @return the admin username.
     */
    @Schema(description = "Gets the administrator's username.")
    public String getAdminUsername() {
        return adminUsername;
    }

    /**
     * Sets the administrator's username.
     *
     * @param adminUsername the admin username.
     */
    @Schema(description = "Sets the administrator's username.")
    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    /**
     * Gets the administrator's password.
     *
     * @return the admin password.
     */
    @Schema(description = "Gets the administrator's password.")
    public String getAdminPassword() {
        return adminPassword;
    }

    /**
     * Sets the administrator's password.
     *
     * @param adminPassword the admin password.
     */
    @Schema(description = "Sets the administrator's password.")
    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    /**
     * Gets the administrator's name.
     *
     * @return the admin name.
     */
    @Schema(description = "Gets the administrator's name.")
    public String getAdminName() {
        return adminName;
    }

    /**
     * Sets the administrator's name.
     *
     * @param adminName the admin name.
     */
    @Schema(description = "Sets the administrator's name.")
    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }
}
