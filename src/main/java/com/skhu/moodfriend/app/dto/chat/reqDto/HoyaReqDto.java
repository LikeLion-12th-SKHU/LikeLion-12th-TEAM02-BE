package com.skhu.moodfriend.app.dto.chat.reqDto;

import com.skhu.moodfriend.app.dto.chat.Message;

import java.util.List;

public record HoyaReqDto(
        String model,
        List<Message> messages
) {
}
