package com.chat.websocket.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessage {

    private MessageType type;
    private String content;
    private String sender;
    private int roomid;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }

    @Builder
    public ChatMessage(MessageType type, String content, String sender,int roomid) {
        this.type = type;
        this.content = content;
        this.sender = sender;
        this.roomid = roomid;
    }
}