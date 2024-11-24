package tech.lastbox.security;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

public class SecurityBannerInitializer implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static final String redColor = "\u001B[31m"; // ANSI escape code for red
    private static final String greenColor = "\u001B[32m"; // ANSI escape code for green
    private static final String resetColor = "\u001B[0m"; // ANSI escape code to reset color
    private static final String bold = "\u001B[1m"; // ANSI escape code for bold

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        SpringApplication application = event.getSpringApplication();

        application.setBanner((environment, sourceClass, out) -> {
            String[] bannerLines = {
                    "███████╗██████╗ ██████╗ ██╗███╗   ██╗ ██████╗",
                    "██╔════╝██╔══██╗██╔══██╗██║████╗  ██║██╔════╝",
                    "███████╗██████╔╝██████╔╝██║██╔██╗ ██║██║  ███╗",
                    "╚════██║██╔═══╝ ██╔══██╗██║██║╚██╗██║██║   ██║",
                    "███████║██║     ██║  ██║██║██║ ╚████║╚██████╔╝",
                    "╚══════╝╚═╝     ╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝ ╚═════╝"
            };
            String firstLine = "with LASTSHIELD SECURITY FRAMEWORK 🔒";
            String secondLine = "by LastBox";

            for (String line : bannerLines) {
                out.println(line);
            }

            out.println(greenColor + bold + firstLine + resetColor);
            out.println(redColor + bold + secondLine + resetColor);
            out.println();
        });
        application.setBannerMode(Banner.Mode.CONSOLE);
    }
}
