package com.yubraj.whatsapp_clone.message;

import com.yubraj.whatsapp_clone.NotificationService.Notification;
import com.yubraj.whatsapp_clone.NotificationService.NotificationService;
import com.yubraj.whatsapp_clone.NotificationService.NotificationType;
import com.yubraj.whatsapp_clone.chat.Chat;
import com.yubraj.whatsapp_clone.chat.ChatRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import org.springframework.security.access.AccessDeniedException;

@Service
public class MessageService {


    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final FileService fileService;
    private final NotificationService notificationService;

    public MessageService(ChatRepository chatRepository, MessageRepository messageRepository, FileService fileService, NotificationService notificationService) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.fileService = fileService;
        this.notificationService = notificationService;
    }

    public void saveMessage(MessageRequest messageRequest){
        //find chat id
       Chat chat =  chatRepository.findById(messageRequest.getChatId()).orElseThrow(()->new EntityNotFoundException("no chat..."));

       Message message = new Message();
       message.setChat(chat);
       message.setContent(messageRequest.getContent());
       message.setType(messageRequest.getType());
       message.setSenderId(messageRequest.getSenderId());
       message.setReceiverId(messageRequest.getRecipientId());
       message.setState(MessageState.SENT);

       messageRepository.save(message);

       // todo notification
        Notification notification = Notification.builder()
                .chatId(chat.getId())
                .messageType(messageRequest.getType())
                .content(messageRequest.getContent())
                .senderId(messageRequest.getSenderId())
                .receiverId(messageRequest.getRecipientId())
                .type(NotificationType.MESSAGE)
                .build();

        notificationService.sendNotification(message.getReceiverId(),notification);




    }

    public List<MessageResponse> findChatMessages(String chatId){
      List<Message> messages =  messageRepository.findByChat_Id(chatId);

      return messages.stream()
              .map(MessageMapper::toMessageResponse)
              .toList();
    }

    @Transactional
    public void setMessagesToSeen(String chatId, Authentication authentication){
        //get the chat
        Chat chat = chatRepository.findById(chatId).orElseThrow(()->new EntityNotFoundException("no entity here"));

        // get the other person
        String otherPerson = getRecipientId(chat,authentication);

        messageRepository.setMessageStatus(MessageState.SEEN,chat.getId(),otherPerson);

        //todo notification

        Notification notification = Notification.builder()
                .chatId(chat.getId())
                .senderId(getSenderId(chat,authentication))
                .receiverId(otherPerson)
                .type(NotificationType.SEEN)
                .build();

        notificationService.sendNotification(otherPerson,notification);
    }

    public void uploadMediaMessage(String chatId, MultipartFile file, Authentication authentication){
        Chat chat = chatRepository.findById(chatId).orElseThrow(()->new EntityNotFoundException("no entity here"));

        //get the sender (validation as well)
        final String senderId = getSenderId(chat,authentication);
        final String recipientId = getRecipientId(chat,authentication);

        final String filePath = fileService.saveFile(file,senderId);

        Message message = new Message();
        message.setChat(chat);
        message.setType(MessageType.IMAGE);
        message.setMediaFilePath(filePath);
        message.setSenderId(senderId);
        message.setReceiverId(recipientId);
        message.setState(MessageState.SENT);

        // todo notification

        Notification notification = Notification.builder()
                .chatId(chat.getId())
                .messageType(MessageType.IMAGE)
                .senderId(senderId)
                .receiverId(recipientId)
                .type(NotificationType.IMAGE)
                .media(FileUtils.readFileFromLocation(filePath))
                .build();

        notificationService.sendNotification(recipientId,notification);

    }

    private String getSenderId(Chat chat, Authentication authentication) {
        // checking if the current user is inside the chat
        if ((authentication.getName().equals(chat.getSender().getId())) || (authentication.getName().equals(chat.getRecipient().getId()))){
            return authentication.getName();
        }
        throw new AccessDeniedException("not authorized to send message..");
    }

    private String getRecipientId(Chat chat , Authentication authentication) {
        if (chat.getSender().getId().equals(authentication.getName())){
            return chat.getRecipient().getId();
        }
        return chat.getSender().getId();
    }
}
