package com.skhu.moodfriend.app.dto.chat.resDto;

import com.skhu.moodfriend.app.dto.chat.Message;

import java.util.List;

public record HoyaResDto(
        List<Choice> choices,
        Usage usage
) {
    public record Choice(
            int index, Message message
    ) {}

    public record Usage(
            int prompt_tokens,
            int completion_tokens,
            int total_tokens
    ) {}
}
