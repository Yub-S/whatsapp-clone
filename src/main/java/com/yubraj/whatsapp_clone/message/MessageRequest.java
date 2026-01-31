package com.yubraj.whatsapp_clone.message;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MessageRequest {

    private String content;
    private String senderId;
    private String recipientId;
    private String chatId;
    private MessageType type;
}
