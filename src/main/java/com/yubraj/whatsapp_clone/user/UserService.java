package com.yubraj.whatsapp_clone.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponse> getAllUsersExceptSelf(String userId){
      return userRepository.findAllExceptSelf(userId).stream().map(UserMapper::toUserResponse).toList();
   }

}
