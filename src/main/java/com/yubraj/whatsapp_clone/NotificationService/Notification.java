package com.yubraj.whatsapp_clone.NotificationService;


import com.yubraj.whatsapp_clone.message.MessageType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {

    private String chatId;
    private String content;
    private String senderId;
    private String receiverId;
    private MessageType messageType;
    private NotificationType type;
    private byte[] media;

}
