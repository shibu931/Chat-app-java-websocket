package com.project.chat.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.project.chat.chat.ChatMessage;
import com.project.chat.chat.MessageType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {
	
	private final SimpMessageSendingOperations messagingTemplate = null;
	
	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String username = (String) headerAccessor.getSessionAttributes().get("username");
		if(username!= null) {
			log.info("User disconnected: {}",username);
			var chatMessage = ChatMessage.builder()
					.type(MessageType.LEAVES)
					.sender(username)
					.build();
			messagingTemplate.convertAndSend("/topic/public",chatMessage);
		}
	}
	
}
