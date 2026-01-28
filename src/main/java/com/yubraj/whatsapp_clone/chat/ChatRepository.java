package com.yubraj.whatsapp_clone.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat,String> {

    // finding chat by sender
    @Query ("select c from Chat c where c.sender.id = :senderId or c.recipient.id = :senderId")
    List<Chat> findBySender(@Param("senderId")String senderId);
    /* hibernate can actually handle this automatcially as well --
    findBySender_IdOrRecipient_Id(String senderId, String senderId) -- must pass both times */


    //finding by sender and recipient
    @Query("SELECT c FROM Chat c WHERE (c.sender.id = :senderId AND c.recipient.id = :recipientId) OR (c.sender.id = :recipientId AND c.recipient.id = :senderId)")
    Optional<Chat> findBySenderAndRecipient(@Param("senderId") String senderId, @Param("recipientId") String recipientId);
    /* findBySender_IdAndRecipient_IdOrSender_IdAndRecipient_Id(String senderId,String recipientId, String recipientId, String senderId);
    * looks messy so we choose query method instead*/


}
