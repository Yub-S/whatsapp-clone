package com.yubraj.whatsapp_clone.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {

    //finding by email
    Optional<User> findByEmail (String email);

    //finding all users except the self
    @Query("select u from User u where u.id != :theId ")
    List<User> findAllExceptSelf(@Param("theId") String theId);


}
