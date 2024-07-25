package com.skhu.moodfriend.app.service.chat;

import com.skhu.moodfriend.app.dto.chat.Message;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConversationService {

    private final Map<Long, List<Message>> conversations = new HashMap<>();

    public List<Message> getConversation(Long memberId) {
        return conversations.getOrDefault(memberId, new ArrayList<>());
    }

    public void addMessage(Long memberId, Message message) {
        conversations.computeIfAbsent(memberId, k -> new ArrayList<>()).add(message);
    }
}
