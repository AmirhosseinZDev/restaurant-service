package com.ftgo.restaurant.restaurantservice.config.security;

import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final String principalClaimName = "sub"; // Or whatever claim contains the user identifier
    private static final String authorityClaimName = "auth"; // <<< Change this if you used a different claim name

    @Override
    public AbstractAuthenticationToken convert(@Nonnull Jwt jwt) {
        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);
        return new JwtAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt));
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        Map<String, Object> claims = jwt.getClaims();
        Object authorities = claims.get(authorityClaimName);

        if (authorities instanceof List<?>) {
            return ((List<String>) authorities).stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
        return List.of();

    }

    private String getPrincipalClaimName(Jwt jwt) {
        String claimName = principalClaimName;
        if (claimName == null) {
            claimName = jwt.getSubject();
        }
        return claimName;
    }
}
