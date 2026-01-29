package com.yubraj.whatsapp_clone.interceptor;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Security;

/*
synchronize the users in keycloak to our database ..
extend OncePerRequestFilter
this filter runs after BearerTokenAuthenticationFilter
 */
@Component
@RequiredArgsConstructor
public class UserSynchronizerFilter extends OncePerRequestFilter {
    private final UserSynchronizer userSynchronizer;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
                JwtAuthenticationToken token = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

                userSynchronizer.synchronizeWithIdp(token.getToken());
            }
        } finally{
            // passing on the request
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            }

    }
}
