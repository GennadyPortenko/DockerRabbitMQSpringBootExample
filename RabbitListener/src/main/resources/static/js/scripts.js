$(document).ready(function() {
    disconnect();
    connect();
});

var stompClient = null;

function setConnected(connected) {
   $("#connect").prop("disabled", connected);
   $("#disconnect").prop("disabled", !connected);

   if (connected) {
      $("#conversation").show();
   } else {
      $("#conversation").hide();
   }
   $("#messagesContainer").html("");
}

function connect() {
   var socket = new SockJS('/rabbitaws-websocket');
   stompClient = Stomp.over(socket);
   stompClient.connect({}, function (frame) {
      setConnected(true);
      console.log('Connected: ' + frame);
      stompClient.subscribe('/client/monitor', function (responseMessage) {
         showMessage(JSON.parse(responseMessage.body).text);
      });
   });
}
function disconnect() {
   if (stompClient !== null) {
      stompClient.disconnect();
   }
   setConnected(false);
   console.log("Disconnected");
}
function sendMessage() {
   stompClient.send("/app/hello", {}, JSON.stringify({'text': 'Hello from client.'}));
}
function showMessage(message) {
   if ($('#messagesContainer').find('.message').length > 9) {
     $('#messagesContainer').find('.message').first().remove();
   }
   $('#messagesContainer').append('<div class="message">' + message + '</div>');
}
$(function () {
   $( "form" ).on('submit', function (e) {e.preventDefault();});
   $( "#connect" ).click(function() { connect(); });
   $( "#disconnect" ).click(function() { disconnect(); });
   $( "#send" ).click(function() { sendMessage(); });
});
