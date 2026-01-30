package com.yubraj.whatsapp_clone.chat;

import com.yubraj.whatsapp_clone.user.User;
import com.yubraj.whatsapp_clone.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public ChatService(ChatRepository chatRepository, UserRepository userRepository) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    public Optional<Chat> findChatBySenderAndRecipientId(String senderId,String recipientId){

        return chatRepository.findBySenderAndRecipient(senderId,recipientId);

    }

    public Chat createChat(String senderId, String recipientId){

        //finding chat if present
        Optional<Chat> existingChat = findChatBySenderAndRecipientId(senderId,recipientId);
        if(existingChat.isPresent()){
            return existingChat.get();
        }

        User senderUser = userRepository.findById(senderId).orElseThrow(()->new EntityNotFoundException("sender not found.."));
        User recipientUser = userRepository.findById(recipientId).orElseThrow(()-> new EntityNotFoundException("recipient not found.."));
        Chat newChat = Chat.builder()
                .sender(senderUser)
                .recipient(recipientUser)
                .build();

        return chatRepository.save(newChat);
    }
}
