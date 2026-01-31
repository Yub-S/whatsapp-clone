package com.yubraj.whatsapp_clone.message;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageResponse {

    private Long id;
    private String content;
    private String senderId;
    private String recipientId;
    private MessageState state;
    private MessageType type;
    private LocalDateTime createdAt;
    private byte[] media;
}
