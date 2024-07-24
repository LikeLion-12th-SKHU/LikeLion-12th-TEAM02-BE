package com.skhu.moodfriend.app.dto.chat.resDto;

import com.skhu.moodfriend.app.dto.chat.Message;

import java.util.List;

public record HoyaResDto(
        List<Choice> choices
) {
    public record Choice(
            int index, Message message
    ) {
    }
}
