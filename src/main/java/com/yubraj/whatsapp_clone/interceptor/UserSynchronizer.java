package com.yubraj.whatsapp_clone.interceptor;

import com.yubraj.whatsapp_clone.user.User;
import com.yubraj.whatsapp_clone.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class UserSynchronizer {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /* token (of Jwt class ) is the actual object we are looking for
    that contains the user details (inside claims) and standard header info and signature part*/

    public void synchronizeWithIdp(Jwt token) {
        System.out.println("synchronizing user with idp");

        getUserEmail(token).ifPresent(userEmail -> {
            System.out.println("synchronizing user with email " + userEmail);
            // Optional<User> user =   userRepository.findById(userEmail);
            User user = userMapper.fromTokenAttributes(token.getClaims());
            userRepository.save(user);
        });

    }

    private Optional<String> getUserEmail(Jwt token) {
        Map<String, Object> attributes = token.getClaims();

        return Optional.ofNullable(attributes.get("email").toString());

    }

}
