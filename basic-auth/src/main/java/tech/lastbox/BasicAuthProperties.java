package tech.lastbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "lastshield.basicauth")
public class BasicAuthProperties {
    private String issuer;
    private String secretKey;
    private final Logger logger = LoggerFactory.getLogger(BasicAuthProperties.class);

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        if (issuer == null || issuer.isEmpty()) {
            logger.warn("Issuer cannot be null or empty, please set this in properties. \t\\u001B[33mProperty name: 'lastshield.basicauth.issuer'\\u001B[0m");
            throw new InvalidIssuerException("Issuer cannot be null or empty.");
        }
        this.issuer = issuer;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        if (secretKey == null || secretKey.isEmpty()) {
            logger.warn("SecretKey cannot be null or empty, please set this in properties. \t\\u001B[33mProperty name: 'lastshield.basicauth.secret_key'\\u001B[0m");
            throw new InvalidSecretKeyException("Secret Key cannot be null or empty.");
        }
        this.secretKey = secretKey;
    }
}
