package tech.lastbox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import tech.lastbox.controllers.*;

import java.util.HashSet;

@Configuration
public class OAuthConfig {
    private final HashSet<Providers> providers = new HashSet<>();
    private final GenericApplicationContext context;

    @Autowired
    public OAuthConfig(GenericApplicationContext context) {
        this.context = context;
    }

    public void addProvider(Providers provider, String providerOAuthUrl, String callback) {
        providers.add(provider);
    }

    public void build() {
        if (providers.isEmpty()) throw new EmptyProviderList("Can't build without at least one provider.");
        registerOAuthControllers();
    }

    private void registerOAuthControllers() {
        for (Providers provider : providers) {
            OAuthProviderController controller = createController(provider);
            if (controller != null) {
                context.registerBean(provider.name(), OAuthProviderController.class, () -> controller);
            }
        }
    }

    private OAuthProviderController createController(Providers provider) {
        return switch (provider) {
            case FACEBOOK -> new FacebookOAuthController();
            case GOOGLE -> new GoogleOAuthController();
            case GITHUB -> new GithubOAuthController();
            case SPOTIFY -> new SpotifyOAuthController();
            case TWITTER -> new TwitterOAuthController();
            case MICROSOFT -> new MicrosoftOAuthController();
            case LINKEDIN -> new LinkedInOAuthController();
            default -> null;
        };
    }
}
