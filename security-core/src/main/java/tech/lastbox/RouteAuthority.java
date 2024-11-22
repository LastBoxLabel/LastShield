package tech.lastbox;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public record RouteAuthority(String path, SimpleGrantedAuthority simpleGrantedAuthority) {}
