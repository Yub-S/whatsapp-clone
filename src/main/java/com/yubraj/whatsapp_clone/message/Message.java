package com.yubraj.whatsapp_clone.message;

import com.yubraj.whatsapp_clone.chat.Chat;
import com.yubraj.whatsapp_clone.common.BaseAuditingEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="message")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message extends BaseAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "msg_sequence")
    @SequenceGenerator(name="msg_sequence",sequenceName = "msg_sequence",allocationSize = 5)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    @Enumerated(EnumType.STRING)
    private MessageState state;

    private String senderId;
    private String receiverId;

    @ManyToOne()
    @JoinColumn(name="chat_id")
    private Chat chat;

    private String mediaFilePath;

}
