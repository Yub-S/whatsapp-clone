package com.yubraj.whatsapp_clone.message;

import com.yubraj.whatsapp_clone.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Long> {

    // find Messages by chatID
    /* inefficient - first finding Chat object -> though hibernate can handle it (use the id) */
    List<Message> findByChat(Chat chat);

    /* hibernate will find the chat field, and then _ tells it to look inside the Chat and find the Id field*/
    List<Message> findByChat_Id (String id);
    /* the argument can be named anything , for this hibernate will look for the position inside the query
    arguments must be passed x times if there is x parameter placeholder inside the query even if the value would be same */

    // setting message state to seen
    // modifying annotation -> usually hibernate thinks it's a select query
    @Modifying
    @Query("update Message m set m.state = :state where m.id = :id")
    void setMessageStatus(@Param("state") MessageState state, @Param("id") Long id);
    /* we can do this in service layer as well -> by fetching and then using setter and transactional annotation
    as the whole concept of orm is to work with objects and not write sql queries....
    -- however must prefer this in case of bulk operation and efficiency as we just need 1 db call*/
}
