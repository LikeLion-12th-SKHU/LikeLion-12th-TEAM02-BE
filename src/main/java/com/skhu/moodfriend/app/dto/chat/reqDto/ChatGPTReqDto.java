package com.skhu.moodfriend.app.dto.chat.reqDto;

import com.skhu.moodfriend.app.dto.chat.Message;

import java.util.ArrayList;
import java.util.List;

public record ChatGPTReqDto(
        String model,
        List<Message> messages
) {
    public ChatGPTReqDto(String model, String prompt) {
        this(model, new ArrayList<>(List.of(new Message("user", prompt))));
    }
}
