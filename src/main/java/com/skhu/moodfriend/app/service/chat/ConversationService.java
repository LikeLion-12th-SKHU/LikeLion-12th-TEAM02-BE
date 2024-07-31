package com.skhu.moodfriend.app.service.chat;

import com.skhu.moodfriend.app.dto.chat.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConversationService {

    private final Map<Long, List<Message>> conversations = new HashMap<>();
    private final Map<Long, List<LocalDateTime>> messageTimestamps = new HashMap<>();

    public List<Message> getConversation(Long memberId) {
        return conversations.getOrDefault(memberId, new ArrayList<>());
    }

    public void addMessage(Long memberId, Message message) {
        conversations.computeIfAbsent(memberId, k -> new ArrayList<>()).add(message);
        messageTimestamps.computeIfAbsent(memberId, k -> new ArrayList<>()).add(LocalDateTime.now());
    }

    @Transactional
    public void clearConversation(Long memberId) {
        conversations.put(memberId, new ArrayList<>());
        messageTimestamps.put(memberId, new ArrayList<>());
    }

    public List<LocalDateTime> getMessageTimestamps(Long memberId) {
        return messageTimestamps.getOrDefault(memberId, new ArrayList<>());
    }
}
