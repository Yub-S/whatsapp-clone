package com.yubraj.whatsapp_clone.user;


import java.util.List;

public class UserMapper {

    public static UserResponse toUserResponse(User user){

       return UserResponse.builder()
                .firstName(user.getFirstName())
                .id(user.getId())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .Online(user.isUserOnline())
                .lastLogin(user.getLastSeen())
                .build();
    }
}
