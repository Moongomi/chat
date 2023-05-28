package com.chat.websocket.model;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.web.socket.WebSocketSession;

import lombok.Builder;
import lombok.Data;

@Data
public class ChatRoom {

    private static final Set<String> existingRoomIds = new HashSet<>();

    private String roomid;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(String name) {
        this.roomid = generateUniqueRoomid(4);
        this.name = name;
    }

    public String generateRoomid(int length) {
        int leftLimitUpper = 'A';
        int rightLimitUpper = 'Z';
        int leftLimitLower = 'a';
        int rightLimitLower = 'z';
        Random random = new Random();

        return random.ints(length, 0, 2)
            .map(i -> i == 0 ? leftLimitUpper + random.nextInt(rightLimitUpper - leftLimitUpper + 1)
                            : leftLimitLower + random.nextInt(rightLimitLower - leftLimitLower + 1))
            .mapToObj(i -> (char) i)
            .map(String::valueOf)
            .collect(Collectors.joining());
    }

    private String generateUniqueRoomid(int length) {
        String randomString;
        do {
            randomString = generateRoomid(length);
        } while (existingRoomIds.contains(randomString));

        existingRoomIds.add(randomString);

        return randomString;
    }

    public boolean isEmpty(){
        return sessions.isEmpty();
    }

    public int getClientCount() {
        return sessions.size();
    }
}
