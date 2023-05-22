package com.chat.websocket.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import com.chat.websocket.model.ChatMessage;

//@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
public class ChatControllerTest {

    @Mock
    private SimpMessageHeaderAccessor headerAccessor;

    @InjectMocks
    private ChatController chatController;

    @Test
    public void testSendMessage() {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender("John");
        chatMessage.setContent("Hello World");

        ChatMessage result = chatController.sendMessage(chatMessage);

        assertEquals(chatMessage, result);
    }

    @Test
    public void testAddUser() {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender("John");
        chatMessage.setContent("Hello World");

        when(headerAccessor.getSessionAttributes()).thenReturn(new HashMap<>());

        ChatMessage result = chatController.addUser(chatMessage, headerAccessor);

        assertEquals(chatMessage, result);
        assertEquals("John", headerAccessor.getSessionAttributes().get("username"));
    }
}