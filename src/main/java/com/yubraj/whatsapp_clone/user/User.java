package com.yubraj.whatsapp_clone.user;

import com.yubraj.whatsapp_clone.chat.Chat;
import com.yubraj.whatsapp_clone.common.BaseAuditingEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="users")
public class User extends BaseAuditingEntity {
    public static final int LAST_ACTIVE_INTERVAL=5;
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime lastSeen;

    @OneToMany(mappedBy = "sender")
    private List<Chat> chatsAsSender;

    @OneToMany(mappedBy = "recipient")
    private List<Chat> chatsAsReceiver;

    public boolean isUserOnline(){
        return lastSeen!=null && lastSeen.isAfter(LocalDateTime.now().minusMinutes(LAST_ACTIVE_INTERVAL));
    }

}
