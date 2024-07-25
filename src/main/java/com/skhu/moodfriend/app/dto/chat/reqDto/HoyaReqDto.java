package com.skhu.moodfriend.app.dto.chat.reqDto;

import com.skhu.moodfriend.app.dto.chat.Message;

import java.util.List;

public record HoyaReqDto(
        String model,
        List<Message> messages
) {
    public HoyaReqDto(String model, String prompt) {
        this(model, List.of(new Message("user", prompt)));
    }
}
