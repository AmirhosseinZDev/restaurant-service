package com.ftgo.restaurant.restaurantservice.config.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.JWSKeySelector; // Correct import
import com.nimbusds.jose.proc.JWSVerificationKeySelector; // Correct import
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
public class JwtDecoderConfig {

    private static final String jwtSecret = "fBsWwVrv0BNVpGIRYAjCeXn8nPCp5nHNfMvQY+PI5H9NE37H6ForsPalQRZi01d7zmfLVxe35UIE1qxwWVZxLw==";

    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "HmacSHA512");

        // 1. Create the JWK Source
        OctetSequenceKey jwk = new OctetSequenceKey.Builder(secretKeySpec.getEncoded())
                .algorithm(JWSAlgorithm.HS512)
                .build();
        ConfigurableJWTProcessor<SecurityContext> jwtProcessor = getSecurityContextConfigurableJWTProcessor(jwk);

        // 4. Skip explicit header verifier for now - rely on KeySelector's implicit Alg check
        // If the 'typ' header continues to cause issues *without* an explicit verifier,
        // the simplest fix might be to remove it during token generation.

        // Optional: Configure standard claim validation (like expiration)
        // The DefaultJWTProcessor usually includes basic checks (exp, nbf) by default.
        // You can add more specific claim verifiers if needed.
        // jwtProcessor.setJWTClaimsSetVerifier(...)

        // 5. Build the decoder with the configured processor
        return new NimbusJwtDecoder(jwtProcessor);
    }

    private static ConfigurableJWTProcessor<SecurityContext> getSecurityContextConfigurableJWTProcessor(OctetSequenceKey jwk) {
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));

        // 2. Configure the JWT Processor
        ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();

        // 3. Set the JWS Key Selector - This is CRUCIAL
        // It ensures the 'alg' header matches HS512 and uses the jwkSource to find the key.
        // This selector is the standard way to link the expected algorithm to the key source.
        JWSKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(
                JWSAlgorithm.HS512, // Expect HS512 algorithm in the header
                jwkSource           // Provide the key source
        );
        jwtProcessor.setJWSKeySelector(keySelector);
        return jwtProcessor;
    }
}