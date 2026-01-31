package com.yubraj.whatsapp_clone.message;

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

    public MessageService(ChatRepository chatRepository, MessageRepository messageRepository, FileService fileService) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.fileService = fileService;
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
