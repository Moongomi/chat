"use strict";

var usernamePage = document.querySelector("#username-page");
var roomnamePage = document.querySelector("#roomname-page");
var chatPage = document.querySelector("#chat-page");
var usernameForm = document.querySelector("#usernameForm");
var messageForm = document.querySelector("#messageForm");
var messageInput = document.querySelector("#message");
var messageArea = document.querySelector("#messageArea");
var connectingElement = document.querySelector(".connecting");

var stompClient = null;
var username = null;
var roomname = null;

var colors = [
  "#2196F3",
  "#32c787",
  "#00BCD4",
  "#ff5652",
  "#ffc107",
  "#ff85af",
  "#FF9800",
  "#39bbb0",
];

function connect(event) {
  username = document.querySelector("#name").value.trim();

  if (username) {
    usernamePage.classList.add("hidden");
    roomnamePage.classList.remove("hidden");

    //id가 room-crate, room-list , room-enter 로 이루어진 버튼 별로 액션을 취하려고함

    //chatPage.classList.remove('hidden');
    /*
    var socket = new SockJS("/ws");
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);
  }
  */
  }
  event.preventDefault();
}

async function createRoom(event) {
  event.preventDefault();
  // Room creation logic goes here
  roomname = document.querySelector("#rname").value.trim();
  console.log("Roomname : ",roomname);

  if (roomname) {
    try {
      const response = await fetch(`/chat/room?name=${roomname}`, {
        method: "POST", // or 'PUT'
        headers: {
          "Content-Type": "application/json",
        },
      });

      if (response.ok) {
        roomnamePage.classList.add("hidden");
        chatPage.classList.remove("hidden");
        console.log("Room created successfully.");
        console.log(response);

        var socket = new SockJS("/ws");
        stompClient = Stomp.over(socket);

        //stompClient.connect({}, onConnected(roomname), onError);
        stompClient.connect({}, () => onConnected(roomname), onError);


        // Perform any required actions upon successful room creation
      } else {
        console.error("Error creating room: ", response.statusText);
        // Handle any errors during room creation
      }
    } catch (error) {
      console.error("Error creating room: ", error);
      // Handle any errors during room creation
    }
  }
  
}

function listRooms() {
  // List rooms logic goes here
  console.log("List rooms button clicked");
}

function enterRoom() {
  // Enter room logic goes here
  console.log("Enter room button clicked");
}

function onConnected(roomname) {
  // Subscribe to the Public Topic
  //stompClient.subscribe("/topic/public", onMessageReceived);
  stompClient.subscribe("/topic/public/" + roomname, onMessageReceived);

  // Tell your username to the server
  stompClient.send(
    "/app/chat/addUser/"+roomname,
    {},
    JSON.stringify({ sender: username, type: "JOIN" })
  );

  connectingElement.classList.add("hidden");
}

function onError(error) {
  connectingElement.textContent =
    "Could not connect to WebSocket server. Please refresh this page to try again!";
  connectingElement.style.color = "red";
}

function sendMessage(event, roomname) {
  var messageContent = messageInput.value.trim();
  if (messageContent && stompClient) {
    var chatMessage = {
      sender: username,
      content: messageInput.value,
      type: "CHAT",
    };
    stompClient.send(
      "/app/chat/sendMessage/"+roomname,
      {},
      JSON.stringify(chatMessage)
    );
    messageInput.value = "";
  }
  event.preventDefault();
}

function onMessageReceived(payload) {
  var message = JSON.parse(payload.body);

  var messageElement = document.createElement("li");

  if (message.type === "JOIN") {
    messageElement.classList.add("event-message");
    message.content = message.sender + " joined!";
  } else if (message.type === "LEAVE") {
    messageElement.classList.add("event-message");
    message.content = message.sender + " left!";
  } else {
    messageElement.classList.add("chat-message");

    var avatarElement = document.createElement("i");
    var avatarText = document.createTextNode(message.sender[0]);
    avatarElement.appendChild(avatarText);
    avatarElement.style["background-color"] = getAvatarColor(message.sender);

    messageElement.appendChild(avatarElement);

    var usernameElement = document.createElement("span");
    var usernameText = document.createTextNode(message.sender);
    usernameElement.appendChild(usernameText);
    messageElement.appendChild(usernameElement);
  }

  var textElement = document.createElement("p");
  var messageText = document.createTextNode(message.content);
  textElement.appendChild(messageText);

  messageElement.appendChild(textElement);

  messageArea.appendChild(messageElement);
  messageArea.scrollTop = messageArea.scrollHeight;
}

function getAvatarColor(messageSender) {
  var hash = 0;
  for (var i = 0; i < messageSender.length; i++) {
    hash = 31 * hash + messageSender.charCodeAt(i);
  }
  var index = Math.abs(hash % colors.length);
  return colors[index];
}

usernameForm.addEventListener("submit", connect, true);
//messageForm.addEventListener("submit", sendMessage, true);
messageForm.addEventListener(
  "submit",
  (event) => sendMessage(event, roomname),
  true
);

// Room-specific buttons
document.querySelector("#room-create").addEventListener("click", createRoom);
document.querySelector("#room-list").addEventListener("click", listRooms);
document.querySelector("#room-enter").addEventListener("click", enterRoom);
