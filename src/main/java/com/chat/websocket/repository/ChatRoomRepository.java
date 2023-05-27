package com.chat.websocket.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Repository;

import com.chat.websocket.model.ChatRoom;

import jakarta.annotation.PostConstruct;

@Repository
public class ChatRoomRepository {

    private AtomicInteger roomIdCounter = new AtomicInteger(0);

    private Map<String, ChatRoom> chatRooms;

    @PostConstruct
    private void init(){
        chatRooms = new LinkedHashMap<>();
    }

    public List<ChatRoom> findAllRooms(){
        //채팅방 생성 순서 최근 순으로 반환
        List<ChatRoom> result = new ArrayList<>(chatRooms.values());
        Collections.reverse(result);

        return result;
    }

    public ChatRoom findRoomById(String roomid){
        return chatRooms.get(roomid);
    }

    public ChatRoom createChatRoom(String name){
        ChatRoom room = new ChatRoom(name);
        chatRooms.put(room.getRoomid(), room);

        return room;
    }

    public int getSessionCount(String roomid){
        ChatRoom room = findRoomById(roomid);
        return room.getClientCount();
    }

    public boolean isChatRoomNull(ChatRoom chatRoom){
        return chatRoom == null;
    }
}