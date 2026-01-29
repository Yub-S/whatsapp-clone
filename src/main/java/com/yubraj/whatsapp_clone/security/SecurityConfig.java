package com.yubraj.whatsapp_clone.security;

import com.yubraj.whatsapp_clone.interceptor.UserSynchronizerFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserSynchronizerFilter userSynchronizerFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())  // it will look for bean of CorsFilter that we implement below
                .csrf(AbstractHttpConfigurer::disable) // we will use oauth2 token not session id
                .authorizeHttpRequests(req->
                {
                    // permit all of these
                    req.requestMatchers("/v3/api-docs","/v3/api-docs/**","/swagger-resources",
                            "/swagger-resources/**","/configuration/ui","/configuration/security",
                            "/swagger-ui/**","/webjars/**","/swagger-ui.html","/ws/**").permitAll();
                    req.anyRequest().authenticated();
                })
                // anything else must be authenticated by our ResourceServer
                .oauth2ResourceServer(auth->auth.jwt(
                        token->token.jwtAuthenticationConverter(new KeyCloakJwtAuthenticationConverter())
                ))
                .addFilterAfter(userSynchronizerFilter, BearerTokenAuthenticationFilter.class); // our custom filter

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();


        config.setAllowCredentials(true);

        config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));

        config.setAllowedHeaders(Arrays.asList(
                HttpHeaders.ORIGIN,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT,
                HttpHeaders.AUTHORIZATION
        ));
        config.setAllowedMethods(Arrays.asList(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "PATCH"
        ));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}