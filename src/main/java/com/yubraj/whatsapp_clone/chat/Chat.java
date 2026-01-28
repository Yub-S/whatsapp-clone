package com.yubraj.whatsapp_clone.chat;

import com.yubraj.whatsapp_clone.common.BaseAuditingEntity;
import com.yubraj.whatsapp_clone.message.Message;
import com.yubraj.whatsapp_clone.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="chat")
public class Chat extends BaseAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="sender_id")
    private User sender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="receiver_id")
    private User recipient;

    @OneToMany(mappedBy = "chat", fetch = FetchType.LAZY)
    @OrderBy("createDate DESC ")
    private List<Message> messages;
}
