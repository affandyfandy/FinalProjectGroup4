package com.group4.gateway.config;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.group4.gateway.exceptions.JwtDecodingException;

@Configuration
public class GatewaySecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(GatewaySecurityConfig.class);

    @Value("classpath:certs/public-key.pem")
    private Resource publicKeyResource;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws JwtDecodingException {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange((var exchange) -> exchange
                    .pathMatchers("/api/v1/**").permitAll()  // Public API
                    .anyExchange().authenticated()           // Secured routes
                )
                .build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() throws JwtDecodingException {
        try {
            RSAPublicKey publicKey = loadPublicKey();
            return NimbusReactiveJwtDecoder.withPublicKey(publicKey).build();
        } catch (JwtDecodingException e) {
            throw new JwtDecodingException("Error decoding JWT", e);
        }
    }

    private RSAPublicKey loadPublicKey() throws JwtDecodingException {
        try {
            byte[] keyBytes = publicKeyResource.getInputStream().readAllBytes();
            String publicKeyContent = new String(keyBytes)
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s+", "");
            log.info("Public key content: {}", publicKeyContent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new JwtDecodingException("Failed to load the public key", e);
        }
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(List.of("http://localhost:3000"));  // Define allowed origins
        corsConfig.setAllowedMethods(List.of("PUT", "GET", "POST", "DELETE"));
        corsConfig.setAllowedHeaders(List.of("Content-Type", "Authorization", "api-key"));
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);  // Apply CORS globally

        return new CorsWebFilter(source);
    }
}