package com.example.musicplayer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserPresenceService {

    private final Set<String> activeSessions = ConcurrentHashMap.newKeySet();

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        if (sessionId != null) {
            activeSessions.add(sessionId);
            broadcastOnlineCount();
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        if (sessionId != null) {
            activeSessions.remove(sessionId);
            broadcastOnlineCount();
        }
    }

    public int getOnlineCount() {
        return activeSessions.size();
    }

    private void broadcastOnlineCount() {
        messagingTemplate.convertAndSend("/topic/online-users", getOnlineCount());
    }
}
