package tech.lastbox;

class JwtServiceConfig {
    private static JwtService jwtService;

    public static void configureJwtService(JwtConfig jwtConfig) {
        jwtService = new JwtService(jwtConfig);
    }

    public static JwtService getJwtService() {
        return jwtService;
    }
}
