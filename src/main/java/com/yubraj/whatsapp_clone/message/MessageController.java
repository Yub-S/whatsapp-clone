package com.yubraj.whatsapp_clone.message;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // saving a message
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void saveMessage(@RequestBody MessageRequest messageRequest){
        messageService.saveMessage(messageRequest);
    }

   @PostMapping(value="/media-upload",consumes = "multipart/form-data")
   @ResponseStatus(HttpStatus.ACCEPTED)
   public void uploadMedia(@RequestParam("chat-id") String chatId, @RequestParam("file") MultipartFile file ,
                           Authentication authentication){
        messageService.uploadMediaMessage(chatId,file,authentication);
   }

    @PatchMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void setMessageToSeen(@RequestParam("chat-id") String chatId,Authentication authentication){
        messageService.setMessagesToSeen(chatId,authentication);
    }

    @GetMapping("/chat/{chat-id}")
    public ResponseEntity<List<MessageResponse>> getMessage(@PathVariable("chat-id") String chatId){
        return ResponseEntity.ok(messageService.findChatMessages(chatId));

    }
}
