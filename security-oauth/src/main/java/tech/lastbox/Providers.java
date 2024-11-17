package tech.lastbox;

public enum Providers {
    FACEBOOK("https://facebook.com/oauth", "/callback/facebook"),
    GITHUB("https://github.com/login/oauth", "/callback/github"),
    GOOGLE("https://accounts.google.com/o/oauth2/auth", "/callback/google"),
    LINKEDIN("https://www.linkedin.com/oauth/v2/authorization", "/callback/linkedin"),
    MICROSOFT("https://login.microsoftonline.com/oauth2/v2.0/authorize", "/callback/microsoft"),
    SPOTIFY("https://accounts.spotify.com/authorize", "/callback/spotify"),
    TWITTER("https://api.twitter.com/oauth", "/callback/twitter");

    private String providerOAuthUrl;
    private String callbackMapping;

    Providers(String providerOAuthUrl, String callbackMapping) {
        this.providerOAuthUrl = providerOAuthUrl;
        this.callbackMapping = callbackMapping;
    }

    public String providerOAuthUrl() {
        return providerOAuthUrl;
    }

    public void setProviderOAuthUrl(String providerOAuthUrl) {
        this.providerOAuthUrl = providerOAuthUrl;
    }

    public String callbackMapping() {
        return callbackMapping;
    }

    public void setCallbackMapping(String callbackMapping) {
        this.callbackMapping = callbackMapping;
    }
}