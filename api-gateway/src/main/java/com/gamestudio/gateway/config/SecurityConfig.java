package com.gamestudio.gateway.config;

import com.gamestudio.gateway.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         AuthenticationWebFilter jwtWebFilter) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/auth/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/users/**").permitAll()
                        .pathMatchers("/api/users/v1/users/by-email/**").authenticated()
                        .pathMatchers("/actuator/**").permitAll()
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .pathMatchers("/api/users/**").hasRole("ADMIN")
                        .pathMatchers("/api/data/**").authenticated()
                        .anyExchange().authenticated()
                )

                .addFilterAt(jwtWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .logout(ServerHttpSecurity.LogoutSpec::disable)
                .build();
    }

    @Bean
    public AuthenticationWebFilter jwtWebFilter(
            ReactiveAuthenticationManager jwtAuthManager,
            ServerAuthenticationConverter jwtConverter
    ) {

        AuthenticationWebFilter filter = new AuthenticationWebFilter(jwtAuthManager);

        filter.setServerAuthenticationConverter(jwtConverter);

        filter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.anyExchange());

        return filter;
    }

    @Bean
    public ReactiveAuthenticationManager jwtAuthManager(JwtService jwtService) {
        return authentication -> {

            String token = authentication.getPrincipal().toString();

            if (!jwtService.isValidToken(token)) {
                return Mono.empty();
            }

            String username = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            List<SimpleGrantedAuthority> authorities =
                    List.of(new SimpleGrantedAuthority("ROLE_" + role));

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    authorities
            );

            return Mono.just(auth);
        };
    }

    @Bean
    public ServerAuthenticationConverter jwtConverter(JwtService jwtService) {
        return exchange -> {
            String auth = exchange.getRequest()
                    .getHeaders()
                    .getFirst(HttpHeaders.AUTHORIZATION);

            if (Objects.isNull(auth) || !auth.startsWith("Bearer ")) {
                return Mono.empty();
            }

            String token = auth.substring(7);

            if (!jwtService.isValidToken(token)) {
                return Mono.empty();
            }

            String username = jwtService.extractUsername(token);

            return Mono.just(
                    new UsernamePasswordAuthenticationToken(token, username)
            );
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "http://localhost:5173"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}