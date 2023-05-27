package com.chat.websocket.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.web.socket.WebSocketSession;

import lombok.Data;

@Data
public class ChatRoom {

    private String roomid;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();

    public ChatRoom(String name) {
        this.roomid = UUID.randomUUID().toString();
        this.name = name;
    }

    public boolean isEmpty(){
        return sessions.isEmpty();
    }
}
