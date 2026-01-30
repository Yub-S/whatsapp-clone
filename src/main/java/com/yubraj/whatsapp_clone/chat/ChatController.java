package com.yubraj.whatsapp_clone.chat;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/chats")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public ResponseEntity<Chat> getChat(Authentication auth, @RequestParam("recipientId") String recipientId){
        //get the current user id
        String actionPerformingUserId = auth.getName();

        //find chats
        Chat chat = chatService.findChatBySenderAndRecipientId(actionPerformingUserId,recipientId).orElseThrow(()->new EntityNotFoundException("no sender.."));
        return ResponseEntity.ok(chat);
    }

    @PostMapping
    public ResponseEntity<Chat> createChat(Authentication auth , @RequestParam("recipientId") String recipientId){
        //get the sender
        String actionPerformingUserId = auth.getName();

        //create chat
       Chat chat = chatService.createChat(actionPerformingUserId,recipientId);
       return ResponseEntity.ok(chat);

    }
}
