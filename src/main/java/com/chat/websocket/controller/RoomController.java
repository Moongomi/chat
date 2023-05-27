package com.chat.websocket.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chat.websocket.model.ChatRoom;
import com.chat.websocket.repository.ChatRoomRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/chat")
@Log4j2
public class RoomController {

    private final ChatRoomRepository repository;

    @GetMapping(value = "/rooms")
    public List<ChatRoom> rooms(){
        log.info("# All Chat Rooms");
        return repository.findAllRooms();
    }

    @PostMapping(value = "/room")
    public ResponseEntity<ChatRoom> create(@RequestParam String name){
        log.info("# Create Chat Room , name: " + name);
        ChatRoom newRoom = repository.createChatRoom(name);
        return ResponseEntity.ok(newRoom);
    }
    
}
