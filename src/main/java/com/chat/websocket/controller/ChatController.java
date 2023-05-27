package com.chat.websocket.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.chat.websocket.model.ChatMessage;

@Controller
public class ChatController {

    // @Autowired
    // private final SimpMessagingTemplate template;

    // public ChatController(SimpMessagingTemplate template) {
    //     this.template = template;
    // }

    @MessageMapping("/chat/sendMessage/{roomId}")
    @SendTo("/topic/public/{roomId}")
    public ChatMessage sendMessage(@DestinationVariable String roomId,@Payload ChatMessage chatMessage) {
        String content = chatMessage.getContent();
        String answer = "answer";
        if (answer.equals(content)) {
            chatMessage.setContent("정답입니다");
        }

        //template.convertAndSend("/topic/public", answer, null, null);
        return chatMessage;
    }

    @MessageMapping("/chat/addUser/{roomId}")
    @SendTo("/topic/public/{roomId}")
    public ChatMessage addUser(@DestinationVariable String roomId,@Payload ChatMessage chatMessage, 
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}