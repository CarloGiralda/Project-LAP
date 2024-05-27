'use strict';
document.addEventListener("DOMContentLoaded", function () {
    // Check if the user is authenticated based on the presence of the HttpOnly cookie
    const isAuthenticated = document.cookie.includes('jwtToken')
    const usernameDisplay = document.getElementById('usernameDisplay')
    const userDropDown = document.getElementById('userDropDown')

    console.log("user is authenticated:", isAuthenticated)

    if (isAuthenticated && usernameDisplay) {

        // read name from session storage and show to the user
        usernameDisplay.textContent = sessionStorage.getItem("username")

        // show user dropdown
        userDropDown.style.display = 'block'
    }

});

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
//var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');
var sender = document.querySelector('#sender').textContent;
var receiver = document.querySelector('#receiver').textContent;

var stompClient = null;
var username = null;
var sessionId = "";

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {

    username = sender;
    if(username) {
       var socket = new SockJS('http://localhost:8080/secured/room');//,null,{transports: sockJsProtocols});
       stompClient = Stomp.over(socket);
       stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();

}


function onConnected() {
console.log("ei");

    var url = stompClient.ws._transport.url;
            url = url.replace(
              "ws://localhost:8080/secured/room/",  "");
            url = url.replace("/websocket", "");
            url = url.replace(/^[0-9]+\//, "");
            sessionId = url;
            stompClient.subscribe('/secured/user/queue/specific-user'
              + '-user' + sessionId, onMessageReceived);

    connectingElement.classList.add('hidden');
    fetchAndDisplayUserChat();
}


function onError(error) {
    connectingElement.textContent = 'Unable to connect to WebSocket! Refresh the page and try again, or contact your administrator.';
    connectingElement.style.color = 'red';
}


function send(event) {
    var messageContent = messageInput.value.trim();

    if(messageContent && stompClient) {
        var chatMessage = {
            sender: sender,
            sessionId: sessionId,
            receiver: receiver,
            content: messageInput.value,
            type: 'CHAT'
        };

        stompClient.send('/spring-security-mvc-socket/secured/room', {}, JSON.stringify(chatMessage));

        messageInput.value = '';

    }
    event.preventDefault();
}




function onMessageReceived(payload) {
 console.log("-----------here"+payload);
    var message = JSON.parse(payload.body);

    var messageElement = document.createElement('li');

    if(message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
    } else {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

function onMessagesReceived(payload) {
  //  var message = JSON.parse(payload);
  var message = payload;

    var messageElement = document.createElement('li');


    messageElement.classList.add('chat-message');

    var avatarElement = document.createElement('i');
    var avatarText = document.createTextNode(message.sender[0]);
    avatarElement.appendChild(avatarText);
    avatarElement.style['background-color'] = getAvatarColor(message.sender);

    messageElement.appendChild(avatarElement);

    var usernameElement = document.createElement('span');
    var usernameText = document.createTextNode(message.sender);
    usernameElement.appendChild(usernameText);
    messageElement.appendChild(usernameElement);


    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

function getCookie(cname) {
    let name = cname + "=";
    let decodedCookie = decodeURIComponent(document.cookie);
    let ca = decodedCookie.split(';');
    for(let i = 0; i <ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}


async function fetchAndDisplayUserChat() {
    var token = getCookie("jwtToken");
    const url =`http://localhost:8080/chat/messages/${sender}/${receiver}/${sessionId}`;

    const userChatResponse = await fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': "Bearer " + token
        },
    })

    const userChat = await userChatResponse.json();
    messageArea.innerHTML = '';

    console.log(userChat)

    userChat.forEach(chat => {

        //onMessagesReceived(chat.valueOf());
        onMessagesReceived(chat.valueOf());
    });

}
function getAvatarColor(messageSender) {
    var hash = 0;

    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);

    }
    var int=parseInt(hash);
    var index = Math.abs(int % colors.length);
    console.log("---"+index)
    return colors[index];
}

window.addEventListener('load', connect, true);
//usernameForm.addEventListener('submit', fetchAndDisplayUserChat(), true)
messageForm.addEventListener('submit', send, true);